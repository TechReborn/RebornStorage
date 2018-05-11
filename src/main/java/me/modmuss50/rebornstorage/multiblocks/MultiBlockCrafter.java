package me.modmuss50.rebornstorage.multiblocks;

import me.modmuss50.rebornstorage.blocks.BlockMultiCrafter;
import me.modmuss50.rebornstorage.tiles.CraftingNode.CachingItemHandler;
import me.modmuss50.rebornstorage.tiles.TileMultiCrafter;
import com.raoulvdberge.refinedstorage.inventory.ItemHandlerBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.rectangular.RectangularMultiblockControllerBase;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Created by Mark on 03/01/2017.
 */
public class MultiBlockCrafter extends RectangularMultiblockControllerBase {

	public Map<Integer, CachingItemHandler> invs = new TreeMap<>();

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

	public void updateInfo() {
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
				tile.getNode().rebuildPatterns();
				if (tile.page.isPresent()) {
					collector.put(tile.page.get(), tile);
				} else {
					collector.put(append++, tile);
				}
			}
			if (part.getBlockState().getValue(BlockMultiCrafter.VARIANTS).equals("cpu")) {
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
			invs.put(newid, tile.getNode().patterns);
		}
	}

	public CachingItemHandler getInvForPage(int page) {
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
		for (IMultiblockPart part : connectedParts) {
			TileMultiCrafter tile = (TileMultiCrafter) part;
			tile.getNode().rebuildPatterns();
		}
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
		return false;
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
}
