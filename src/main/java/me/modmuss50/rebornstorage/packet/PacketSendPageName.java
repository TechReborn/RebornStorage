package me.modmuss50.rebornstorage.packet;

import me.modmuss50.rebornstorage.client.gui.GuiMultiCrafter;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;


public class PacketSendPageName implements INetworkPacket<PacketSendPageName> {

	String pageName;

	public PacketSendPageName(String pageName) {
		this.pageName = pageName;
	}

	public PacketSendPageName() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) {
		buffer.writeInt(pageName.length());
		buffer.writeString(pageName);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) {
		pageName = buffer.readString(buffer.readInt());
	}

	@Override
	public void processData(PacketSendPageName message, MessageContext context) {
		GuiMultiCrafter.pageName = pageName;
	}
}
