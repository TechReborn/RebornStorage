package me.modmuss50.rebornstorage.packet;

import me.modmuss50.rebornstorage.RebornStorage;
import me.modmuss50.rebornstorage.client.GuiHandler;
import me.modmuss50.rebornstorage.tiles.TileMultiCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;
import reborncore.common.network.NetworkManager;

import java.io.IOException;

/**
 * Created by Mark on 03/01/2017.
 */
public class PacketGui implements INetworkPacket<PacketGui> {

	private int page;
	private BlockPos blockPos;

	public PacketGui(int page, BlockPos blockPos) {
		this.page = page;
		this.blockPos = blockPos;
	}

	public PacketGui() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		extendedPacketBuffer.writeInt(page);
		extendedPacketBuffer.writeBlockPos(blockPos);
	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		page = extendedPacketBuffer.readInt();
		blockPos = extendedPacketBuffer.readBlockPos();
	}

	@Override
	public void processData(PacketGui packetGui, MessageContext messageContext) {
		packetGui.openGUI(messageContext.getServerHandler().player);
	}

	public void openGUI(EntityPlayer player){
		TileEntity tileEntity = player.world.getTileEntity(blockPos);
		if(tileEntity instanceof TileMultiCrafter){
			TileMultiCrafter tileMultiCrafter = (TileMultiCrafter) tileEntity;
			tileMultiCrafter.updateLastPage(page);
			NetworkManager.sendToPlayer(new PacketSendPageName(tileMultiCrafter.getMultiBlock().pageNameMap.getOrDefault(page, "")), (EntityPlayerMP) player);
		}
		player.openGui(RebornStorage.INSTANCE, GuiHandler.MULTI_CRAFTER_BASEPAGE + page, player.world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
}
