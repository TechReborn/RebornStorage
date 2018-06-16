package me.modmuss50.rebornstorage.init;

import me.modmuss50.rebornstorage.blocks.BlockMultiCrafter;
import me.modmuss50.rebornstorage.items.ItemRebornStorageCell;
import me.modmuss50.rebornstorage.items.ItemRebornStorageCellFluid;
import me.modmuss50.rebornstorage.items.ItemStoragePart;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ModelHelper {
	public static void init() {
		int i;
		for (i = 0; i < EnumItemStorage.values().length; ++i) {
			String[] name = getNames(EnumItemStorage.class);
			registerItemModel(ModItems.REBORN_STORAGE_CELL, i, name[i]);
		}
		for (i = 0; i < EnumFluidStorage.values().length; ++i) {
			String[] name = getNames(EnumFluidStorage.class);
			registerItemModel(ModItems.REBORN_STORAGE_CELL_FLUID, i, name[i]);
		}
		for (i = 0; i < ItemStoragePart.types.length; ++i) {
			String[] name = ItemStoragePart.types.clone();
			registerItemModel(ModItems.REBORN_STORAGE_PART, i, name[i]);
		}
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

	static String[] getNames(Class<? extends Enum<?>> e) {
		return Arrays.stream(e.getEnumConstants())
			.map(Enum::name)
			.map(s -> s.toLowerCase().replace("type_", ""))
			.toArray(String[]::new);
	}
}
