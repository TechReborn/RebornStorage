package RebornStorage.tiles;

import RebornStorage.blocks.BlockMultiCrafter;
import RebornStorage.multiblocks.MultiBlockCrafter;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.api.network.INetworkMaster;
import com.raoulvdberge.refinedstorage.api.network.INetworkNode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.MultiblockValidationException;
import reborncore.common.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import reborncore.common.util.Inventory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Mark on 03/01/2017.
 */
public class TileMultiCrafter extends RectangularMultiblockTileEntityBase implements ICraftingPatternContainer, INetworkNode {

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
			if(multiBlockCrafter.network == null){
				multiBlockCrafter.network = master;
			}
			multiBlockCrafter.rebuildPatterns();
		}
	}

	@Override
	public void onMachineDeactivated() {
		if (getMultiBlock() != null) {
			MultiBlockCrafter multiBlockCrafter = getMultiBlock();
			if(multiBlockCrafter.network == null){
				multiBlockCrafter.network = master;
			}
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

	//RS stuff
	public int getEnergyUsage() {
		if (getMultiBlock() == null) {
			return 0;
		}
		return getMultiBlock().powerUsage;
	}

	public void update() {
		if(inv != null){
			if (inv.hasChanged) {
				inv.hasChanged = false;
				MultiBlockCrafter multiBlockCrafter = getMultiBlock();
				if(multiBlockCrafter != null){
					multiBlockCrafter.rebuildPatterns();
				}
			}
		}
	}

	public int getSpeedUpdateCount() {
		if (getMultiBlock() == null) {
			return 0;
		}
		return getMultiBlock().speed;
	}

	public IItemHandler getFacingInventory() {
		return null;
	}

	@Override
	public TileEntity getFacingTile() {
		return null;
	}

	public List<ICraftingPattern> getPatterns() {
		if (getMultiBlock() == null) {
			return new ArrayList<>();
		}
		return getMultiBlock().actualPatterns;
	}

	INetworkMaster master;

	@Override
	public BlockPos getPosition() {
		return getPos();
	}

	@Override
	public void onConnected(INetworkMaster iNetworkMaster) {
		if (getMultiBlock() != null) {
			getMultiBlock().onConnectionChange(iNetworkMaster, true, pos);
		}
		master = iNetworkMaster;

	}

	@Override
	public void onDisconnected(INetworkMaster iNetworkMaster) {
		if (getMultiBlock() != null) {
			getMultiBlock().onConnectionChange(iNetworkMaster, false, pos);
		}
		master = iNetworkMaster;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public boolean canConduct(EnumFacing enumFacing) {
		return false;
	}

	@Override
	public INetworkMaster getNetwork() {
		if (getMultiBlock() != null) {
			return getMultiBlock().network;
		}
		return null; //Will that cause issues?
	}

	@Override
	public World getNodeWorld() {
		return world;
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

	}

	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		if(inv == null && data.hasKey("hasInv")){
			inv = new Inventory(78, "storageBlock", 1, this);
		}
		if(inv != null){
			inv.readFromNBT(data);
		}
		if(data.hasKey("page")){
			page = Optional.of(data.getInteger("page"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		if(inv != null){
			inv.writeToNBT(data);
			data.setBoolean("hasInv", true);
		}
		if(page.isPresent()){
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

	//TODO add better cap support for it
	//	@Override
	//	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
	//		if (getMultiblockController() != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
	//			return true;
	//		}
	//		return super.hasCapability(capability, facing);
	//	}
	//
	//	@Override
	//	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
	//		if (getMultiblockController() != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
	//			return (T) new InvWrapper(getMultiBlock().inv);
	//		}
	//		return super.getCapability(capability, facing);
	//	}

}
