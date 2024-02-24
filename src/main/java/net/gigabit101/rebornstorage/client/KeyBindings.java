package net.gigabit101.rebornstorage.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final KeyMapping OPEN_WIRELESS_CRAFTING_GRID = new KeyMapping(
        "key.rebornstorage.openWirelessCraftingGrid",
        KeyConflictContext.IN_GAME,
        KeyModifier.CONTROL,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_G,
        "Reborn Storage"
    );
    public static final KeyMapping MODE_SWITCH_WIRELESS_CRAFTING_GRID = new KeyMapping(
            "key.rebornstorage.switchModeWirelessCraftingGrid",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "Reborn Storage"
    );
}
