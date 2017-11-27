package RebornStorage.multiblocks;

import RebornStorage.blocks.BlockMultiCrafter;
import RebornStorage.tiles.TileMultiCrafter;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import reborncore.RebornCore;
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
		updateInfo();
		hasRebuiltRecently = false;
		rebuildPatterns();
	}

	public void updateInfo() {
		powerUsage = 0;
		speed = 0;
		pages = 0;
		invs.clear();
		TreeMap<Integer, TileMultiCrafter> collector = new TreeMap<>();
		int append = 2745;
		/*	we ned to collect all storages first and assign ids after every storage is known
		 *	also we need them to be sorted ... so a treemap. 'append' is explained later
	    */
		for (IMultiblockPart part : connectedParts) {
			if (part.getBlockState().getValue(BlockMultiCrafter.VARIANTS).equals("storage")) {
				pages++;
				powerUsage += 1;
				TileMultiCrafter tile = (TileMultiCrafter) part;
				/*	just colect the block instead of assinging ids now
			    *	blocks without id get numerated by id 2745 and up
	            *	because max size of a crafter is 16*16*16, internals are 14*14*14
	            *	so the max id any storage can have is 2744
	            *	this makes new added storages get appended last
	            *	if you want new storages added before existing ones, set 'append' to '-2745' instead
	            */
				//				tile.page = Optional.of(pages);
				//				invs.put(pages, tile.inv);
				if (tile.page.isPresent()) {
					collector.put(tile.page.get(), tile);
				} else {
					collector.put(append++, tile);
				}
			}
			if (part.getBlockState().getValue(BlockMultiCrafter.VARIANTS).equals("cpu")) {
				powerUsage += 2;
				speed++;
			}
		}
		/*	now every storage is known and placed in an ordered map we just
		 *	need to iterate over it and assing new ids in that order, starting by 1
		 *	map.values() should preserve the order
		 */
		int newid = 0;
		for (TileMultiCrafter tile : collector.values()) {
			newid++;
			tile.page = Optional.of(newid);
			invs.put(newid, tile.inv);
		}
	}

	public Inventory getInvForPage(int page) {
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
		actualPatterns.clear();
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

	boolean hasRebuiltRecently = false;

	public void tick() {
		hasRebuiltRecently = false;
	}

	public List<ICraftingPattern> actualPatterns = new ArrayList<>();
	public ICraftingPatternContainer node;

	public void rebuildPatterns() {
		if(hasRebuiltRecently){
			return;
		}
		hasRebuiltRecently = true;
		long start = System.currentTimeMillis();
		if (worldObj.isRemote) {
			return;
		}
		if (node == null) {
			node = getReferenceTile().getNewNode();
		}

		this.actualPatterns.clear();
		if (isAssembled()) {
			updateInfo();
			for (HashMap.Entry<Integer, Inventory> entry : invs.entrySet()) {
				for (int i = 0; i < entry.getValue().getSizeInventory(); ++i) {
					ItemStack patternStack = entry.getValue().getStackInSlot(i);
					if (!patternStack.isEmpty() && patternStack.getItem() instanceof ICraftingPatternProvider) {
						ICraftingPattern pattern = ((ICraftingPatternProvider) patternStack.getItem()).create(worldObj, patternStack, node);
						if (pattern.isValid()) {
							this.actualPatterns.add(pattern);
						}
					}
				}
			}
		}
		RebornCore.logHelper.debug("pattern rebuild took" + (System.currentTimeMillis() - start) + " ms");
	}

	private TileMultiCrafter getReferenceTile() {
		return (TileMultiCrafter) worldObj.getTileEntity(getReferenceCoord());
	}
}
