package me.modmuss50.rebornstorage.items;

import me.modmuss50.rebornstorage.lib.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ItemStoragePart extends ItemBase {
	public static final String[] types = new String[] { "256k", "1024k", "4096k", "16384k", "16384k_fluid", "32768k_fluid", "131m_fluid", "524m_fluid" };

	public ItemStoragePart() {
		setTranslationKey(ModInfo.MOD_ID + ".storagepart");
		setHasSubtypes(true);
		setRegistryName("storagepart");
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			for (int meta = 0; meta < types.length; meta++) {
				list.add(new ItemStack(this, 1, meta));
			}
		}
	}

	@Override
	public String getTranslationKey(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length) {
			meta = 0;
		}
		return super.getTranslationKey() + "." + types[meta];
	}
}
