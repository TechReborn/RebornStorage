package net.gigabit101.rebornstorage.blockentities;

import com.refinedmods.refinedstorage.apiimpl.network.node.WirelessTransmitterNetworkNode;
import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;
import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.init.ModBlocks;
import net.gigabit101.rebornstorage.nodes.AdvancedWirelessTransmitterNode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvancedWirelessTransmitter extends NetworkNodeBlockEntity<AdvancedWirelessTransmitterNode>
{
    public static final BlockEntitySynchronizationParameter<Integer, BlockEntityAdvancedWirelessTransmitter> RANGE;
    public static BlockEntitySynchronizationSpec SPEC;

    public BlockEntityAdvancedWirelessTransmitter(BlockPos pos, BlockState state)
    {
        super(ModBlocks.ADVANCED_WIRELESS_TRANSMITTER.get(), pos, state, SPEC, AdvancedWirelessTransmitterNode.class);
    }

    @Override
    public AdvancedWirelessTransmitterNode createNode(Level level, BlockPos blockPos)
    {
        return new AdvancedWirelessTransmitterNode(level, blockPos);
    }

    static {
        RANGE = new BlockEntitySynchronizationParameter<>(new ResourceLocation("refinedstorage", "advanced_wireless_transmitter_range"), EntityDataSerializers.INT, 0, (t) -> {
            return ((AdvancedWirelessTransmitterNode) t.getNode()).getRange();
        });
        SPEC = BlockEntitySynchronizationSpec.builder().addWatchedParameter(REDSTONE_MODE).addWatchedParameter(RANGE).build();
    }
}
