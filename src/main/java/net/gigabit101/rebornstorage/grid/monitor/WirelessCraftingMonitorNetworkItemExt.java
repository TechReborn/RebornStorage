package net.gigabit101.rebornstorage.grid.monitor;

import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.RSContainerMenus;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.grid.factory.WirelessFluidGridGridFactory;
import com.refinedmods.refinedstorage.apiimpl.network.item.WirelessCraftingMonitorNetworkItem;
import com.refinedmods.refinedstorage.blockentity.craftingmonitor.CraftingMonitorBlockEntity;
import com.refinedmods.refinedstorage.blockentity.craftingmonitor.WirelessCraftingMonitor;
import com.refinedmods.refinedstorage.container.factory.CraftingMonitorMenuProvider;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.item.WirelessFluidGridItem;
import com.refinedmods.refinedstorage.util.LevelUtils;
import net.gigabit101.rebornstorage.items.ItemWirelessGrid;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.Objects;

public class WirelessCraftingMonitorNetworkItemExt extends WirelessCraftingMonitorNetworkItem
{
    private final INetworkItemManager handler;
    private final Player player;
    private final ItemStack stack;
    private final PlayerSlot slot;

    public WirelessCraftingMonitorNetworkItemExt(INetworkItemManager handler, Player player, ItemStack stack, PlayerSlot slot)
    {
        super(handler, player, stack, slot);
        this.handler = handler;
        this.player = player;
        this.stack = stack;
        this.slot = slot;
    }

    @Override
    public boolean onOpen(INetwork network)
    {
        IEnergyStorage energy = (IEnergyStorage)this.stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (RS.SERVER_CONFIG.getWirelessCraftingMonitor().getUseEnergy() && ((ItemWirelessGrid)this.stack.getItem()).getType() != ItemWirelessGrid.Type.CREATIVE && energy != null && energy.getEnergyStored() <= RS.SERVER_CONFIG.getWirelessCraftingMonitor().getOpenUsage()) {
            this.sendOutOfEnergyMessage();
            return false;
        } else if (network.getSecurityManager().hasPermission(Permission.MODIFY, this.player) && network.getSecurityManager().hasPermission(Permission.AUTOCRAFTING, this.player)) {
            WirelessCraftingMonitor wirelessCraftingMonitor = new WirelessCraftingMonitor(this.stack, this.player.getServer(), this.slot);
            Player var10000 = this.player;
            CraftingMonitorMenuProvider var10001 = new CraftingMonitorMenuProvider((MenuType)RSContainerMenus.WIRELESS_CRAFTING_MONITOR.get(), wirelessCraftingMonitor, (CraftingMonitorBlockEntity)null);
            PlayerSlot var10002 = this.slot;
            Objects.requireNonNull(var10002);
            var10000.openMenu(var10001, var10002::writePlayerSlot);
            this.drainEnergy(RS.SERVER_CONFIG.getWirelessCraftingMonitor().getOpenUsage());
            return true;
        } else {
            LevelUtils.sendNoPermissionMessage(this.player);
            return false;
        }
    }

    @Override
    public void drainEnergy(int energy)
    {
        IEnergyStorage energyStorage = this.stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage != null) {
            energyStorage.extractEnergy(energy, false);
            if (energyStorage.getEnergyStored() <= 0) {
                this.handler.close(this.player);
                this.player.closeContainer();
                this.sendOutOfEnergyMessage();
            }
        }
    }

    public void sendOutOfEnergyMessage()
    {
        this.player.displayClientMessage(Component.translatable("misc.refinedstorage.network_item.out_of_energy", new Object[]{Component.translatable(this.stack.getItem().getDescriptionId())}), false);
    }
}
