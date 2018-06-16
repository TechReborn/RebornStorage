package me.modmuss50.rebornstorage.packet;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import reborncore.common.network.RegisterPacketEvent;

/**
 * Created by Mark on 03/01/2017.
 */
public class Packets {

	@SubscribeEvent
	public static void loadPackets(RegisterPacketEvent event) {
		event.registerPacket(PacketGui.class, Side.SERVER);
		event.registerPacket(PacketSetPageName.class, Side.SERVER);
		event.registerPacket(PacketSendPageName.class, Side.CLIENT);
	}
}
