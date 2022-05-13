package net.gigabit101.rebornstorage.nodes;

import com.refinedmods.refinedstorage.api.network.IWirelessTransmitter;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.RebornStorageConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class AdvancedWirelessTransmitterNode extends NetworkNode implements IWirelessTransmitter
{
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "advanced_wireless_transmitter");

    public AdvancedWirelessTransmitterNode(Level level, BlockPos pos)
    {
        super(level, pos);
    }

    @Override
    public int getRange()
    {
        return RebornStorageConfig.ADVANCED_WIRELESS_TRANSMITTER_RANGE.get();
    }

    @Override
    public BlockPos getOrigin()
    {
        return this.pos;
    }

    @Override
    public ResourceKey<Level> getDimension()
    {
        return this.level.dimension();
    }

    @Override
    public int getEnergyUsage()
    {
        return RebornStorageConfig.ADVANCED_WIRELESS_TRANSMITTER_POWER_COST.get();
    }

    @Override
    public boolean canConduct(Direction direction)
    {
        return this.getDirection() == direction;
    }

    @Override
    public void visit(Operator operator)
    {
        operator.apply(this.level, this.pos.relative(Direction.DOWN), Direction.UP);
    }

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }
}