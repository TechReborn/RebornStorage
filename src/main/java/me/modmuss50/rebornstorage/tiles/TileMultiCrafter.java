package me.modmuss50.rebornstorage.tiles;

import me.modmuss50.rebornstorage.RebornStorage;
import me.modmuss50.rebornstorage.blocks.BlockMultiCrafter;
import me.modmuss50.rebornstorage.init.ModBlocks;
import me.modmuss50.rebornstorage.multiblocks.MultiBlockCrafter;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeManager;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeProxy;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.capability.CapabilityNetworkNodeProxy;
import com.raoulvdberge.refinedstorage.inventory.ItemHandlerBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.MultiblockValidationException;
import reborncore.common.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import reborncore.common.util.Inventory;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by Mark on 03/01/2017.
 */
public class TileMultiCrafter extends RectangularMultiblockTileEntityBase implements INetworkNodeProxy {

	@Override
	public void isGoodForFrame() throws MultiblockValidationException {
		if (!getVarient().equals("frame")) {
			throw new MultiblockValidationException(getVarient() + " is not valid for the frame of the block");
		}
	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException {
		if (!getVarient().equals("heat")) {
			throw new MultiblockValidationException(getVarient() + " is not valid for the sides of the block");
		}
	}

	@Override
	public void isGoodForTop() throws MultiblockValidationException {
		if (!getVarient().equals("heat")) {
			throw new MultiblockValidationException(getVarient() + " is not valid for the sides of the block");
		}
	}

	@Override
	public void isGoodForBottom() throws MultiblockValidationException {
		if (!getVarient().equals("heat")) {
			throw new MultiblockValidationException(getVarient() + " is not valid for the sides of the block");
		}
	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException {
		if (!getVarient().equals("cpu") && !getVarient().equals("storage")) {
			throw new MultiblockValidationException(getVarient() + " is not valid for the inside of the block");
		}
	}

	@Override
	public void onMachineActivated() {
		getNode().rebuildPatterns();
	}

	@Override
	public void onMachineDeactivated() {
		getNode().rebuildPatterns();
	}

	String getVarient() {
		return world.getBlockState(pos).getValue(BlockMultiCrafter.VARIANTS);
	}

	@Override
	public MultiblockControllerBase createNewMultiblock() {
		return new MultiBlockCrafter(getWorld());
	}

	@Override
	public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
		return MultiBlockCrafter.class;
	}

	public MultiBlockCrafter getMultiBlock() {
		return (MultiBlockCrafter) getMultiblockController();
	}

	public Optional<Integer> page = Optional.empty();
	public Optional<Integer> lastPage = Optional.empty();

	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);

		//Old code to allow multiblocks in the old format to be automatticly updated.
		if (data.hasKey("hasInv")) {
			Inventory oldInv = new Inventory(78, "storageBlock", 1, this);
			oldInv.readFromNBT(data);
			ItemHandlerBase itemHandler = getNode().patterns;
			for (int i = 0; i < oldInv.contents.length; i++) {
				ItemStack stack = oldInv.getStackInSlot(i);
				if (!stack.isEmpty()) {
					itemHandler.setStackInSlot(i, stack.copy());
				}
			}
			getNode().markDirty();
		}
		if (data.hasKey("page")) {
			page = Optional.of(data.getInteger("page"));
		}
		if (data.hasKey("lastPage")) {
			lastPage = Optional.of(data.getInteger("lastPage"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		if (page.isPresent()) {
			data.setInteger("page", page.get());
		}
		if (lastPage.isPresent()) {
			data.setInteger("lastPage", lastPage.get());
		}
		return super.writeToNBT(data);
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	public TileMultiCrafter() {
	}

	ItemStack stack = ItemStack.EMPTY;

	public ItemStack getStack() {
		if (stack.isEmpty()) {
			stack = new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, ModBlocks.BLOCK_MULTI_CRAFTER.getMetaFromState(world.getBlockState(getPos())));
		}
		return stack;
	}

	public void updateLastPage(int page){
		if(getMultiBlock() != null){
			for (IMultiblockPart part : getMultiBlock().connectedParts) {
				TileMultiCrafter tile = (TileMultiCrafter) part;
				tile.lastPage = Optional.of(page);
			}
		}
		this.lastPage = Optional.of(page);
	}

	public int getValidLastPage(){
		if(lastPage.isPresent() && getMultiBlock() != null){
			int page = lastPage.get();
			if(page <= getMultiBlock().invs.size()){
				return page;
			}
		}
		return 0;
	}

	//RS API

	CraftingNode node;
	CraftingNode clientNode;

	@Override
	public CraftingNode getNode() {
		if (world.isRemote) {
			if (clientNode == null) {
				clientNode = new CraftingNode(world, getPos());
			}
			return clientNode;
		}
		INetworkNodeManager manager = API.instance().getNetworkNodeManager(this.world);
		CraftingNode node = (CraftingNode) manager.getNode(this.pos);
		if (node == null || !node.getId().equals(RebornStorage.MULTI_BLOCK_ID)) {
			manager.setNode(this.pos, node = new CraftingNode(world, getPos()));
			manager.markForSaving();
		}

		return node;
	}

	@Override
	public boolean hasCapability(Capability<?> capability,
	                             @Nullable
		                             EnumFacing facing) {
		if (capability == CapabilityNetworkNodeProxy.NETWORK_NODE_PROXY_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability,
	                           @Nullable
		                           EnumFacing facing) {
		if (capability == CapabilityNetworkNodeProxy.NETWORK_NODE_PROXY_CAPABILITY) {
			return (T) this;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {

	}
}
