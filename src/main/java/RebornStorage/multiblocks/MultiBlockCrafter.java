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
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.rectangular.RectangularMultiblockControllerBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mark on 03/01/2017.
 */
public class MultiBlockCrafter extends RectangularMultiblockControllerBase {

//	public MultiBlockInventory inv = new MultiBlockInventory(5, "multicrafter", 1, this);

	public HashMap<Integer, MultiBlockInventory> invs = new HashMap<>();

	public int powerUsage = 0;
	public int speed = 0;
	public int pages = 0;

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
		updateInfo();
	}


	public void updateInfo(){
		powerUsage = 0;
		speed = 0;
		pages = 0;
		for(IMultiblockPart part : connectedParts){
			if(part.getBlockState().getValue(BlockMultiCrafter.VARIANTS).equals("storage")){
				pages ++;
				powerUsage += 5;
			}
			if(part.getBlockState().getValue(BlockMultiCrafter.VARIANTS).equals("cpu")){
				powerUsage += 10;
				speed++;
			}
		}
	}

	public MultiBlockInventory createOrGetInv(int page){
		if(invs.containsKey(page)){
			return invs.get(page);
		}
		MultiBlockInventory newInv = new MultiBlockInventory(78, "multicrafter." + page, 1, this);
		invs.put(page, newInv);
		return newInv;
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
		tick();
		return true;
	}

	@Override
	protected void updateClient() {

	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		nbtTagCompound.setInteger("pages", invs.size());
		for(HashMap.Entry<Integer, MultiBlockInventory> entry : invs.entrySet()){
			NBTTagCompound tagCompound = new NBTTagCompound();
			entry.getValue().writeToNBT(tagCompound);
			nbtTagCompound.setTag("page" + entry.getKey(), tagCompound);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		for (int i = 0; i < nbtTagCompound.getInteger("pages"); i++) {
			MultiBlockInventory inventory = createOrGetInv(i);
			inventory.readFromNBT(nbtTagCompound.getCompoundTag("page" + i));
		}
	}

	@Override
	public void formatDescriptionPacket(NBTTagCompound nbtTagCompound) {
		writeToNBT(nbtTagCompound);
	}

	@Override
	public void decodeDescriptionPacket(NBTTagCompound nbtTagCompound) {
		readFromNBT(nbtTagCompound);
	}

    //RS things:

	public void tick(){
			boolean shouldUpdate = false;
			for(HashMap.Entry<Integer, MultiBlockInventory> entry : invs.entrySet()){
				if(entry.getValue().hasChanged){
					shouldUpdate = true;
				}
				entry.getValue().hasChanged = false;
			}

			if(shouldUpdate){
				rebuildPatterns();
				System.out.println(network);
				if(network != null){
					network.rebuildPatterns();
				}
			}
	}

	public List<ICraftingPattern> actualPatterns = new ArrayList();
	public INetworkMaster network;

	public void rebuildPatterns() {
		this.actualPatterns.clear();
		if(!isAssembled()){
			return;
		}
		for(HashMap.Entry<Integer, MultiBlockInventory> entry : invs.entrySet()){
			for (int i = 0; i < entry.getValue().getSizeInventory(); ++i) {
				ItemStack patternStack = entry.getValue().getStackInSlot(i);
				if (patternStack != null && patternStack.getItem() instanceof ICraftingPatternProvider) {
					ICraftingPattern pattern = ((ICraftingPatternProvider) patternStack.getItem()).create(worldObj, patternStack, getReferenceTile());
					if (pattern.isValid()) {
						this.actualPatterns.add(pattern);
					}
				}
			}
		}
	}



	public void onConnectionChange(INetworkMaster network, boolean state, BlockPos pos) {
		if (!state) {
			network.getCraftingTasks().stream().filter((task) -> task.getPattern().getContainer().getPosition().equals(pos)).forEach(network::cancelCraftingTask);
		}

		network.rebuildPatterns();
		this.network = network;
	}

	private TileMultiCrafter getReferenceTile(){
		return (TileMultiCrafter) worldObj.getTileEntity(getReferenceCoord().toBlockPos());
	}


}
