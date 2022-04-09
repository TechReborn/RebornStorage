package net.gigabit101.rebornstorage.blocks;

import com.refinedmods.refinedstorage.apiimpl.API;
import net.gigabit101.rebornstorage.tiles.TileMultiCrafter;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class BlockMultiCrafter extends BaseEntityBlock {
    public BlockMultiCrafter() {
        super(Properties.of(Material.METAL).strength(2.0F));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileMultiCrafter(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.getBlockEntity(blockPos) == null) return InteractionResult.FAIL;

        TileMultiCrafter tile = (TileMultiCrafter) level.getBlockEntity(blockPos);
        if (tile.getMultiblockController() != null) {
            if (!tile.getMultiblockController().isAssembled()) {
                if (tile.getMultiblockController().getLastValidationException() != null) {
                    if (level.isClientSide) {
                        player.sendMessage(new TextComponent(tile.getMultiblockController().getLastValidationException().getMessage()), Util.NIL_UUID);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else {
                if (!level.isClientSide) {
                    NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) level.getBlockEntity(blockPos), blockPos);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if (!level.isClientSide) {
            API.instance().getNetworkNodeManager((ServerLevel) level).getNode(blockPos);
        }
    }
}
