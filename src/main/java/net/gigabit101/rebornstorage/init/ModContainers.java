package net.gigabit101.rebornstorage.init;

import com.refinedmods.refinedstorage.container.factory.BlockEntityContainerFactory;
import net.gigabit101.rebornstorage.blockentities.BlockEntityAdvancedWirelessTransmitter;
import net.gigabit101.rebornstorage.containers.AdvancedWirelessTransmitterContainer;
import net.gigabit101.rebornstorage.containers.ContainerMultiCrafter;
import net.gigabit101.rebornstorage.Constants;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MOD_ID);
    public static final RegistryObject<MenuType<ContainerMultiCrafter>> MULTI_CRAFTER_CONTAINER = CONTAINERS.register("container_multiblock_crafter", () -> IForgeMenuType.create(ContainerMultiCrafter::new));
    public static final RegistryObject<MenuType<AdvancedWirelessTransmitterContainer>> ADVANCED_WIRELESS_CONTAINER = CONTAINERS.register("container_advanced_wireless_transmitter",
            () -> IForgeMenuType.create(new BlockEntityContainerFactory<AdvancedWirelessTransmitterContainer, BlockEntityAdvancedWirelessTransmitter>(((i, inventory, wirelessTransmitter)
                    -> new AdvancedWirelessTransmitterContainer(wirelessTransmitter, inventory.player, i)))));
}
