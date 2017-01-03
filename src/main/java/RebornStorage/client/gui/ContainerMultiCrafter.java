package RebornStorage.client.gui;

import RebornStorage.multiblocks.MultiBlockCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import reborncore.common.container.RebornContainer;

public class ContainerMultiCrafter extends RebornContainer
{
	int page;

	public ContainerMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter, int page)
    {
        this.page = page;
        if (crafter != null)
        {
            ItemStackHandler handler = crafter.getInv();
            if (handler != null)
            {
                drawSlotsForPage(page, handler);
            }

            drawPlayersInv(player, 45, 141);
            drawPlayersHotBar(player, 45, 199);
        }
    }

    public void drawSlotsForPage(int page, ItemStackHandler handler)
    {
        int j;
        int k;
        int i;
        int max;
        if(page == 0)
        {
            i = 0;
            max = GuiMultiCrafter.maxSlotsPerPage;
        }
        else
        {
            i = (GuiMultiCrafter.maxSlotsPerPage * page);
            max = i + GuiMultiCrafter.maxSlotsPerPage;
        }

        for (j = 0; j < 6; ++j)
        {
            for (k = 0; k < 13; ++k)
            {
                i++;
                if (i <= max)
                    this.addSlotToContainer(new SlotItemHandler(handler, i - 1, 9 + k * 18, 21 + j * 18));
            }
        }
    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
