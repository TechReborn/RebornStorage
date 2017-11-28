package me.modmuss50.rebornstorage.init;

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
	}

	static void registerMultiblockRecipes() {
		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 0),
			"IGI",
			"GCG",
			"IGI",
			'G', "ingotGold",
			'I', RSItems.QUARTZ_ENRICHED_IRON,
			'C', new ItemStack(RSItems.PROCESSOR, 1, 4));

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 1),
			"IGI",
			"GCG",
			"IGI",
			'G', new ItemStack(Blocks.IRON_BARS),
			'I', RSItems.QUARTZ_ENRICHED_IRON,
			'C', new ItemStack(RSItems.PROCESSOR, 1, 3));

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 2),
			"IQI",
			"GCG",
			"IQI",
			'I', RSItems.QUARTZ_ENRICHED_IRON,
			'G', Items.GLOWSTONE_DUST,
			'Q', Items.QUARTZ,
			'C', new ItemStack(RSItems.PROCESSOR, 1, 3));

		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER, 1, 3),
			"ITI",
			"NCN",
			"IGI",
			'T', new ItemStack(Blocks.CRAFTING_TABLE),
			'I', RSItems.QUARTZ_ENRICHED_IRON,
			'N', RSItems.CORE,
			'C', new ItemStack(RSItems.PROCESSOR, 1, 4),
			'G', new ItemStack(RSItems.STORAGE_PART, 1, 0));
	}
}
