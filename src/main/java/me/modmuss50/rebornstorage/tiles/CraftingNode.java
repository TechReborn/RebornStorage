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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import reborncore.RebornCore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CraftingNode implements INetworkNode, ICraftingPatternContainer {

	World world;
	BlockPos pos;
	List<ICraftingPattern> actualPatterns = new ArrayList<>();
	@Nullable
	INetwork network;
	int ticks = 0;

	public ExtendedItemHandler patterns = new ExtendedItemHandler(6 * 13, new ItemHandlerListenerNetworkNode(this), s -> s.getItem() instanceof ICraftingPatternProvider && ((ICraftingPatternProvider) s.getItem()).create(world, s, this).isValid()) {
		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);

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

	//Allows me to to get the stack list
	public abstract static class ExtendedItemHandler extends ItemHandlerBase {

		public ExtendedItemHandler(int size,
		                           @Nullable
			                           Consumer<Integer> listener, Predicate<ItemStack>... validators) {
			super(size, listener, validators);
		}

		public ExtendedItemHandler(int size, Predicate<ItemStack>... validators) {
			super(size, validators);
		}

		public NonNullList<ItemStack> getStacks(){
			return stacks;
		}
	}

	public CraftingNode(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}

	public void rebuildPatterns() {
		this.actualPatterns.clear();
		final CraftingNode node = this;
		if (!world.isRemote && isValidMultiBlock() && !patterns.isEmpty()) {
			actualPatterns.addAll(patterns.getStacks().parallelStream()
				.filter(stack -> stack.getItem() instanceof ICraftingPatternProvider)
				.map(stack -> ((ICraftingPatternProvider) stack.getItem()).create(world, stack, node))
				.filter(ICraftingPattern::isValid)
				.collect(Collectors.toList()));
		}
		if (getNetwork() != null) {
			getNetwork().getCraftingManager().rebuild();
		}
	}

	protected void stateChange(INetwork network, boolean state) {
		if (!state) {
			network.getCraftingManager().getTasks()
				.forEach((task) -> network.getCraftingManager().cancel(task));
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
	public int getSpeedUpdateCount() {
		if (!isValidMultiBlock()) {
			return 0;
		}
		return getTile().getMultiBlock().speed;
	}

	@Override
	public IItemHandler getFacingInventory() {
		return null;
	}

	@Override
	public TileEntity getFacingTile() {
		return null;
	}

	@Override
	public List<ICraftingPattern> getPatterns() {
		return actualPatterns;
	}

	@Override
	public BlockPos getPosition() {
		return pos;
	}

	@Override
	public boolean isBlocked() {
		return false;
	}

	@Override
	public void setBlocked(boolean b) {

	}
}
