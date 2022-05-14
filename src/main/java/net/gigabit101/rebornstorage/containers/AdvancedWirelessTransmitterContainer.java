package net.gigabit101.rebornstorage.containers;

import com.refinedmods.refinedstorage.container.BaseContainerMenu;
import net.gigabit101.rebornstorage.blockentities.BlockEntityAdvancedWirelessTransmitter;
import net.gigabit101.rebornstorage.init.ModContainers;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancedWirelessTransmitterContainer extends BaseContainerMenu
{
    public AdvancedWirelessTransmitterContainer(@Nullable BlockEntityAdvancedWirelessTransmitter wirelessTransmitter, Player player, int windowId)
    {
        super(ModContainers.ADVANCED_WIRELESS_CONTAINER.get(), wirelessTransmitter, player, windowId);
        for (int i = 0; i < 4; ++i)
        {
            addSlot(new SlotItemHandler(wirelessTransmitter.getNode().getUpgrades(), i, 187, 6 + (i * 18)));
        }

        addPlayerInventory(8, 55);

        transferManager.addBiTransfer(player.getInventory(), wirelessTransmitter.getNode().getUpgrades());
    }

    @Override
    public boolean stillValid(@NotNull Player player)
    {
        return true;
    }
}
