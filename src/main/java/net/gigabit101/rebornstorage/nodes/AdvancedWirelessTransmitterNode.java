package net.gigabit101.rebornstorage.nodes;

import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.api.network.IWirelessTransmitter;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.inventory.item.UpgradeItemHandler;
import com.refinedmods.refinedstorage.inventory.listener.NetworkNodeInventoryListener;
import com.refinedmods.refinedstorage.item.UpgradeItem;
import com.refinedmods.refinedstorage.util.StackUtils;
import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.RebornStorageConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class AdvancedWirelessTransmitterNode extends NetworkNode implements IWirelessTransmitter
{
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "advanced_wireless_transmitter");
    private final UpgradeItemHandler upgrades = (UpgradeItemHandler) new UpgradeItemHandler(4, UpgradeItem.Type.RANGE).addListener(new NetworkNodeInventoryListener(this));

    public AdvancedWirelessTransmitterNode(Level level, BlockPos pos)
    {
        super(level, pos);
    }

    @Override
    public int getRange()
    {
        return RebornStorageConfig.ADVANCED_WIRELESS_TRANSMITTER_RANGE.get() + this.upgrades.getUpgradeCount(UpgradeItem.Type.RANGE) * 500;
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

    @Override
    public void read(CompoundTag tag)
    {
        super.read(tag);
        StackUtils.readItems(upgrades, 0, tag);
    }

    @Override
    public CompoundTag write(CompoundTag tag)
    {
        super.write(tag);
        StackUtils.writeItems(upgrades, 0, tag);
        return tag;
    }

    public UpgradeItemHandler getUpgrades()
    {
        return upgrades;
    }

    @Nullable
    @Override
    public IItemHandler getDrops()
    {
        return getUpgrades();
    }
}
