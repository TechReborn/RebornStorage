package RebornStorage.init;

import RebornStorage.blocks.BlockMultiCrafter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ModelHelper {
	public static void init() {
		int i;
		//        for (i = 0; i < ItemRebornStorageCell.types.length; ++i)
		//        {
		//            String[] name = ItemRebornStorageCell.types.clone();
		//            registerItemModel(ModItems.REBORN_STORAGE_CELL, i, name[i]);
		//        }
		//        for (i = 0; i < ItemRebornStorageCellFluid.types.length; ++i)
		//        {
		//            String[] name = ItemRebornStorageCellFluid.types.clone();
		//            registerItemModel(ModItems.REBORN_STORAGE_CELL_FLUID, i, name[i]);
		//        }
		//        for (i = 0; i < ItemStoragePart.types.length; ++i)
		//        {
		//            String[] name = ItemStoragePart.types.clone();
		//            registerItemModel(ModItems.REBORN_STORAGE_PART, i, name[i]);
		//        }
		for (i = 0; i < BlockMultiCrafter.types.length; ++i) {
			String[] name = BlockMultiCrafter.types.clone();
			registerItemModel(ModBlocks.BLOCK_MULTI_CRAFTER, i, name[i]);
		}
	}

	static void registerItemModel(Block b, int meta) {
		registerItemModel(Item.getItemFromBlock(b), meta);
	}

	static void registerItemModel(Block b, int meta, String variant) {
		registerItemModel(Item.getItemFromBlock(b), meta, variant);
	}

	static void registerItemModel(Item i, int meta) {
		ResourceLocation loc = i.getRegistryName();
		ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(loc, "inventory"));
	}

	static void registerItemModel(Item i, int meta, String variant) {
		ResourceLocation loc = i.getRegistryName();
		ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(loc, "type=" + variant));
	}
}
