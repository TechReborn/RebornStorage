package net.gigabit101.rebornstorage.blocks;

import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeManager;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.apiimpl.API;
import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.gigabit101.rebornstorage.packet.PacketGui;
import net.gigabit101.rebornstorage.packet.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BlockMultiCrafter extends BaseEntityBlock
{
    public BlockMultiCrafter()
    {
        super(Properties.of().strength(2.0F));
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return new BlockEntityMultiCrafter(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult)
    {
        if (level.getBlockEntity(blockPos) == null) return InteractionResult.FAIL;
        BlockEntityMultiCrafter tile = (BlockEntityMultiCrafter) level.getBlockEntity(blockPos);
        if(tile == null) return InteractionResult.FAIL;

        if (tile.getMultiblockController() != null)
        {
            if (!tile.getMultiblockController().isAssembled())
            {
                if (tile.getMultiblockController().getLastValidationException() != null)
                {
                    if (player.getItemInHand(hand).isEmpty())
                    {
                        if(level.isClientSide)
                        {
                            player.sendSystemMessage(Component.literal(tile.getMultiblockController().getLastValidationException().getMessage()));
                        }

                        return InteractionResult.SUCCESS;
                    }
                }
            } else
            {
                if(level.isClientSide)
                    PacketHandler.sendToServer(new PacketGui(0, blockPos));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.SUCCESS;
        } else
        {
            return super.use(blockState, level, blockPos, player, hand, blockHitResult);
        }
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, @NotNull ItemStack itemStack)
    {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if (!level.isClientSide)
        {
            API.instance().getNetworkNodeManager((ServerLevel) level).getNode(blockPos);
        }
    }

    @Override
    public void onRemove(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState2, boolean p_60519_)
    {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity != null && blockEntity instanceof BlockEntityMultiCrafter blockEntityMultiCrafter)
        {
            if (blockEntityMultiCrafter.getNode().patterns != null)
            {
                for (int i = 0; i < blockEntityMultiCrafter.getNode().patterns.getSlots(); i++)
                {
                    ItemStack stack = blockEntityMultiCrafter.getNode().patterns.getStackInSlot(i);
                    if (!stack.isEmpty())
                    {
                        Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);
                    }
                }
            }
            if (blockEntityMultiCrafter.getMultiBlock() != null)
            {
                blockEntityMultiCrafter.getMultiBlock().detachBlock(blockEntityMultiCrafter, false);
            }
        }

        INetworkNodeManager manager = API.instance().getNetworkNodeManager((ServerLevel) level);
        INetworkNode node = manager.getNode(blockPos);
        if (node != null && node.getNetwork() != null)
        {
            node.getNetwork().getCraftingManager().invalidate();
            node.getNetwork().markDirty();
            node.getNetwork().getNodeGraph().invalidate(Action.PERFORM, level, blockPos);
        }

        manager.removeNode(blockPos);
        manager.markForSaving();

        level.removeBlockEntity(blockPos);

        super.onRemove(blockState, level, blockPos, blockState2, p_60519_);
    }
}
