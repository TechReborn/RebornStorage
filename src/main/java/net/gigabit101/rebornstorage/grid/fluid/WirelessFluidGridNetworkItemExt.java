package net.gigabit101.rebornstorage.grid.fluid;

import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.grid.factory.WirelessFluidGridGridFactory;
import com.refinedmods.refinedstorage.apiimpl.network.item.WirelessFluidGridNetworkItem;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.item.WirelessFluidGridItem;
import com.refinedmods.refinedstorage.util.LevelUtils;
import net.gigabit101.rebornstorage.items.ItemWirelessGrid;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;


public class WirelessFluidGridNetworkItemExt extends WirelessFluidGridNetworkItem
{
    private final INetworkItemManager handler;
    private final Player player;
    private final ItemStack stack;
    private final PlayerSlot slot;

    public WirelessFluidGridNetworkItemExt(INetworkItemManager handler, Player player, ItemStack stack, PlayerSlot slot)
    {
        super(handler, player, stack, slot);
        this.handler = handler;
        this.player = player;
        this.stack = stack;
        this.slot = slot;
    }

    public boolean onOpen(INetwork network) {
        IEnergyStorage energy = stack.getCapability(Capabilities.EnergyStorage.ITEM, null);
        if (RS.SERVER_CONFIG.getWirelessFluidGrid().getUseEnergy() && ((ItemWirelessGrid)this.stack.getItem()).getType() != ItemWirelessGrid.Type.CREATIVE && energy != null && energy.getEnergyStored() <= RS.SERVER_CONFIG.getWirelessFluidGrid().getOpenUsage()) {
            this.sendOutOfEnergyMessage();
            return false;
        } else if (!network.getSecurityManager().hasPermission(Permission.MODIFY, this.player)) {
            LevelUtils.sendNoPermissionMessage(this.player);
            return false;
        } else {
            API.instance().getGridManager().openGrid(WirelessFluidGridGridFactory.ID, (ServerPlayer)this.player, this.stack, this.slot);
            this.drainEnergy(RS.SERVER_CONFIG.getWirelessFluidGrid().getOpenUsage());
            return true;
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

    private void sendOutOfEnergyMessage() {
        this.player.displayClientMessage(Component.translatable("misc.refinedstorage.network_item.out_of_energy", new Object[]{Component.translatable(this.stack.getItem().getDescriptionId())}), false);
    }
}
