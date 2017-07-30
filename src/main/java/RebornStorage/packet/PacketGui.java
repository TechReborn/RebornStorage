package RebornStorage.packet;

import RebornStorage.RebornStorage;
import RebornStorage.client.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

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
		EntityPlayer player = messageContext.getServerHandler().player;
		player.openGui(RebornStorage.INSTANCE, GuiHandler.MULTI_CRAFTER_BASEPAGE + packetGui.page, player.world, packetGui.blockPos.getX(), packetGui.blockPos.getY(), packetGui.blockPos.getZ());
	}
}
