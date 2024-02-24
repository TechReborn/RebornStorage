package net.gigabit101.rebornstorage.packet;

import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class PacketGui implements CustomPacketPayload
{
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "gui");

    private final int page;
    private final BlockPos blockPos;

    public PacketGui(int page, BlockPos blockPos)
    {
        this.page = page;
        this.blockPos = blockPos;
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf)
    {
        buf.writeInt(page);
        buf.writeBlockPos(blockPos);
    }

    public static PacketGui decode(FriendlyByteBuf buf)
    {
        return new PacketGui(buf.readInt(), buf.readBlockPos());
    }

    @Override
    public @NotNull ResourceLocation id()
    {
        return ID;
    }

    public static class Handler
    {
        public static void handle(final PacketGui message, PlayPayloadContext ctx)
        {
            ctx.workHandler().execute(() ->
            {
                ServerPlayer player = (ServerPlayer) ctx.player().get();
                if (player == null) return;

                BlockEntity blockEntity = player.level().getBlockEntity(message.blockPos);
                if (blockEntity != null && blockEntity instanceof BlockEntityMultiCrafter blockEntityMultiCrafter && blockEntityMultiCrafter.getMultiBlock().isAssembled())
                {
                    if(message.page > 0)
                    {
                        blockEntityMultiCrafter.getMultiBlock().currentPage = message.page;
                        blockEntityMultiCrafter.setChanged();
                    }
                }
                openGUI(player.level(), player, message.blockPos);
            });
        }

        public static void openGUI(Level world, Player player, BlockPos blockPos)
        {
            player.openMenu((MenuProvider) world.getBlockEntity(blockPos), blockPos);
        }
    }
}
