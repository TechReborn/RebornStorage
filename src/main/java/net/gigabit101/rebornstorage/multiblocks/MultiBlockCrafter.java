package net.gigabit101.rebornstorage.multiblocks;

import net.gigabit101.rebornstorage.RebornStorage;
import net.gigabit101.rebornstorage.RebornStorageConfig;
import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.gigabit101.rebornstorage.core.multiblock.IMultiblockPart;
import net.gigabit101.rebornstorage.core.multiblock.MultiblockControllerBase;
import net.gigabit101.rebornstorage.core.multiblock.rectangular.RectangularMultiblockControllerBase;
import net.gigabit101.rebornstorage.init.ModBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class MultiBlockCrafter extends RectangularMultiblockControllerBase
{
    public Map<Integer, ItemStackHandler> invs = new TreeMap<>();
    public int speed = 0;
    public int pages = 0;
    public Level level;
    public int currentPage = 1;

    public MultiBlockCrafter(Level world)
    {
        super(world);
        this.level = world;
    }

    @Override
    public void onAttachedPartWithMultiblockData(IMultiblockPart iMultiblockPart, CompoundTag nbtTagCompound)
    {
        readFromNBT(nbtTagCompound);
    }

    @Override
    public void onBlockAdded(IMultiblockPart iMultiblockPart)
    {
    }

    @Override
    public void onBlockRemoved(IMultiblockPart iMultiblockPart)
    {
    }

    @Override
    public void onMachineAssembled()
    {
        updateInfo("machineAssembled");
    }

    public void updateInfo(String reason)
    {
        RebornStorage.logger.info("Rebuilding Multiblock Crafter due to " + reason);
        speed = 0;
        pages = 0;
        TreeMap<Integer, BlockEntityMultiCrafter> collector = new TreeMap<>();
        int append = 2745;
        /*	we need to collect all storages first and assign ids after every storage is known
         *	also we need them to be sorted ... so a treemap. 'append' is explained later
         */
        for (IMultiblockPart part : connectedParts)
        {
            if (part.getBlockState().getBlock() == ModBlocks.BLOCK_MULTI_STORAGE.get())
            {
                pages++;
                BlockEntityMultiCrafter tile = (BlockEntityMultiCrafter) part;
                tile.getNode().rebuildPatterns(reason);
                if (tile.page.isPresent())
                {
                    collector.put(tile.page.get(), tile);
                } else
                {
                    collector.put(append++, tile);
                }
            }
            if (part.getBlockState().getBlock() == ModBlocks.BLOCK_MULTI_CPU.get())
            {
                speed++;
            }
        }

        int newid = 0;
        for (BlockEntityMultiCrafter tile : collector.values())
        {
            newid++;
            tile.page = Optional.of(newid);
            invs.put(newid, tile.getNode().patterns);
        }
    }

    public ItemStackHandler getInvForPage(int page)
    {
        return invs.get(page);
    }

    @Override
    protected void onMachineRestored()
    {
        updateInfo("machine rebuilt");
    }

    @Override
    protected void onMachinePaused()
    {
    }

    @Override
    protected void onMachineDisassembled()
    {
        for (IMultiblockPart part : connectedParts)
        {
            BlockEntityMultiCrafter tile = (BlockEntityMultiCrafter) part;
            tile.getNode().rebuildPatterns("machine disassembled");
            tile.getNode().invalidate();
        }
    }

    @Override
    protected int getMinimumNumberOfBlocksForAssembledMachine()
    {
        return (9 * 3);
    }

    @Override
    protected int getMaximumXSize()
    {
        return RebornStorageConfig.MULTIBLOCK_MAX_XSIZE.get();
    }

    @Override
    protected int getMaximumZSize()
    {
        return RebornStorageConfig.MULTIBLOCK_MAX_ZSIZE.get();
    }

    @Override
    protected int getMaximumYSize()
    {
        return RebornStorageConfig.MULTIBLOCK_MAX_YSIZE.get();
    }

    @Override
    protected int getMinimumXSize()
    {
        return RebornStorageConfig.MULTIBLOCK_MIN_XSIZE.get();
    }

    @Override
    protected int getMinimumYSize()
    {
        return RebornStorageConfig.MULTIBLOCK_MIN_YSIZE.get();
    }

    @Override
    protected int getMinimumZSize()
    {
        return RebornStorageConfig.MULTIBLOCK_MIN_ZSIZE.get();
    }

    @Override
    protected void onAssimilate(MultiblockControllerBase multiblockControllerBase)
    {
    }

    @Override
    protected void onAssimilated(MultiblockControllerBase multiblockControllerBase)
    {
    }

    @Override
    protected boolean updateServer()
    {
        return true;
    }

    @Override
    public void updateClient()
    {
    }

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
    public void formatDescriptionPacket(CompoundTag nbtTagCompound)
    {
        writeToNBT(nbtTagCompound);
    }

    @Override
    public void decodeDescriptionPacket(CompoundTag nbtTagCompound)
    {
        readFromNBT(nbtTagCompound);
    }
}
