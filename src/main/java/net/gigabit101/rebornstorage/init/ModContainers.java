package net.gigabit101.rebornstorage.init;

import com.refinedmods.refinedstorage.container.factory.BlockEntityContainerFactory;
import net.gigabit101.rebornstorage.blockentities.BlockEntityAdvancedWirelessTransmitter;
import net.gigabit101.rebornstorage.containers.AdvancedWirelessTransmitterContainer;
import net.gigabit101.rebornstorage.containers.ContainerMultiCrafter;
import net.gigabit101.rebornstorage.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModContainers
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(BuiltInRegistries.MENU, Constants.MOD_ID);
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerMultiCrafter>>  MULTI_CRAFTER_CONTAINER = CONTAINERS.register("container_multiblock_crafter", () -> IMenuTypeExtension.create(ContainerMultiCrafter::new));
    public static final DeferredHolder<MenuType<?>, MenuType<AdvancedWirelessTransmitterContainer>>  ADVANCED_WIRELESS_CONTAINER = CONTAINERS.register("container_advanced_wireless_transmitter",
            () -> IMenuTypeExtension.create(new BlockEntityContainerFactory<AdvancedWirelessTransmitterContainer, BlockEntityAdvancedWirelessTransmitter>(((i, inventory, wirelessTransmitter)
                    -> new AdvancedWirelessTransmitterContainer(wirelessTransmitter, inventory.player, i)))));
}
