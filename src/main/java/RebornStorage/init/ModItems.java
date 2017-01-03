package RebornStorage.init;

import RebornStorage.items.ItemRebornStorageCell;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ModItems
{
    public static Item REBORN_STORAGE_CELL;

    public static void init()
    {
        REBORN_STORAGE_CELL = new ItemRebornStorageCell();
        GameRegistry.register(REBORN_STORAGE_CELL);
    }
}
