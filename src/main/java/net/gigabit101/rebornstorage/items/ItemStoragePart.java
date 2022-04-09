package net.gigabit101.rebornstorage.items;

import net.gigabit101.rebornstorage.client.CreativeTabRebornStorage;
import net.minecraft.world.item.Item;

//TODO kill this
@Deprecated
public class ItemStoragePart extends Item {
    public ItemStoragePart() {
        super(new Item.Properties().tab(CreativeTabRebornStorage.INSTANCE));
    }
}
