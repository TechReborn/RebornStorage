package net.gigabit101.rebornstorage.packet;

import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class PacketGui
{
    private final int page;
    private final BlockPos blockPos;

    public PacketGui(int page, BlockPos blockPos)
    {
        this.page = page;
        this.blockPos = blockPos;
    }

    public static void encode(PacketGui packetGui, FriendlyByteBuf buf)
    {
        buf.writeInt(packetGui.page);
        buf.writeBlockPos(packetGui.blockPos);
    }

    public static PacketGui decode(FriendlyByteBuf buf)
    {
        return new PacketGui(buf.readInt(), buf.readBlockPos());
    }

    public static class Handler
    {
        public static void handle(final PacketGui message, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() ->
            {
                ServerPlayer player = ctx.get().getSender();
                if (player == null) return;

                BlockEntity blockEntity = player.getLevel().getBlockEntity(message.blockPos);
                if (blockEntity != null && blockEntity instanceof BlockEntityMultiCrafter blockEntityMultiCrafter && blockEntityMultiCrafter.getMultiBlock().isAssembled())
                {
                    if(message.page > 0)
                    {
                        blockEntityMultiCrafter.getMultiBlock().currentPage = message.page;
                        blockEntityMultiCrafter.setChanged();
                    }
                }
                openGUI(player.getLevel(), player, message.blockPos);
            });
            ctx.get().setPacketHandled(true);
        }

        public static void openGUI(Level world, Player player, BlockPos blockPos)
        {
            NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) world.getBlockEntity(blockPos), blockPos);
        }
    }
}
