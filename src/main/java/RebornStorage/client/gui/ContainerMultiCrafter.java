package RebornStorage.client.gui;

import RebornStorage.multiblocks.MultiBlockCrafter;
import RebornStorage.multiblocks.MultiBlockInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import reborncore.common.container.RebornContainer;

public class ContainerMultiCrafter extends RebornContainer
{
	int page;
	MultiBlockCrafter crafter;

	public ContainerMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter, int page)
    {
        this.page = page;
        this.crafter = crafter;
        if(page > crafter.pages){
        	page = crafter.pages;
        }
        if (crafter != null)
        {
            MultiBlockInventory handler = crafter.createOrGetInv(page);
            if(handler != null)
            {
                drawSlotsForPage(page, handler);
            }

            drawPlayersInv(player, 45, 141);
            drawPlayersHotBar(player, 45, 199);
        }
    }

    public void drawSlotsForPage(int page, MultiBlockInventory handler)
    {

//        if(page == 0)
//        {
//            i = 0;
//            max = GuiMultiCrafter.maxSlotsPerPage;
//        }
//        else
//        {
//            i = (GuiMultiCrafter.maxSlotsPerPage * page);
//            max = i + Math.min(GuiMultiCrafter.maxSlotsPerPage, crafter.inv.getSizeInventory());
//        }
//
//        for (j = 0; j < 6; ++j)
//        {
//            for (k = 0; k < 13; ++k)
//            {
//                i++;
//                if (i < max)
//                    this.addSlotToContainer(new Slot(handler, i, 9 + k * 18, 21 + j * 18));
//            }
//        }
	    int i = 0;
	    for (int l = 0; l < 6; ++l)
	    {
		    for (int j1 = 0; j1 < 13; ++j1)
		    {
			    this.addSlotToContainer(new Slot(handler, i, 9 + j1 * 18, 21 + l * 18));
			    i++;
		    }
	    }
    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
