package me.modmuss50.rebornstorage.tiles;

import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import com.raoulvdberge.refinedstorage.api.network.INetwork;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNode;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.inventory.ItemHandlerBase;
import com.raoulvdberge.refinedstorage.inventory.ItemHandlerListenerNetworkNode;
import com.raoulvdberge.refinedstorage.util.StackUtils;
import me.modmuss50.rebornstorage.RebornStorage;
import me.modmuss50.rebornstorage.multiblocks.MultiBlockCrafter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import reborncore.RebornCore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CraftingNode implements INetworkNode, ICraftingPatternContainer {

	World world;
	BlockPos pos;
	List<ICraftingPattern> actualPatterns = new ArrayList<>();
	@Nullable
	INetwork network;
	int ticks = 0;
	private UUID uuid;

	// An item handler that caches the first available and last used slots.
	public abstract class CachingItemHandler extends ItemHandlerBase {
		private int firstAvailable = 0;
		private int lastUsed = -1;

		public CachingItemHandler(int size, @Nullable Consumer<Integer> listener, Predicate<ItemStack>... validators) {
			super(size, listener, validators);
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

	public CachingItemHandler patterns = new CachingItemHandler(6 * 13, new ItemHandlerListenerNetworkNode(this), s -> s.getItem() instanceof ICraftingPatternProvider && ((ICraftingPatternProvider) s.getItem()).create(world, s, this).isValid()) {
		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);

			markDirty();

			if (!world.isRemote) {
				rebuildPatterns();
			}

			if (network != null) {
				network.getCraftingManager().rebuild();
			}
		}

		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
	};

	public CraftingNode(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}

	public void rebuildPatterns() {
		this.actualPatterns.clear();
		if (!world.isRemote && isValidMultiBlock()) {
			for (int i = 0; i < patterns.getSlots(); i++) {
				ItemStack stack = patterns.getStackInSlot(i);
				if (!stack.isEmpty() && stack.getItem() instanceof ICraftingPatternProvider) {
					ICraftingPattern pattern = ((ICraftingPatternProvider) stack.getItem()).create(world, stack, this);
					if (pattern.isValid()) {
						actualPatterns.add(pattern);
					}
				}
			}
		}

		if (getNetwork() != null) {
			getNetwork().getCraftingManager().rebuild();
		}

	}

	protected void stateChange(INetwork network, boolean state) {
		if (!state) {
			network.getCraftingManager().getTasks()
				.forEach((task) -> network.getCraftingManager().cancel(task.getId()));
			actualPatterns.clear();
		}
		network.getCraftingManager().rebuild();
	}

	@Nullable
	public TileMultiCrafter getTile() {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileMultiCrafter){
			return (TileMultiCrafter) tileEntity;
		}
		//TODO have a way for users to see this?
		RebornCore.logHelper.debug(tileEntity + " is not an instance of TileMultiCrafter, this is an error and your RebornStorage multiblock may not work. Please report to the mod author");
		return null;
	}

	public boolean isValidMultiBlock() {
		TileMultiCrafter tileMultiCrafter = getTile();
		if (tileMultiCrafter == null) {
			return false;
		}
		MultiBlockCrafter multiBlockCrafter = getTile().getMultiBlock();
		if (multiBlockCrafter == null) {
			return false;
		}
		return multiBlockCrafter.isAssembled();
	}

	@Override
	public int getEnergyUsage() {
		return 1;
	}

	@Nonnull
	@Override
	public ItemStack getItemStack() {
		return getTile().getStack();
	}

	@Override
	public void onConnected(INetwork iNetwork) {
		this.network = iNetwork;
		stateChange(network, true);
		rebuildPatterns();
	}

	@Override
	public void onDisconnected(INetwork iNetwork) {
		this.network = null;
		actualPatterns.clear();
		stateChange(iNetwork, true);
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Nullable
	@Override
	public INetwork getNetwork() {
		return network;
	}

	@Override
	public void update() {
		ticks++;
		if (ticks == 1) {
			rebuildPatterns();
		}
	}

	@Override
	public NBTTagCompound write(NBTTagCompound nbtTagCompound) {
		StackUtils.writeItems(patterns, 0, nbtTagCompound);
		return nbtTagCompound;
	}

	@Override
	public BlockPos getPos() {
		return pos;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public void markDirty() {
		if (!world.isRemote) {
			API.instance().getNetworkNodeManager(world).markForSaving();
		}
	}

	@Override
	public String getId() {
		return RebornStorage.MULTI_BLOCK_ID;
	}

	@Override
	public int getSpeedUpgradeCount() {
		if (!isValidMultiBlock()) {
			return 0;
		}
		return getTile().getMultiBlock().speed;
	}

	@Override
	public IItemHandler getConnectedInventory() {
		return null;
	}

	@Override
	public TileEntity getConnectedTile() {
		return null;
	}

	@Override
	public TileEntity getFacingTile() {
		return null;
	}

	@Override
	public EnumFacing getDirection() {
		return null;
	}

	@Override
	public List<ICraftingPattern> getPatterns() {
		return actualPatterns;
	}

	@Override
	public IItemHandlerModifiable getPatternInventory() {
	    if(isValidMultiBlock() && getTile().getVarient() != null && getTile().getVarient().equals("storage")) {
            return patterns;
        }
        return null;
	}

	@Override
	public String getName() {
		return "MultiBlock Crafter";
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
		if(uuid == null){
			uuid = UUID.randomUUID();
		}
		return uuid;
	}

}
