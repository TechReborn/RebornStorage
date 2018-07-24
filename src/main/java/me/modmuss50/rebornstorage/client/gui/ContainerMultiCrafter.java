package me.modmuss50.rebornstorage.client.gui;

import com.raoulvdberge.refinedstorage.inventory.item.ItemHandlerBase;
import me.modmuss50.rebornstorage.client.SlotFiltered;
import me.modmuss50.rebornstorage.multiblocks.MultiBlockCrafter;
import net.minecraft.entity.player.EntityPlayer;
import reborncore.common.container.RebornContainer;

public class ContainerMultiCrafter extends RebornContainer {
	int page = 0;
	MultiBlockCrafter crafter;

	public ContainerMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter, int page) {
		this.page = page;
		this.crafter = crafter;
		if (crafter != null) {
			ItemHandlerBase handler = crafter.getInvForPage(page);
			if (handler != null) {
				drawSlotsForPage(page, handler);
			}

			drawPlayersInv(player, 45, 141);
			drawPlayersHotBar(player, 45, 199);
		}
	}

	public void drawSlotsForPage(int page, ItemHandlerBase handler) {
		int i = 0;
		for (int l = 0; l < 6; ++l) {
			for (int j1 = 0; j1 < 13; ++j1) {
				this.addSlotToContainer(new SlotFiltered(handler, i, 9 + j1 * 18, 21 + l * 18));
				i++;
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
