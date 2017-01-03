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

	public ContainerMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter, int page)
    {
    	this.page = page;
		if(crafter != null)
        {
            MultiBlockInventory handler = crafter.getInv();
            if(handler != null)
            {


	            int pos = 0;
	            int row = 0;
	            for (int i = 0; i < crafter.inv.getSizeInventory(); i++) {
		            this.addSlotToContainer(new Slot(handler, i,  10 + (pos * 18), 10 + (row * 18)));
		            pos++;
		            if(pos > 10){
			            row ++;
			            pos= 0;
		            }
	            }

            }
		}
        drawPlayersInv(player, 45, 141);
        drawPlayersHotBar(player, 45, 199);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
