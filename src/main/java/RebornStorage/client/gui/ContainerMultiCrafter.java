package RebornStorage.client.gui;

import RebornStorage.multiblocks.MultiBlockCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import reborncore.common.container.RebornContainer;


public class ContainerMultiCrafter extends RebornContainer {

	public ContainerMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter) {
		this.addPlayersHotbar(player);
		this.addPlayersInventory(player);
		if(crafter != null){
			this.addSlotToContainer(new Slot(crafter.inventory, 0, 0, 0));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
