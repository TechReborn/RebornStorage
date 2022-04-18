package net.gigabit101.rebornstorage.blocks;

import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeManager;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.apiimpl.API;
import net.gigabit101.rebornstorage.RebornStorage;
import net.gigabit101.rebornstorage.RebornStorageEventHandler;
import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.gigabit101.rebornstorage.core.multiblock.MultiblockRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class BlockMultiCrafter extends BaseEntityBlock
{
    public BlockMultiCrafter()
    {
        super(Properties.of(Material.METAL).strength(2.0F));
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
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
        if (tile.getMultiblockController() != null)
        {
            if (!tile.getMultiblockController().isAssembled())
            {
                if (tile.getMultiblockController().getLastValidationException() != null)
                {
                    if (level.isClientSide && player.getItemInHand(hand).isEmpty())
                    {
                        player.sendMessage(new TextComponent(tile.getMultiblockController().getLastValidationException().getMessage()), Util.NIL_UUID);
                        return InteractionResult.SUCCESS;
                    }
                    return super.use(blockState, level, blockPos, player, hand, blockHitResult);
                }
            } else
            {
                if (!level.isClientSide)
                {
                    NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) level.getBlockEntity(blockPos), blockPos);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.SUCCESS;
        } else
        {
            return super.use(blockState, level, blockPos, player, hand, blockHitResult);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack)
    {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if (!level.isClientSide)
        {
            API.instance().getNetworkNodeManager((ServerLevel) level).getNode(blockPos);
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean p_60519_)
    {
        if (level.isClientSide()) return;

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
