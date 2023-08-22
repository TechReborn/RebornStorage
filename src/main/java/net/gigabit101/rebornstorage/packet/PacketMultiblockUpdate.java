package net.gigabit101.rebornstorage.packet;

import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketMultiblockUpdate
{
    private final BlockPos blockPos;
    private final boolean assembled;

    public PacketMultiblockUpdate(BlockPos blockPos, boolean assembled)
    {
        this.blockPos = blockPos;
        this.assembled = assembled;
    }

    public static void encode(PacketMultiblockUpdate packetGui, FriendlyByteBuf buf)
    {
        buf.writeBlockPos(packetGui.blockPos);
        buf.writeBoolean(packetGui.assembled);
    }

    public static PacketMultiblockUpdate decode(FriendlyByteBuf buf)
    {
        return new PacketMultiblockUpdate(buf.readBlockPos(), buf.readBoolean());
    }

    public static class Handler
    {
        public static void handle(final PacketMultiblockUpdate message, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() ->
            {
                ServerPlayer player = ctx.get().getSender();
                if (player == null) return;

                BlockEntity blockEntity = player.level().getBlockEntity(message.blockPos);
                if(blockEntity != null && blockEntity instanceof BlockEntityMultiCrafter blockEntityMultiCrafter)
                {
                    blockEntityMultiCrafter.getMultiBlock().setAssembled(message.assembled);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
