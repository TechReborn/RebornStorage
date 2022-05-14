package net.gigabit101.rebornstorage.blockentities;

import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import net.gigabit101.rebornstorage.init.ModBlocks;
import net.gigabit101.rebornstorage.nodes.AdvancedWirelessTransmitterNode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvancedWirelessTransmitter extends NetworkNodeBlockEntity<AdvancedWirelessTransmitterNode>
{
    public static final BlockEntitySynchronizationParameter<Integer, BlockEntityAdvancedWirelessTransmitter> RANGE = new BlockEntitySynchronizationParameter<>(EntityDataSerializers.INT, 0, (t) -> t.getNode().getRange());

    public BlockEntityAdvancedWirelessTransmitter(BlockPos pos, BlockState state)
    {
        super(ModBlocks.ADVANCED_WIRELESS_TRANSMITTER.get(), pos, state);
        this.dataManager.addWatchedParameter(RANGE);
    }

    @Override
    public AdvancedWirelessTransmitterNode createNode(Level level, BlockPos blockPos)
    {
        return new AdvancedWirelessTransmitterNode(level, blockPos);
    }
}
