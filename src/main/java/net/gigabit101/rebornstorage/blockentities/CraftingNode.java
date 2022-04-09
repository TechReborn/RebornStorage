package net.gigabit101.rebornstorage.blockentities;

import com.refinedmods.refinedstorage.api.autocrafting.ICraftingPattern;
import com.refinedmods.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.refinedmods.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeManager;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.ConnectivityStateChangeCause;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.inventory.item.BaseItemHandler;
import com.refinedmods.refinedstorage.util.StackUtils;
import net.gigabit101.rebornstorage.RebornStorage;
import net.gigabit101.rebornstorage.RebornStorageEventHandler;
import net.gigabit101.rebornstorage.init.ModBlocks;
import net.gigabit101.rebornstorage.init.ModItems;
import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.multiblocks.MultiBlockCrafter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CraftingNode extends NetworkNode implements ICraftingPatternContainer {
    Level world;
    BlockPos pos;
    List<ICraftingPattern> actualPatterns = new ArrayList<>();
    @Nullable
    INetwork network;
    int ticks = 0;
    private UUID uuid;
    private boolean needsRebuild = false;
    private boolean isValid;
    private int speed = -1;

    public static int craftingSpeed = 15;
    public static int invUpdateTime = 5;

    public CraftingNode(Level level, BlockPos pos) {
        super(level, pos);
        this.world = level;
        this.pos = pos;
    }

    public void invalidate() {
        isValid = false;
    }

    // An item handler that caches the first available and last used slots.
    public abstract class CachingItemHandler extends BaseItemHandler {
        private int firstAvailable = 0;
        private int lastUsed = -1;

        protected HashMap<Integer, ICraftingPattern> craftingPatternMap = new HashMap<>();

        public CachingItemHandler(int size) {
            super(size);
        }

        @Override
        protected void onLoad() {
            super.onLoad();
            firstAvailable = getSlots();
            lastUsed = -1;
            for (int i = 0; i < getSlots(); i++) {
                if (getStackInSlot(i).isEmpty()) {
                    firstAvailable = Integer.min(firstAvailable, i);
                } else {
                    lastUsed = Integer.max(lastUsed, i);
                }
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            for (int i = slot; i < firstAvailable && i >= 0 && getStackInSlot(i).isEmpty(); i--) {
                firstAvailable = i;
            }
            for (int i = slot; i == firstAvailable && i < getSlots() && !getStackInSlot(i).isEmpty(); i++) {
                firstAvailable = i + 1;
            }
            for (int i = slot; i > lastUsed && i < getSlots() && !getStackInSlot(i).isEmpty(); i++) {
                lastUsed = i;
            }
            for (int i = slot; i == lastUsed && i >= 0 && getStackInSlot(i).isEmpty(); i--) {
                lastUsed = i - 1;
            }
            craftingPatternMap.remove(slot); //When the slot changes un cache it

        }

        public int getFirstAvailable() {
            return firstAvailable;
        }

        public int getLastUsed() {
            return lastUsed;
        }

        public boolean isEmpty() {
            return lastUsed == -1;
        }

        public boolean isFull() {
            return firstAvailable == getSlots();
        }
    }

    public CachingItemHandler patterns = new CachingItemHandler(6 * 13) {

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if(network != null) {
                markDirty();
            }
            needsRebuild = true;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    public void rebuildPatterns(String reason) {
        this.actualPatterns.clear();
        if (!world.isClientSide && isValidMultiBlock(true)) {
            if (!patterns.isEmpty()) {
                for (int i = 0; i < patterns.getSlots(); i++) {
                    ItemStack stack = patterns.getStackInSlot(i);
                    if (!stack.isEmpty() && stack.getItem() instanceof ICraftingPatternProvider) {
                        if (patterns.craftingPatternMap.containsKey(i)) {
                            actualPatterns.add(patterns.craftingPatternMap.get(i));
                        } else {
                            ICraftingPattern pattern = ((ICraftingPatternProvider) stack.getItem()).create(world, stack, this);
                            if (pattern.isValid()) {
                                actualPatterns.add(pattern);
                                patterns.craftingPatternMap.put(i, pattern);
                            }
                        }
                    } else {
                        patterns.craftingPatternMap.remove(i);
                    }
                }
            } else {
                patterns.craftingPatternMap.clear();
            }
        }
        if (getNetwork() != null) {
            RebornStorageEventHandler.queue(network.getCraftingManager(), this, reason);
        }
    }

    protected void stateChange(INetwork network, boolean state, String reason) {
        if (!state) {
            network.getCraftingManager().getTasks().forEach((task) -> network.getCraftingManager().cancel(task.getId()));
            actualPatterns.clear();
        }
        RebornStorageEventHandler.queue(network.getCraftingManager(), this, reason);
    }

    @Nullable
    public BlockEntityMultiCrafter getTile() {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof BlockEntityMultiCrafter) {
            return (BlockEntityMultiCrafter) tileEntity;
        }
        RebornStorage.logger.warning(tileEntity + " is not an instance of TileMultiCrafter, this is an error and your RebornStorage multiblock may not work. Please report to the mod author");
        return null;
    }

    public boolean isValidMultiBlock(boolean check) {
        if (!check && isValid) {
            return true;
        }
        BlockEntityMultiCrafter blockEntityMultiCrafter = getTile();
        if (blockEntityMultiCrafter == null) {
            return false;
        }
        MultiBlockCrafter multiBlockCrafter = getTile().getMultiBlock();
        if (multiBlockCrafter == null) {
            return false;
        }
        isValid = multiBlockCrafter.isAssembled();

        return isValid;
    }

    @Override
    public int getEnergyUsage()
    {
        if(getBlock() == ModBlocks.BLOCK_MULTI_FRAME.get()) return 0;
        if(getBlock() == ModBlocks.BLOCK_MULTI_HEAT.get()) return 0;
        if(getBlock() == ModBlocks.BLOCK_MULTI_CPU.get()) return (5 * getCraftingCpus());
        if(getBlock() == ModBlocks.BLOCK_MULTI_STORAGE.get()) return (10 * getStorage());

        return 0;
    }

    public Block getBlock()
    {
        return getTile().getBlockState().getBlock();
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        return new ItemStack(getBlock());
    }

    @Override
    public void onConnected(INetwork iNetwork) {
        this.network = iNetwork;
        stateChange(network, true, "connected to network");
        rebuildPatterns("connected to network");
    }

    @Override
    public void onDisconnected(INetwork iNetwork) {
        this.network = null;
        actualPatterns.clear();
        stateChange(iNetwork, true, "disconnected from network");
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Nullable
    @Override
    public INetwork getNetwork() {
        return network;
    }

    @Override
    public void update() {
        super.update();
        ticks++;
        if (ticks == 1) {
            rebuildPatterns("first tick rebuild");
        }
        if (needsRebuild && world.getLevelData().getGameTime() % (invUpdateTime * 20) == 0) {
            rebuildPatterns("inv slot change");
            needsRebuild = false;
        }
    }

    @Override
    public CompoundTag write(CompoundTag nbtTagCompound) {
        StackUtils.writeItems(patterns, 0, nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void read(CompoundTag tag) {
        StackUtils.readItems(patterns, 0, tag);
        super.read(tag);
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public Level getLevel() {
        return world;
    }

    @Override
    public void markDirty() {
        if (world != null && !world.isClientSide)
        {
            try
            {
                INetworkNodeManager networkNodeManager = API.instance().getNetworkNodeManager((ServerLevel) world);
                if(networkNodeManager != null)
                {
                    networkNodeManager.markForSaving();
                }
            } catch (Exception ignored) {}
        }
    }

    @Override
    protected void onConnectedStateChange(INetwork network, boolean state, ConnectivityStateChangeCause cause) {
        super.onConnectedStateChange(network, state, cause);
        network.getCraftingManager().invalidate();
    }


    @Override
    public ResourceLocation getId() {
        return Constants.MULTI_BLOCK_ID;
    }

    @Override
    public void setOwner(@Nullable UUID uuid) {}

    @Nullable
    @Override
    public UUID getOwner() {
        return null;
    }

    @Override
    public int getUpdateInterval() {
        return Math.max(craftingSpeed - getCraftingCpus(), 1);
    }

    @Override
    public int getMaximumSuccessfulCraftingUpdates() {
        //Dont do anything if we have less cpus than the craftings speed
        if (getCraftingCpus() < craftingSpeed) {
            return 1;
        }
        return Math.max(getCraftingCpus() / Math.max(craftingSpeed, 1), 1);
    }

    public int getCraftingCpus() {
        if (isValid && speed != -1) {
            return speed;
        }
        if (!isValidMultiBlock(false)) {
            return 0;
        }
        speed = getTile().getMultiBlock().speed;
        return speed;
    }

    public int getStorage()
    {
        if(!isValidMultiBlock(false)) {
            return 0;
        }
        return getTile().getMultiBlock().pages;
    }

    @Override
    public IItemHandler getConnectedInventory() {
        return null;
    }

    @Nullable
    @Override
    public IFluidHandler getConnectedFluidInventory() {
        return null;
    }

    @Override
    public BlockEntity getConnectedBlockEntity() {
        return null;
    }

    @Override
    public BlockEntity getFacingBlockEntity() {
        return null;
    }

    @Override
    public Direction getDirection() {
        return null;
    }

    @Override
    public List<ICraftingPattern> getPatterns() {
        return actualPatterns;
    }

    @Override
    public IItemHandlerModifiable getPatternInventory() {
        if (isValidMultiBlock(true) && getTile() != null && getTile().getBlockState().getBlock() == ModBlocks.BLOCK_MULTI_STORAGE.get()) {
            return patterns;
        }
        return null;
    }

    @Override
    public Component getName() {
        return new TextComponent("MultiBlock Crafter");
    }

    @Override
    public BlockPos getPosition() {
        return pos;
    }

    @Nullable
    @Override
    public ICraftingPatternContainer getRootContainer() {
        return null;
    }

    @Override
    public UUID getUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }

    @Override
    public void unlock() {
    }
}
