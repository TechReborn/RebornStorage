package RebornStorage.items;

import RebornStorage.client.CreativeTabRebornStorage;
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
