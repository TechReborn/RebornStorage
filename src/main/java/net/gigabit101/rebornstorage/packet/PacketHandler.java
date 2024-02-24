package net.gigabit101.rebornstorage.packet;

import net.gigabit101.rebornstorage.Constants;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;


public class PacketHandler
{
    private static final String PROTOCOL_VERSION = Integer.toString(1);

    public static void init(IEventBus bus) {
        bus.addListener(PacketHandler::registerEvent);
    }

    public static void registerEvent(RegisterPayloadHandlerEvent event)
    {
        IPayloadRegistrar registrar = event.registrar(Constants.MOD_ID).versioned(PROTOCOL_VERSION);

        registrar.play(PacketGui.ID, PacketGui::decode, PacketGui.Handler::handle);
        registrar.play(PacketChangeMode.ID, PacketChangeMode::decode, PacketChangeMode.Handler::handle);
        registrar.play(PacketRequestMultiblockUpdate.ID, PacketRequestMultiblockUpdate::decode, PacketRequestMultiblockUpdate.Handler::handle);
        registrar.play(PacketMultiblockUpdate.ID, PacketMultiblockUpdate::decode, PacketMultiblockUpdate.Handler::handle);
    }

    public static void sendTo(CustomPacketPayload msg, ServerPlayer player)
    {
        if (!(player instanceof FakePlayer))
        {
            PacketDistributor.PLAYER.with(player).send(msg);
        }
    }

    public static void sendToServer(CustomPacketPayload msg)
    {
        PacketDistributor.SERVER.noArg().send(msg);
    }
}
