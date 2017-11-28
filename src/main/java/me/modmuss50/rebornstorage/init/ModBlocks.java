package me.modmuss50.rebornstorage.init;

import me.modmuss50.rebornstorage.blocks.BlockMultiCrafter;
import me.modmuss50.rebornstorage.blocks.ItemBlockMultiCrafter;
import me.modmuss50.rebornstorage.lib.ModInfo;
import me.modmuss50.rebornstorage.tiles.TileMultiCrafter;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.RebornRegistry;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ModBlocks {
	public static Block BLOCK_MULTI_CRAFTER;

	public static void init() {
		BLOCK_MULTI_CRAFTER = new BlockMultiCrafter();
		RebornRegistry.registerBlock(BLOCK_MULTI_CRAFTER, ItemBlockMultiCrafter.class, "multicrafter");
		GameRegistry.registerTileEntity(TileMultiCrafter.class, ModInfo.MOD_NAME + "TileMultiCrafter");
	}

}
