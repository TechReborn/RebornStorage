package net.gigabit101.rebornstorage.packet;

import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class PacketRequestMultiblockUpdate implements CustomPacketPayload
{
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "request_mutliblock_update");

    private final BlockPos blockPos;

    public PacketRequestMultiblockUpdate(BlockPos blockPos)
    {
        this.blockPos = blockPos;
    }


    public static PacketRequestMultiblockUpdate decode(FriendlyByteBuf buf)
    {
        return new PacketRequestMultiblockUpdate(buf.readBlockPos());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf)
    {
        buf.writeBlockPos(blockPos);
    }

    @Override
    public @NotNull ResourceLocation id()
    {
        return ID;
    }

    public static class Handler
    {
        public static void handle(final PacketRequestMultiblockUpdate message, PlayPayloadContext ctx)
        {
            ctx.workHandler().execute(() ->
            {
                ServerPlayer player = (ServerPlayer) ctx.player().get();
                if (player == null) return;

                BlockEntity blockEntity = player.level().getBlockEntity(message.blockPos);
                if(blockEntity != null && blockEntity instanceof BlockEntityMultiCrafter blockEntityMultiCrafter)
                {
                    boolean assembled = blockEntityMultiCrafter.getMultiBlock().isAssembled();
                    PacketHandler.sendTo(new PacketMultiblockUpdate(message.blockPos, assembled), player);
                }
            });
        }
    }
}
