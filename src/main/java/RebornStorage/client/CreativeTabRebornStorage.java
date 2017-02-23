package RebornStorage.client;

import RebornStorage.init.ModBlocks;
import RebornStorage.lib.ModInfo;
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
	public ItemStack getTabIconItem() {
		return new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER);
	}
}
