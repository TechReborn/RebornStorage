package RebornStorage.client.gui;

import RebornStorage.multiblocks.MultiBlockCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import reborncore.common.container.RebornContainer;

public class ContainerMultiCrafter extends RebornContainer
{
	public ContainerMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter)
    {
		if(crafter != null)
        {
            ItemStackHandler handler = crafter.getInv();
            if(handler != null)
            {
                this.addSlotToContainer(new SlotItemHandler(handler, 0, 25, 100));
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
