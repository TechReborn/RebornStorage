package net.gigabit101.rebornstorage.containers;

import net.gigabit101.rebornstorage.RebornStorage;
import net.gigabit101.rebornstorage.client.SlotFiltered;
import net.gigabit101.rebornstorage.init.ModContainers;
import net.gigabit101.rebornstorage.multiblocks.MultiBlockCrafter;
import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.IItemHandler;

import java.util.Objects;

public class ContainerMultiCrafter extends ContainerBase
{
    public MultiBlockCrafter crafter;
    public BlockPos blockPos;

    public ContainerMultiCrafter(int id, Inventory playerInv, FriendlyByteBuf extraData)
    {
        this(id, playerInv, (BlockEntityMultiCrafter) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(extraData.readBlockPos())));
    }

    public ContainerMultiCrafter(int id, Inventory playerInv, BlockEntityMultiCrafter multiBlockCrafter)
    {
        super(ModContainers.MULTI_CRAFTER_CONTAINER.get(), id);
        crafter = RebornStorage.getMultiBlock(multiBlockCrafter.getLevel(), multiBlockCrafter.getBlockPos());
        this.blockPos = multiBlockCrafter.getBlockPos();
        if (crafter != null)
        {
            if (crafter.currentPage > 0 && crafter.currentPage <= crafter.invs.size())
            {
                drawSlotsForPage(crafter.getInvForPage(crafter.currentPage));
            }
            else
            {
                RebornStorage.logger.warning("currentPage is out of bounds, Resetting to 1");
                crafter.currentPage = 1;
            }
        }
        drawPlayersInv(playerInv, 45, 141);
        drawPlayersHotBar(playerInv, 45, 199);
    }

    public void drawSlotsForPage(IItemHandler handler)
    {
        int i = 0;
        for (int l = 0; l < 6; ++l)
        {
            for (int j1 = 0; j1 < 13; ++j1)
            {
                this.addSlot(new SlotFiltered(handler, i, 9 + j1 * 18, 21 + l * 18));
                i++;
            }
        }
    }

    @Override
    public boolean stillValid(Player player)
    {
        return true;
    }
}
