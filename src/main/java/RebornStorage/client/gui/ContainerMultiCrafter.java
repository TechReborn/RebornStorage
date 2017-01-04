package RebornStorage.client.gui;

import RebornStorage.multiblocks.MultiBlockCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import reborncore.common.container.RebornContainer;
import reborncore.common.util.Inventory;

public class ContainerMultiCrafter extends RebornContainer
{
	int page = 0;
	MultiBlockCrafter crafter;

	public ContainerMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter, int page)
    {
        this.page = page;
        this.crafter = crafter;
        if (crafter != null)
        {
	        Inventory handler = crafter.getInvForPage(page);
            if(handler != null)
            {
                drawSlotsForPage(page, handler);
            }

            drawPlayersInv(player, 45, 141);
            drawPlayersHotBar(player, 45, 199);
        }
    }

    public void drawSlotsForPage(int page, Inventory handler)
    {
	    int i = 0;
	    for (int l = 0; l < 6; ++l)
	    {
		    for (int j1 = 0; j1 < 13; ++j1)
		    {
			    this.addSlotToContainer(new Slot(handler, i, 9 + j1 * 18, 21 + l * 18));
			    i++;
		    }
	    }
	    System.out.println("count " + i);
    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
