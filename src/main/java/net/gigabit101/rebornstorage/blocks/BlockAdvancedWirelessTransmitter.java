package net.gigabit101.rebornstorage.blocks;

import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.ColoredNetworkBlock;
import net.gigabit101.rebornstorage.blockentities.BlockEntityAdvancedWirelessTransmitter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAdvancedWirelessTransmitter extends ColoredNetworkBlock
{
    private static final VoxelShape SHAPE_DOWN = box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    private static final VoxelShape SHAPE_UP = box(6.0D, 6.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape SHAPE_EAST = box(6.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
    private static final VoxelShape SHAPE_WEST = box(0.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    private static final VoxelShape SHAPE_NORTH = box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 10.0D);
    private static final VoxelShape SHAPE_SOUTH = box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 16.0D);

    public BlockAdvancedWirelessTransmitter()
    {
        super(Properties.of(Material.METAL).strength(2.0F));
    }

    @Override
    public BlockDirection getDirection()
    {
        return BlockDirection.ANY;
    }

    @Override
    public boolean hasConnectedState()
    {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context)
    {
        switch (state.getValue(getDirection().getProperty()))
        {
            case DOWN:
                return SHAPE_DOWN;
            case UP:
                return SHAPE_UP;
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            case EAST:
                return SHAPE_EAST;
            default:
                return Shapes.empty();
        }
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState)
    {
        return new BlockEntityAdvancedWirelessTransmitter(blockPos, blockState);
    }
}
