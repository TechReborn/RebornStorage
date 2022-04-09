package net.gigabit101.rebornstorage.core.multiblock.rectangular;

import net.gigabit101.rebornstorage.core.multiblock.MultiblockControllerBase;
import net.gigabit101.rebornstorage.core.multiblock.MultiblockBlockEntityBase;
import net.gigabit101.rebornstorage.core.multiblock.MultiblockValidationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RectangularMultiblockTileEntityBase extends MultiblockBlockEntityBase {

    PartPosition position;
    Direction outwards;

    public RectangularMultiblockTileEntityBase(BlockEntityType<?> tileEntityTypeIn, BlockPos blockPos, BlockState blockState) {
        super(tileEntityTypeIn, blockPos, blockState);

        position = PartPosition.Unknown;
        outwards = null;
    }

    // Positional Data
    public Direction getOutwardsDir() {
        return outwards;
    }

    public PartPosition getPartPosition() {
        return position;
    }

    // Handlers from MultiblockTileEntityBase
    @Override
    public void onAttached(MultiblockControllerBase newController) {
        super.onAttached(newController);
        recalculateOutwardsDirection(newController.getMinimumCoord(), newController.getMaximumCoord());
    }

    @Override
    public void onMachineAssembled(MultiblockControllerBase controller) {
        BlockPos maxCoord = controller.getMaximumCoord();
        BlockPos minCoord = controller.getMinimumCoord();

        // Discover where I am on the reactor
        recalculateOutwardsDirection(minCoord, maxCoord);
    }

    @Override
    public void onMachineBroken() {
        position = PartPosition.Unknown;
        outwards = null;
    }

    // Positional helpers
    public void recalculateOutwardsDirection(BlockPos minCoord, BlockPos maxCoord) {
        outwards = null;
        position = PartPosition.Unknown;

        int facesMatching = 0;
        if (maxCoord.getX() == this.getBlockPos().getX() || minCoord.getX() == this.getBlockPos().getX()) {
            facesMatching++;
        }
        if (maxCoord.getY() == this.getBlockPos().getY() || minCoord.getY() == this.getBlockPos().getY()) {
            facesMatching++;
        }
        if (maxCoord.getZ() == this.getBlockPos().getZ() || minCoord.getZ() == this.getBlockPos().getZ()) {
            facesMatching++;
        }

        if (facesMatching <= 0) {
            position = PartPosition.Interior;
        } else if (facesMatching >= 3) {
            position = PartPosition.FrameCorner;
        } else if (facesMatching == 2) {
            position = PartPosition.Frame;
        } else {
            // 1 face matches
            if (maxCoord.getX() == this.getBlockPos().getX()) {
                position = PartPosition.EastFace;
                outwards = Direction.EAST;
            } else if (minCoord.getX() == this.getBlockPos().getX()) {
                position = PartPosition.WestFace;
                outwards = Direction.WEST;
            } else if (maxCoord.getZ() == this.getBlockPos().getZ()) {
                position = PartPosition.SouthFace;
                outwards = Direction.SOUTH;
            } else if (minCoord.getZ() == this.getBlockPos().getZ()) {
                position = PartPosition.NorthFace;
                outwards = Direction.NORTH;
            } else if (maxCoord.getY() == this.getBlockPos().getY()) {
                position = PartPosition.TopFace;
                outwards = Direction.UP;
            } else {
                position = PartPosition.BottomFace;
                outwards = Direction.DOWN;
            }
        }
    }

    // /// Validation Helpers (IMultiblockPart)
    public abstract void isGoodForFrame() throws MultiblockValidationException;

    public abstract void isGoodForSides() throws MultiblockValidationException;

    public abstract void isGoodForTop() throws MultiblockValidationException;

    public abstract void isGoodForBottom() throws MultiblockValidationException;

    public abstract void isGoodForInterior() throws MultiblockValidationException;
}
