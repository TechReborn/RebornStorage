package me.modmuss50.rebornstorage.items;

import me.modmuss50.rebornstorage.client.CreativeTabRebornStorage;
import net.minecraft.item.Item;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ItemBase extends Item {
	public ItemBase() {
		setMaxStackSize(1);
		setCreativeTab(CreativeTabRebornStorage.INSTANCE);
	}
}
