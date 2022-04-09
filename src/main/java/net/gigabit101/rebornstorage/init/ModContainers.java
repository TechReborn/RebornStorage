package net.gigabit101.rebornstorage.init;

import net.gigabit101.rebornstorage.client.gui.ContainerMultiCrafter;
import net.gigabit101.rebornstorage.Constants;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MOD_ID);
    public static final RegistryObject<MenuType<ContainerMultiCrafter>> MULTI_CRAFTER_CONTAINER =
            CONTAINERS.register("container_multiblock_crafter", () -> IForgeMenuType.create(ContainerMultiCrafter::new));

}
