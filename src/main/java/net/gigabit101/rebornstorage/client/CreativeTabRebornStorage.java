package net.gigabit101.rebornstorage.client;

import net.gigabit101.rebornstorage.init.ModItems;
import net.gigabit101.rebornstorage.Constants;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabRebornStorage extends CreativeModeTab
{
    public static CreativeTabRebornStorage INSTANCE = new CreativeTabRebornStorage();

    public CreativeTabRebornStorage()
    {
        super(Constants.MOD_ID);
    }

    @Override
    public ItemStack makeIcon()
    {
        return new ItemStack(ModItems.STORAGE_DISK_16384K.get());
    }
}
