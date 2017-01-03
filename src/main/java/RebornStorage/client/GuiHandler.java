package RebornStorage.client;

import RebornStorage.client.gui.ContainerMultiCrafter;
import RebornStorage.client.gui.GuiMultiCrafter;
import RebornStorage.multiblocks.MultiBlockCrafter;
import RebornStorage.tiles.TileMultiCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by Mark on 03/01/2017.
 */
public class GuiHandler implements IGuiHandler {

	public static final int MULTI_CRAFTER = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == MULTI_CRAFTER)
		{
			if(getMultiBlock(world, x, y, z) != null)
			{
				return new ContainerMultiCrafter(player, getMultiBlock(world, x, y, z));
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == MULTI_CRAFTER) {
			if(getMultiBlock(world, x, y, z) != null)
			{
				return new GuiMultiCrafter(player, getMultiBlock(world, x, y, z));
			}
		}
		return null;
	}


	public MultiBlockCrafter getMultiBlock(World world, int x, int y, int z){
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(tileEntity instanceof TileMultiCrafter){
			return (MultiBlockCrafter) ((TileMultiCrafter) tileEntity).getMultiblockController();
		}
		return null;
	}

}
