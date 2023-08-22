package net.gigabit101.rebornstorage.packet;

import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRequestMultiblockUpdate
{
    private final BlockPos blockPos;

    public PacketRequestMultiblockUpdate(BlockPos blockPos)
    {
        this.blockPos = blockPos;
    }

    public static void encode(PacketRequestMultiblockUpdate packetGui, FriendlyByteBuf buf)
    {
        buf.writeBlockPos(packetGui.blockPos);
    }

    public static PacketRequestMultiblockUpdate decode(FriendlyByteBuf buf)
    {
        return new PacketRequestMultiblockUpdate(buf.readBlockPos());
    }

    public static class Handler
    {
        public static void handle(final PacketRequestMultiblockUpdate message, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() ->
            {
                ServerPlayer player = ctx.get().getSender();
                if (player == null) return;

                BlockEntity blockEntity = player.level().getBlockEntity(message.blockPos);
                if(blockEntity != null && blockEntity instanceof BlockEntityMultiCrafter blockEntityMultiCrafter)
                {
                    boolean assembled = blockEntityMultiCrafter.getMultiBlock().isAssembled();
                    PacketHandler.sendTo(new PacketMultiblockUpdate(message.blockPos, assembled), player);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
