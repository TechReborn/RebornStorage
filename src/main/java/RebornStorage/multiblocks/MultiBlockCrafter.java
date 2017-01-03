package RebornStorage.multiblocks;

import RebornStorage.blocks.BlockMultiCrafter;
import RebornStorage.tiles.TileMultiCrafter;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import com.raoulvdberge.refinedstorage.api.network.INetworkMaster;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.MultiblockValidationException;
import reborncore.common.multiblock.rectangular.RectangularMultiblockControllerBase;
import reborncore.common.util.Inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mark on 03/01/2017.
 */
public class MultiBlockCrafter extends RectangularMultiblockControllerBase {

	public MultiBlockInventory inv = new MultiBlockInventory(5, "multicrafter", 1, this);

	public int powerUsage = 0;
	public int speed = 0;

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
		int slots = 0;
		powerUsage = 0;
		speed = 0;
		for(IMultiblockPart part : connectedParts){
			if(part.getBlockState().getValue(BlockMultiCrafter.VARIANTS).equals("storage")){
				slots += 10;
				powerUsage += 5;
			}
			if(part.getBlockState().getValue(BlockMultiCrafter.VARIANTS).equals("cpu")){
				slots += 10;
				powerUsage += 10;
				speed++;
			}
		}
		inv.setSize(slots);
		System.out.println(slots + " slots");
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
		return (9 * 3);
	}

	@Override
	protected int getMaximumXSize() {
		return 16;
	}

	@Override
	protected int getMaximumZSize() {
		return 16;
	}

	@Override
	protected int getMaximumYSize() {
		return 16;
	}

	@Override
	protected int getMinimumXSize() {
		return 3;
	}

	@Override
	protected int getMinimumYSize() {
		return 3;
	}

	@Override
	protected int getMinimumZSize() {
		return 3;
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
        inv.writeToNBT(nbtTagCompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
        inv.readFromNBT(nbtTagCompound);
	}

	@Override
	public void formatDescriptionPacket(NBTTagCompound nbtTagCompound) {
		writeToNBT(nbtTagCompound);
	}

	@Override
	public void decodeDescriptionPacket(NBTTagCompound nbtTagCompound) {
		readFromNBT(nbtTagCompound);
	}

    public MultiBlockInventory getInv()
    {
        return inv;
    }

    //RS things:


	public List<ICraftingPattern> actualPatterns = new ArrayList();

	public void rebuildPatterns() {
		this.actualPatterns.clear();
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack patternStack = inv.getStackInSlot(i);
			if (patternStack != null) {
				ICraftingPattern pattern = ((ICraftingPatternProvider) patternStack.getItem()).create(worldObj, patternStack, getReferenceTile());
				if (pattern.isValid()) {
					this.actualPatterns.add(pattern);
				}
			}
		}

	}

	public void onConnectionChange(INetworkMaster network, boolean state, BlockPos pos) {
		if (!state) {
			network.getCraftingTasks().stream().filter((task) -> task.getPattern().getContainer().getPosition().equals(pos)).forEach(network::cancelCraftingTask);
		}

		network.rebuildPatterns();
	}



	private TileMultiCrafter getReferenceTile(){
		return (TileMultiCrafter) worldObj.getTileEntity(getReferenceCoord().toBlockPos());
	}


}
