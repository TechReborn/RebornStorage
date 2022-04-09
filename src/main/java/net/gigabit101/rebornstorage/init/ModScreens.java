package net.gigabit101.rebornstorage.init;

import net.gigabit101.rebornstorage.client.gui.GuiMultiCrafter;
import net.minecraft.client.gui.screens.MenuScreens;

public class ModScreens {
    public static void init() {
        MenuScreens.register(ModContainers.MULTI_CRAFTER_CONTAINER.get(), GuiMultiCrafter::new);
    }
}
