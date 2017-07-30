package RebornStorage.tiles;

import RebornStorage.blocks.BlockMultiCrafter;
import RebornStorage.multiblocks.MultiBlockCrafter;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNode;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeManager;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeProxy;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.NetworkNode;
import com.raoulvdberge.refinedstorage.capability.CapabilityNetworkNodeProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.MultiblockValidationException;
import reborncore.common.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import reborncore.common.util.Inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Created by Mark on 03/01/2017.
 */
public class TileMultiCrafter extends RectangularMultiblockTileEntityBase implements INetworkNodeProxy {

	NetworkNodeMultiCrafter clientNode;
	NetworkNodeMultiCrafter serverNode;

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
		if (getMultiBlock() != null) {
			MultiBlockCrafter multiBlockCrafter = getMultiBlock();
			multiBlockCrafter.rebuildPatterns();
		}
	}

	@Override
	public void onMachineDeactivated() {
		if (getMultiBlock() != null) {
			MultiBlockCrafter multiBlockCrafter = getMultiBlock();
			multiBlockCrafter.rebuildPatterns();
		}
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
				checkNodes();
				API.instance().discoverNode(world, pos);
				if(!world.isRemote && serverNode != null){
					serverNode.getNetwork().getCraftingManager().rebuild();
				}
			}
		}
	}

	public void checkNodes(){
		if(getMultiBlock() == null){
			return;
		}
		MultiBlockCrafter multiBlockCrafter = getMultiBlock();
		if(world.isRemote){
			multiBlockCrafter.node = clientNode;
		} else {
			multiBlockCrafter.node = serverNode;
		}
		multiBlockCrafter.rebuildPatterns();
	}

	public Inventory inv;
	public Optional<Integer> page = Optional.empty();

	public TileMultiCrafter(String varient) {
		if (varient.equals("storage")) {
			inv = new Inventory(78, "storageBlock", 1, this);
		}
	}

	@Override
	public void onLoad() {
		checkNodes();
		API.instance().discoverNode(world, pos);
		if(!world.isRemote && serverNode != null){
			serverNode.getNetwork().getCraftingManager().rebuild();
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

	@Nonnull
	@Override
	public INetworkNode getNode() {
		if(!world.isRemote){
			INetworkNodeManager manager = API.instance().getNetworkNodeManager(this.world);
			NetworkNodeMultiCrafter node = (NetworkNodeMultiCrafter)manager.getNode(this.pos);
			if (node == null || !node.getId().equals("rebornstorage.multicrafter")) {
				manager.setNode(this.pos, node = getNewNode());
				manager.markForSaving();
			}
			serverNode = node;
			checkNodes();
			return node;
		}

		if (clientNode == null) {
			clientNode = getNewNode();
		}
		checkNodes();
		return clientNode;
	}

	public NetworkNodeMultiCrafter getNewNode() {
		return new NetworkNodeMultiCrafter(this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability,
	                             @Nullable
		                             EnumFacing side) {
		if (capability == CapabilityNetworkNodeProxy.NETWORK_NODE_PROXY_CAPABILITY) {
			return true;
		}

		return super.hasCapability(capability, side);
	}

	@Override
	public <T> T getCapability(Capability<T> capability,
	                           @Nullable
		                           EnumFacing side) {
		if (capability == CapabilityNetworkNodeProxy.NETWORK_NODE_PROXY_CAPABILITY) {
			return CapabilityNetworkNodeProxy.NETWORK_NODE_PROXY_CAPABILITY.cast(this);
		}

		return super.getCapability(capability, side);
	}
}
