package net.gigabit101.rebornstorage.blockentities;

import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeManager;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeProxy;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.capability.NetworkNodeProxyCapability;
import net.gigabit101.rebornstorage.containers.ContainerMultiCrafter;
import net.gigabit101.rebornstorage.core.multiblock.MultiblockControllerBase;
import net.gigabit101.rebornstorage.core.multiblock.MultiblockValidationException;
import net.gigabit101.rebornstorage.core.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import net.gigabit101.rebornstorage.init.ModBlocks;
import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.multiblocks.MultiBlockCrafter;
import net.gigabit101.rebornstorage.nodes.CraftingNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;

public class BlockEntityMultiCrafter extends RectangularMultiblockTileEntityBase implements MenuProvider, INetworkNodeProxy<CraftingNode>
{

    @Override
    public void isGoodForFrame() throws MultiblockValidationException
    {
        if (level == null) return;
        if (getBlockPos() == null) return;
        if (level.getBlockState(getBlockPos()) == null) return;

        Block block = level.getBlockState(getBlockPos()).getBlock();
        if (block != ModBlocks.BLOCK_MULTI_FRAME.get())
        {
            throw new MultiblockValidationException(block.getDescriptionId() + " is not valid for the frame of the block");
        }
    }

    @Override
    public void isGoodForSides() throws MultiblockValidationException
    {
        if (level == null) return;
        if (getBlockPos() == null) return;
        if (level.getBlockState(getBlockPos()) == null) return;

        Block block = level.getBlockState(getBlockPos()).getBlock();
        if (block != ModBlocks.BLOCK_MULTI_HEAT.get())
        {
            throw new MultiblockValidationException(block.getDescriptionId() + " is not valid for the sides of the block");
        }
    }

    @Override
    public void isGoodForTop() throws MultiblockValidationException
    {
        if (level == null) return;
        if (getBlockPos() == null) return;
        if (level.getBlockState(getBlockPos()) == null) return;

        Block block = level.getBlockState(getBlockPos()).getBlock();
        if (block != ModBlocks.BLOCK_MULTI_HEAT.get())
        {
            throw new MultiblockValidationException(block.getDescriptionId() + " is not valid for the top of the block");
        }
    }

    @Override
    public void isGoodForBottom() throws MultiblockValidationException
    {
        if (level == null) return;
        if (getBlockPos() == null) return;
        if (level.getBlockState(getBlockPos()) == null) return;

        Block block = level.getBlockState(getBlockPos()).getBlock();

        if (block != ModBlocks.BLOCK_MULTI_HEAT.get())
        {
            throw new MultiblockValidationException(block.getDescriptionId() + " is not valid for the bottom of the block");
        }
    }

    @Override
    public void isGoodForInterior() throws MultiblockValidationException
    {
        if (level == null) return;
        if (getBlockPos() == null) return;
        if (level.getBlockState(getBlockPos()) == null) return;

        Block block = level.getBlockState(getBlockPos()).getBlock();

        if (block != ModBlocks.BLOCK_MULTI_CPU.get() && block != ModBlocks.BLOCK_MULTI_STORAGE.get())
        {
            throw new MultiblockValidationException(block.getDescriptionId() + " is not valid for the inside of the block");
        }
    }

    @Override
    public void onMachineActivated()
    {
        getNode().rebuildPatterns("machine activated");
    }

    @Override
    public void onMachineDeactivated()
    {
        getNode().rebuildPatterns("machine deactivated");
    }

    @Override
    public MultiblockControllerBase createNewMultiblock()
    {
        return new MultiBlockCrafter(getLevel());
    }

    @Override
    public Class<? extends MultiblockControllerBase> getMultiblockControllerType()
    {
        return MultiBlockCrafter.class;
    }

    public MultiBlockCrafter getMultiBlock()
    {
        return (MultiBlockCrafter) getMultiblockController();
    }

    public Optional<Integer> page = Optional.empty();

    public BlockEntityMultiCrafter(BlockPos blockPos, BlockState blockstate)
    {
        super(ModBlocks.CRAFTER_TILE.get(), blockPos, blockstate);
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        load(getUpdateTag());
    }

    @Override
    public void load(CompoundTag data)
    {
        super.load(data);

        if (data.get("page") != null)
        {
            page = Optional.of(data.getInt("page"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag data)
    {
        super.saveAdditional(data);
        if (page.isPresent())
        {
            data.putInt("page", page.get());
        }
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    //RS API
    CraftingNode clientNode;

    @Override
    public CraftingNode getNode()
    {
        if (level.isClientSide)
        {
            if (clientNode == null)
            {
                clientNode = new CraftingNode(level, worldPosition);
            }
            return clientNode;
        }
        INetworkNodeManager manager = API.instance().getNetworkNodeManager((ServerLevel) level);
        INetworkNode node = manager.getNode(worldPosition);
        if (node == null || !node.getId().equals(Constants.MULTI_BLOCK_ID))
        {
            manager.setNode(worldPosition, node = new CraftingNode(level, worldPosition));
            manager.markForSaving();
        }

        if (node == null)
        {
            throw new IllegalStateException("No network node present at " + worldPosition.toString() + ", consider removing the block at this position");
        }
        return (CraftingNode) node;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability)
    {
        if (capability == NetworkNodeProxyCapability.NETWORK_NODE_PROXY_CAPABILITY)
        {
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(capability);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side)
    {
        if (capability == NetworkNodeProxyCapability.NETWORK_NODE_PROXY_CAPABILITY)
        {
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(capability, side);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player)
    {
        return new ContainerMultiCrafter(id, playerInventory, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TextComponent("MultiBlock Crafter");
    }
}
