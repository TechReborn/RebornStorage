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

public class PacketMultiblockUpdate implements CustomPacketPayload
{
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "mutliblock_update");

    private final BlockPos blockPos;
    private final boolean assembled;

    public PacketMultiblockUpdate(BlockPos blockPos, boolean assembled)
    {
        this.blockPos = blockPos;
        this.assembled = assembled;
    }

    public static PacketMultiblockUpdate decode(FriendlyByteBuf buf)
    {
        return new PacketMultiblockUpdate(buf.readBlockPos(), buf.readBoolean());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf)
    {
        buf.writeBlockPos(blockPos);
        buf.writeBoolean(assembled);
    }

    @Override
    public @NotNull ResourceLocation id()
    {
        return ID;
    }

    public static class Handler
    {
        public static void handle(final PacketMultiblockUpdate message, PlayPayloadContext ctx)
        {
            ctx.workHandler().execute(() ->
            {
                ServerPlayer player = (ServerPlayer) ctx.player().get();
                if (player == null) return;

                BlockEntity blockEntity = player.level().getBlockEntity(message.blockPos);
                if(blockEntity != null && blockEntity instanceof BlockEntityMultiCrafter blockEntityMultiCrafter)
                {
                    blockEntityMultiCrafter.getMultiBlock().setAssembled(message.assembled);
                }
            });
        }
    }
}
