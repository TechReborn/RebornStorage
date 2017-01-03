package RebornStorage.tiles;

import RebornStorage.multiblocks.MultiBlockCrafter;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.MultiblockValidationException;
import reborncore.common.multiblock.rectangular.RectangularMultiblockTileEntityBase;

/**
 * Created by Mark on 03/01/2017.
 */
public class TileMultiCrafter extends RectangularMultiblockTileEntityBase {
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
		return new MultiBlockCrafter(worldObj);
	}

	@Override
	public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
		return MultiBlockCrafter.class;
	}

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
			return (T) new InvWrapper(getMultiBlock().inventory);
		}
		return super.getCapability(capability, facing);
	}

}
