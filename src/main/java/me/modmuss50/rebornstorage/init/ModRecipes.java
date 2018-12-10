package me.modmuss50.rebornstorage.init;

import com.raoulvdberge.refinedstorage.RSBlocks;
import com.raoulvdberge.refinedstorage.RSItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.common.util.RebornCraftingHelper;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ModRecipes {
	public static void init() {
		registerStorageCellRecipes();
		registerMultiblockRecipes();
	}

	static void registerStorageCellRecipes() {
		//Parts
		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 0),
			"DID",
			"GRG",
			"DGD",
			'G', new ItemStack(RSItems.STORAGE_PART, 1, 3),
			'D', new ItemStack(RSItems.PROCESSOR, 1, 5),
			'I', new ItemStack(RSItems.QUARTZ_ENRICHED_IRON, 1),
			'R', new ItemStack(Items.REDSTONE, 1));

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 1),
			"DID",
			"GRG",
			"DGD",
			'G', new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 0),
			'D', new ItemStack(RSItems.PROCESSOR, 1, 5),
			'I', new ItemStack(RSItems.QUARTZ_ENRICHED_IRON, 1),
			'R', new ItemStack(Items.REDSTONE, 1));

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 2),
			"DID",
			"GRG",
			"DGD",
			'G', new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 1),
			'D', new ItemStack(RSItems.PROCESSOR, 1, 5),
			'I', new ItemStack(RSItems.QUARTZ_ENRICHED_IRON, 1),
			'R', new ItemStack(Items.REDSTONE, 1));

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 3),
			"DID",
			"GRG",
			"DGD",
			'G', new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 2),
			'D', new ItemStack(RSItems.PROCESSOR, 1, 5),
			'I', new ItemStack(RSItems.QUARTZ_ENRICHED_IRON, 1),
			'R', new ItemStack(Items.REDSTONE, 1));

		//Fluid Parts
		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 4),
			"DID",
			"GRG",
			"DGD",
			'G', new ItemStack(RSItems.FLUID_STORAGE_PART, 1, 3),
			'D', new ItemStack(RSItems.PROCESSOR, 1, 5),
			'I', new ItemStack(RSItems.QUARTZ_ENRICHED_IRON, 1),
			'R', new ItemStack(Items.BUCKET, 1));

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 5),
			"DID",
			"GRG",
			"DGD",
			'G', new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 4),
			'D', new ItemStack(RSItems.PROCESSOR, 1, 5),
			'I', new ItemStack(RSItems.QUARTZ_ENRICHED_IRON, 1),
			'R', new ItemStack(Items.BUCKET, 1));

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 6),
			"DID",
			"GRG",
			"DGD",
			'G', new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 5),
			'D', new ItemStack(RSItems.PROCESSOR, 1, 5),
			'I', new ItemStack(RSItems.QUARTZ_ENRICHED_IRON, 1),
			'R', new ItemStack(Items.BUCKET, 1));

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 7),
			"DID",
			"GRG",
			"DGD",
			'G', new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 6),
			'D', new ItemStack(RSItems.PROCESSOR, 1, 5),
			'I', new ItemStack(RSItems.QUARTZ_ENRICHED_IRON, 1),
			'R', new ItemStack(Items.BUCKET, 1));

		//Disks
		registerDiskRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 0), new ItemStack(ModItems.REBORN_STORAGE_CELL, 1, 0));
		registerDiskRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 1), new ItemStack(ModItems.REBORN_STORAGE_CELL, 1, 1));
		registerDiskRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 2), new ItemStack(ModItems.REBORN_STORAGE_CELL, 1, 2));
		registerDiskRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 3), new ItemStack(ModItems.REBORN_STORAGE_CELL, 1, 3));

		registerDiskRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 4), new ItemStack(ModItems.REBORN_STORAGE_CELL_FLUID, 1, 0));
		registerDiskRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 5), new ItemStack(ModItems.REBORN_STORAGE_CELL_FLUID, 1, 1));
		registerDiskRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 6), new ItemStack(ModItems.REBORN_STORAGE_CELL_FLUID, 1, 2));
		registerDiskRecipe(new ItemStack(ModItems.REBORN_STORAGE_PART, 1, 7), new ItemStack(ModItems.REBORN_STORAGE_CELL_FLUID, 1, 3));
	}

	static void registerDiskRecipe(ItemStack in, ItemStack out) {
		RebornCraftingHelper.addShapedOreRecipe(out,
			"GRG",
			"RCR",
			"III",
			'G', "blockGlass",
			'R', Items.REDSTONE,
			'I', new ItemStack(RSItems.QUARTZ_ENRICHED_IRON, 1),
			'C', in);
		RebornCraftingHelper.addShapelessRecipe(out, new ItemStack(RSItems.STORAGE_HOUSING, 1), in);
	}

	static void registerMultiblockRecipes() {
		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 6, 0),
			"MBM",
			"BSB",
			"MBM",
			'M', RSBlocks.MACHINE_CASING,
			'S', "itemSilicon",
			'B', new ItemStack(RSItems.PROCESSOR, 1, 3));

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 1),
			"FIF",
			"ISI",
			"FIF",
			'F', new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 0),
			'I', RSItems.QUARTZ_ENRICHED_IRON,
			'S', "itemSilicon");

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 2),
			"FAF",
			"SCS",
			"FAF",
			'F', new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 0),
			'A', new ItemStack(RSItems.PROCESSOR, 1, 5),
			'C', "workbench",
			'S', new ItemStack(RSItems.UPGRADE, 1, 2));

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 3),
			"FCF",
			"SAS",
			"FSF",
			'F', new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 0),
			'C', "chest",
			'S', new ItemStack(RSItems.STORAGE_PART, 1, 0),
			'A', new ItemStack(RSItems.PROCESSOR, 1, 5));
		
		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 4),
			"FCF",
			"AIA",
			"FDF",
			'F', new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 0),
			'C', new ItemStack(RSItems.CORE, 1, 0),
			'A', new ItemStack(RSItems.PROCESSOR, 1, 5),
			'I', RSBlocks.INTERFACE,
			'D', new ItemStack(RSItems.CORE, 1, 1));
	}
}
