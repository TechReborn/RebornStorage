package RebornStorage.client;

import RebornStorage.init.ModItems;
import RebornStorage.lib.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class CreativeTabRebornStorage extends CreativeTabs
{
    public static CreativeTabRebornStorage INSTANCE = new CreativeTabRebornStorage();

    public CreativeTabRebornStorage()
    {
        super(ModInfo.MOD_ID);
    }

    @Override
    public Item getTabIconItem()
    {
        return ModItems.REBORN_STORAGE_CELL;
    }
}
