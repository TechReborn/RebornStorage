package net.gigabit101.rebornstorage.multiblocks;

import net.gigabit101.rebornstorage.core.multiblock.IMultiblockPart;
import net.gigabit101.rebornstorage.core.multiblock.MultiblockControllerBase;
import net.gigabit101.rebornstorage.core.multiblock.rectangular.RectangularMultiblockControllerBase;
import net.gigabit101.rebornstorage.init.ModBlocks;
import net.gigabit101.rebornstorage.tiles.TileMultiCrafter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class MultiBlockCrafter extends RectangularMultiblockControllerBase {

    public Map<Integer, ItemStackHandler> invs = new TreeMap<>();
    public int speed = 0;
    public int pages = 0;
    public Level level;
    public int currentPage = 0;

    public MultiBlockCrafter(Level world) {
        super(world);
        this.level = world;
    }

    @Override
    public void onAttachedPartWithMultiblockData(IMultiblockPart iMultiblockPart, CompoundTag nbtTagCompound) {
        readFromNBT(nbtTagCompound);
    }

    @Override
    protected void onBlockAdded(IMultiblockPart iMultiblockPart) {}

    @Override
    protected void onBlockRemoved(IMultiblockPart iMultiblockPart) {}

    @Override
    protected void onMachineAssembled() {
        updateInfo("machineAssembled");
    }

    public void updateInfo(String reason) {
        speed = 0;
        pages = 0;
        TreeMap<Integer, TileMultiCrafter> collector = new TreeMap<>();
        int append = 2745;
        /*	we ned to collect all storages first and assign ids after every storage is known
         *	also we need them to be sorted ... so a treemap. 'append' is explained later
         */
        for (IMultiblockPart part : connectedParts) {
            if (part.getBlockState().getBlock() == ModBlocks.BLOCK_MULTI_STORAGE.get()) {
                pages++;
                TileMultiCrafter tile = (TileMultiCrafter) part;
				tile.getNode().rebuildPatterns(reason);
                if (tile.page.isPresent()) {
                    collector.put(tile.page.get(), tile);
                } else {
                    collector.put(append++, tile);
                }
            }
            if (part.getBlockState().getBlock() == ModBlocks.BLOCK_MULTI_CPU.get()) {
                speed++;
            }
        }

        int newid = 0;
        for (TileMultiCrafter tile : collector.values()) {
            newid++;
            tile.page = Optional.of(newid);
            invs.put(newid, tile.getNode().patterns);
        }
    }

    public ItemStackHandler getInvForPage(int page) {
        return invs.get(page);
    }

    @Override
    protected void onMachineRestored() {}

    @Override
    protected void onMachinePaused() {}

    @Override
    protected void onMachineDisassembled() {
		for (IMultiblockPart part : connectedParts) {
			TileMultiCrafter tile = (TileMultiCrafter) part;
			tile.getNode().rebuildPatterns("machine disassembled");
			tile.getNode().invalidate();
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
    protected void onAssimilate(MultiblockControllerBase multiblockControllerBase) {}

    @Override
    protected void onAssimilated(MultiblockControllerBase multiblockControllerBase) {}

    @Override
    protected boolean updateServer() {
        return true;
    }

    @Override
    protected void updateClient() {}

    @Override
    public void writeToNBT(CompoundTag nbtTagCompound)
    {
        nbtTagCompound.putInt("currentpage", currentPage);
    }

    @Override
    public void readFromNBT(CompoundTag nbtTagCompound)
    {
        currentPage = nbtTagCompound.getInt("currentpage");
    }

    @Override
    public void formatDescriptionPacket(CompoundTag nbtTagCompound) {
        writeToNBT(nbtTagCompound);
    }

    @Override
    public void decodeDescriptionPacket(CompoundTag nbtTagCompound) {
        readFromNBT(nbtTagCompound);
    }
}
