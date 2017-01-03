package RebornStorage.multiblocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.rectangular.RectangularMultiblockControllerBase;
import reborncore.common.util.Inventory;

/**
 * Created by Mark on 03/01/2017.
 */
public class MultiBlockCrafter extends RectangularMultiblockControllerBase {

	public MultiBlockInventory inventory = new MultiBlockInventory(5, "multicrafter", 1, this);


	public MultiBlockCrafter(World world) {
		super(world);
	}

	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart iMultiblockPart, NBTTagCompound nbtTagCompound) {

	}

	@Override
	protected void onBlockAdded(IMultiblockPart iMultiblockPart) {

	}

	@Override
	protected void onBlockRemoved(IMultiblockPart iMultiblockPart) {

	}

	@Override
	protected void onMachineAssembled() {
		System.out.println("New block");
	}

	@Override
	protected void onMachineRestored() {

	}

	@Override
	protected void onMachinePaused() {

	}

	@Override
	protected void onMachineDisassembled() {
		System.out.println("Invalid");
	}

	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return 1;
	}

	@Override
	protected int getMaximumXSize() {
		return 256;
	}

	@Override
	protected int getMaximumZSize() {
		return 256;
	}

	@Override
	protected int getMaximumYSize() {
		return 256;
	}

	@Override
	protected int getMinimumXSize() {
		return 2;
	}

	@Override
	protected int getMinimumYSize() {
		return 2;
	}

	@Override
	protected int getMinimumZSize() {
		return 2;
	}

	@Override
	protected void onAssimilate(MultiblockControllerBase multiblockControllerBase) {

	}

	@Override
	protected void onAssimilated(MultiblockControllerBase multiblockControllerBase) {

	}

	@Override
	protected boolean updateServer() {
		return true;
	}

	@Override
	protected void updateClient() {

	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		inventory.writeToNBT(nbtTagCompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		inventory.readFromNBT(nbtTagCompound);
	}

	@Override
	public void formatDescriptionPacket(NBTTagCompound nbtTagCompound) {
		writeToNBT(nbtTagCompound);
	}

	@Override
	public void decodeDescriptionPacket(NBTTagCompound nbtTagCompound) {
		readFromNBT(nbtTagCompound);
	}
}
