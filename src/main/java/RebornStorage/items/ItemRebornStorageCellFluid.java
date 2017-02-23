package RebornStorage.items;

import RebornStorage.lib.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ItemRebornStorageCellFluid extends ItemBase
{
    public static final String[] types = new String[]{"1024k", "4096k", "16384k", "32768k"};

    public ItemRebornStorageCellFluid()
    {
        setUnlocalizedName(ModInfo.MOD_ID + ".storagecellfluid");
        setHasSubtypes(true);
        setRegistryName("storagecellfluid");
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (int meta = 0; meta < types.length; meta++)
        {
            list.add(new ItemStack(item, 1, meta));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        int meta = itemStack.getItemDamage();
        if (meta < 0 || meta >= types.length)
        {
            meta = 0;
        }
        return super.getUnlocalizedName() + "." + types[meta];
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextFormatting.RED + "WIP BLAME WAY2MUCHNOISE");
    }
}
