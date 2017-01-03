package RebornStorage.client.gui;

import RebornStorage.multiblocks.MultiBlockCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import reborncore.common.container.RebornContainer;

public class ContainerMultiCrafter extends RebornContainer
{
	public ContainerMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter)
    {
		if(crafter != null)
        {
			this.addSlotToContainer(new Slot(crafter.inventory, 1, 50, 50));
		}
        drawPlayersInv(player, 45, 141);
        drawPlayersHotBar(player, 45, 199);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
