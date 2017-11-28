package me.modmuss50.rebornstorage.blocks;

import me.modmuss50.rebornstorage.RebornStorage;
import me.modmuss50.rebornstorage.client.CreativeTabRebornStorage;
import me.modmuss50.rebornstorage.client.GuiHandler;
import me.modmuss50.rebornstorage.lib.ModInfo;
import me.modmuss50.rebornstorage.packet.PacketGui;
import me.modmuss50.rebornstorage.tiles.TileMultiCrafter;
import com.google.common.collect.Lists;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNode;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeManager;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.blocks.PropertyString;
import reborncore.common.multiblock.BlockMultiblockBase;
import reborncore.common.network.NetworkManager;
import reborncore.common.util.ArrayUtils;
import reborncore.common.util.ChatUtils;

import java.util.List;

/**
 * Created by Mark on 03/01/2017.
 */
public class BlockMultiCrafter extends BlockMultiblockBase {

	public static final String[] types = new String[] { "frame", "heat", "cpu", "storage" };
	private static final List<String> typesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(types));

	public static final PropertyString VARIANTS = new PropertyString("type", types);

	public BlockMultiCrafter() {
		super(Material.IRON);
		setCreativeTab(CreativeTabRebornStorage.INSTANCE);
		setUnlocalizedName(ModInfo.MOD_ID + ".multicrafter");
		this.setDefaultState(this.getStateFromMeta(0));
		setHardness(2F);
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
	                                EnumFacing side,
	                                float hitX,
	                                float hitY,
	                                float hitZ) {
		TileMultiCrafter tile = (TileMultiCrafter) worldIn.getTileEntity(pos);
		if (tile.getMultiblockController() != null) {
			if (!tile.getMultiblockController().isAssembled()) {
				if (tile.getMultiblockController().getLastValidationException() != null) {
					if (worldIn.isRemote) {
						ChatUtils.sendNoSpamMessages(42, new TextComponentString(tile.getMultiblockController().getLastValidationException().getMessage()));
					}
					return false;
				}
			} else if (!worldIn.isRemote) {
				//playerIn.openGui(RebornStorage.INSTANCE, GuiHandler.MULTI_CRAFTER_BASEPAGE + , worldIn, pos.getX(), pos.getY(), pos.getZ());
				NetworkManager.sendToServer(new PacketGui(tile.getValidLastPage(), pos));
				return true;
			}
			return true;
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if (!world.isRemote) {
			API.instance().discoverNode(world, pos);
		}

	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getBlockState().getBaseState().withProperty(VARIANTS, typesList.get(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return typesList.indexOf(state.getValue(VARIANTS));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANTS);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		for (int meta = 0; meta < types.length; meta++) {
			list.add(new ItemStack(this, 1, meta));
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, getMetaFromState(state));
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileMultiCrafter) {
			TileMultiCrafter tile = (TileMultiCrafter) tileentity;
			if (tile.getNode().patterns == null) {
				super.breakBlock(worldIn, pos, state);
				return;
			}
			for (int i = 0; i < tile.getNode().patterns.getSlots(); i++) {
				ItemStack stack = tile.getNode().patterns.getStackInSlot(i);
				if (!stack.isEmpty()) {
					InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack.copy());
				}
			}
		}

		INetworkNodeManager manager = API.instance().getNetworkNodeManager(worldIn);
		INetworkNode node = manager.getNode(pos);
		manager.removeNode(pos);
		manager.markForSaving();
		if (node.getNetwork() != null) {
			node.getNetwork().getNodeGraph().rebuild();
		}

		super.breakBlock(worldIn, pos, state);
	}

}
