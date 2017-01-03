package RebornStorage.tiles;

import RebornStorage.multiblocks.MultiBlockCrafter;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import com.raoulvdberge.refinedstorage.api.network.INetworkMaster;
import com.raoulvdberge.refinedstorage.api.network.INetworkNode;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.MultiblockValidationException;
import reborncore.common.multiblock.rectangular.RectangularMultiblockTileEntityBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 03/01/2017.
 */
public class TileMultiCrafter extends RectangularMultiblockTileEntityBase implements ICraftingPatternContainer , INetworkNode {

	@Override
	public void isGoodForFrame() throws MultiblockValidationException {

	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException {

	}

	@Override
	public void isGoodForTop() throws MultiblockValidationException {

	}

	@Override
	public void isGoodForBottom() throws MultiblockValidationException {

	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException {

	}

	@Override
	public void onMachineActivated() {

	}

	@Override
	public void onMachineDeactivated() {

	}

	@Override
	public MultiblockControllerBase createNewMultiblock() {
		return new MultiBlockCrafter(getWorld());
	}

	@Override
	public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
		return MultiBlockCrafter.class;
	}


	protected INetworkMaster network;
	private BlockPos networkPos;
	private boolean update;

	@Override
	public void update() {
	}

	MultiBlockCrafter getMultiBlock(){
		return (MultiBlockCrafter) getMultiblockController();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(getMultiblockController() != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(getMultiblockController() != null &&  capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getMultiBlock().inv);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public int getSpeedUpdateCount() {
		return 5;
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
		MultiBlockCrafter crafter = getMultiBlock();
		List<ICraftingPattern> patterns = new ArrayList<>();
		if(crafter != null){
			for (int i = 0; i < crafter.inv.getSlots(); i++) {
				ItemStack stack = crafter.inv.getStackInSlot(i);
				if(stack != null){
					if(stack.getItem() instanceof ICraftingPatternProvider){
						patterns.add(((ICraftingPatternProvider) stack.getItem()).create(getWorld(), stack, this));
					}
				}
			}
		}
		return patterns;
	}

	@Override
	public int getEnergyUsage() {
		return 5;
	}

	@Override
	public BlockPos getPosition() {
		return getPos();
	}

	@Override
	public void onConnected(INetworkMaster iNetworkMaster) {
		network = iNetworkMaster;
	}

	@Override
	public void onDisconnected(INetworkMaster iNetworkMaster) {
		network = null;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public boolean canConduct(EnumFacing enumFacing) {
		return true;
	}

	@Override
	public INetworkMaster getNetwork() {
		return network;
	}

	@Override
	public World getNodeWorld() {
		return getWorld();
	}
}
