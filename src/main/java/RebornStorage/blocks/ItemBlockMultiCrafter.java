package RebornStorage.blocks;

import RebornStorage.init.ModBlocks;
import net.minecraft.block.Block;
import reborncore.common.itemblock.ItemBlockBase;

/**
 * Created by Mark on 03/01/2017.
 */
public class ItemBlockMultiCrafter extends ItemBlockBase {
	public ItemBlockMultiCrafter(Block block) {
		super(ModBlocks.BLOCK_MULTI_CRAFTER, ModBlocks.BLOCK_MULTI_CRAFTER, BlockMultiCrafter.types);
	}
}
