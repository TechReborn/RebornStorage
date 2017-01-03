package RebornStorage.blocks;

import RebornStorage.lib.ModInfo;
import RebornStorage.tiles.TileMultiCrafter;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.multiblock.BlockMultiblockBase;

import javax.annotation.Nullable;

/**
 * Created by Mark on 03/01/2017.
 */
public class BlockMultiCrafter extends BlockMultiblockBase{
	public BlockMultiCrafter() {
		super(Material.IRON);
		setCreativeTab(CreativeTabs.MISC);
		setUnlocalizedName(ModInfo.MOD_NAME + ".multicrafter");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileMultiCrafter();
	}

	@Override
	public boolean onBlockActivated(World worldIn,
	                                BlockPos pos,
	                                IBlockState state,
	                                EntityPlayer playerIn,
	                                EnumHand hand,
	                                @Nullable
		                                ItemStack heldItem,
	                                EnumFacing side,
	                                float hitX,
	                                float hitY,
	                                float hitZ) {
		TileMultiCrafter tile = (TileMultiCrafter) worldIn.getTileEntity(pos);
		if(tile.getMultiblockController() != null){
			tile.getMultiblockController().checkIfMachineIsWhole();
			System.out.println(tile.getMultiblockController().getLastValidationException());
		} else {
			System.out.println("null");
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}
}
