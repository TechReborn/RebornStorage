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
import reborncore.common.util.Inventory;

import java.util.*;

/**
 * Created by Mark on 03/01/2017.
 */
public class MultiBlockCrafter extends RectangularMultiblockControllerBase {

	public Map<Integer, Inventory> invs = new TreeMap<>();

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
		invs.clear();
		updateInfo();
	}


	public void updateInfo(){
		powerUsage = 0;
		speed = 0;
		pages = 0;
		invs.clear();
		for(IMultiblockPart part : connectedParts){
			if(part.getBlockState().getValue(BlockMultiCrafter.VARIANTS).equals("storage")){
				pages ++;
				powerUsage += 5;
				if(part instanceof TileMultiCrafter){
					TileMultiCrafter tile = (TileMultiCrafter) part;
					int id = 0;
					boolean genId = false;
					if(tile.page.isPresent()){
						if(!invs.containsKey(tile.page.get())){
							id = tile.page.get();
						} else {
							genId = true;
						}
					} else {
						genId = true;
					}
					if(genId){
						id = invs.size() + 1; //Does this need +1?
						tile.page = Optional.of(id);
					}
					invs.put(id, tile.inv);
				}

			}
			if(part.getBlockState().getValue(BlockMultiCrafter.VARIANTS).equals("cpu")){
				powerUsage += 10;
				speed++;
			}
		}
	}

	public Inventory getInvForPage(int page){
		return invs.get(page);
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

	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {

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

	}

	public List<ICraftingPattern> actualPatterns = new ArrayList();
	public INetworkMaster network;

	public void rebuildPatterns() {
		try
		{
			this.actualPatterns.clear();
			if (!isAssembled())
			{
				return;
			}
			updateInfo();
			for (HashMap.Entry<Integer, Inventory> entry : invs.entrySet())
			{
				for (int i = 0; i < entry.getValue().getSizeInventory(); ++i)
				{
					ItemStack patternStack = entry.getValue().getStackInSlot(i);
					if (patternStack != null && patternStack.getItem() instanceof ICraftingPatternProvider)
					{
						ICraftingPattern pattern = ((ICraftingPatternProvider) patternStack.getItem()).create(worldObj, patternStack, getReferenceTile());
						if (pattern.isValid())
						{
							this.actualPatterns.add(pattern);
						}
					}
				}
			}
			if (network != null)
			{
				network.rebuildPatterns();
			}
		}
		catch (Exception e){}
	}

	public void onConnectionChange(INetworkMaster network, boolean state, BlockPos pos) {
		if (!state) {
			network.getCraftingTasks().stream().filter((task) -> task.getPattern().getContainer().getPosition().equals(pos)).forEach(network::cancelCraftingTask);
		}

		rebuildPatterns();
		this.network = network;
	}

	private TileMultiCrafter getReferenceTile(){
		return (TileMultiCrafter) worldObj.getTileEntity(getReferenceCoord().toBlockPos());
	}
}
