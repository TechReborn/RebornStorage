package RebornStorage.tiles;

import RebornStorage.blocks.BlockMultiCrafter;
import RebornStorage.multiblocks.MultiBlockCrafter;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeProxy;
import com.raoulvdberge.refinedstorage.capability.CapabilityNetworkNodeProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
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

	public void update() {
		if (inv != null) {
			if (inv.hasChanged) {
				inv.hasChanged = false;
				getNode().rebuildPatterns();
			}
		}
	}


	public Inventory inv;
	public Optional<Integer> page = Optional.empty();

	public TileMultiCrafter(String varient) {
		if (varient.equals("storage")) {
			inv = new Inventory(78, "storageBlock", 1, this);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		if (inv == null && data.hasKey("hasInv")) {
			inv = new Inventory(78, "storageBlock", 1, this);
		}
		if (inv != null) {
			inv.readFromNBT(data);
		}
		if (data.hasKey("page")) {
			page = Optional.of(data.getInteger("page"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		if (inv != null) {
			inv.writeToNBT(data);
			data.setBoolean("hasInv", true);
		}
		if (page.isPresent()) {
			data.setInteger("page", page.get());
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

	//RS API

	CraftingNode node;
	CraftingNode clientNode;

	@Override
	public CraftingNode getNode(){
		if(world.isRemote){
			clientNode = new CraftingNode(this);
			return clientNode;
		} else if (node == null){
			node = new CraftingNode(this);
		}
		return node;
	}

	@Override
	public boolean hasCapability(Capability<?> capability,
	                             @Nullable
		                             EnumFacing facing) {
		if(capability == CapabilityNetworkNodeProxy.NETWORK_NODE_PROXY_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability,
	                           @Nullable
		                           EnumFacing facing) {
		if(capability == CapabilityNetworkNodeProxy.NETWORK_NODE_PROXY_CAPABILITY){
			return (T) this;
		}
		return super.getCapability(capability, facing);
	}
}
