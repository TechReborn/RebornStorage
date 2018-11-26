package me.modmuss50.rebornstorage.client;

import me.modmuss50.rebornstorage.init.ModBlocks;
import me.modmuss50.rebornstorage.lib.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class CreativeTabRebornStorage extends CreativeTabs {
	public static CreativeTabRebornStorage INSTANCE = new CreativeTabRebornStorage();

	public CreativeTabRebornStorage() {
		super(ModInfo.MOD_ID);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER);
	}

}
