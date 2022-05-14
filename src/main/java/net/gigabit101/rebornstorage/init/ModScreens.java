package net.gigabit101.rebornstorage.init;

import net.gigabit101.rebornstorage.client.screens.AdvancedWirelessTransmitterScreen;
import net.gigabit101.rebornstorage.client.screens.ScreenMultiCrafter;
import net.minecraft.client.gui.screens.MenuScreens;

public class ModScreens
{
    public static void init()
    {
        MenuScreens.register(ModContainers.MULTI_CRAFTER_CONTAINER.get(), ScreenMultiCrafter::new);
        MenuScreens.register(ModContainers.ADVANCED_WIRELESS_CONTAINER.get(), AdvancedWirelessTransmitterScreen::new);
    }
}
