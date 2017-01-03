package RebornStorage.tiles;

import RebornStorage.multiblocks.MultiBlockCrafter;
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
		if(getMultiblockController() != null){
			getMultiblockController().checkIfMachineIsWhole();
		}

	}
}
