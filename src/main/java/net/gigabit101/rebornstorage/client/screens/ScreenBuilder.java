package net.gigabit101.rebornstorage.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.gigabit101.rebornstorage.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class ScreenBuilder
{
    public static final ResourceLocation GUI_SHEET = new ResourceLocation(Constants.MOD_ID.toLowerCase() + ":" + "textures/gui/gui_sheet.png");

    public void drawDefaultBackground(GuiGraphics guiGraphics, int x, int y, int width, int height, int textureXSize, int textureYSize)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        guiGraphics.blit(GUI_SHEET, x, y, 0, 0, width / 2, height / 2, textureXSize, textureYSize);
        guiGraphics.blit(GUI_SHEET, x + width / 2, y, 150 - width / 2, 0, width / 2, height / 2, textureXSize, textureYSize);
        guiGraphics.blit(GUI_SHEET, x, y + height / 2, 0, 150 - height / 2, width / 2, height / 2, textureXSize, textureYSize);
        guiGraphics.blit(GUI_SHEET, x + width / 2, y + height / 2, 150 - width / 2, 150 - height / 2, width / 2, height / 2, textureXSize, textureYSize);
    }

    public void drawPlayerSlots(GuiGraphics guiGraphics, int posX, int posY, boolean center, int textureXSize, int textureYSize)
    {
        if (center)
        {
            posX -= 81;
        }
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 9; x++)
            {
                guiGraphics.blit(GUI_SHEET, posX + x * 18, posY + y * 18, 150, 0, 18, 18, textureXSize, textureYSize);
            }
        }
        for (int x = 0; x < 9; x++)
        {
            guiGraphics.blit(GUI_SHEET, posX + x * 18, posY + 58, 150, 0, 18, 18, textureXSize, textureYSize);
        }
    }

    public void drawSlot(GuiGraphics guiGraphics, int posX, int posY, int textureXSize, int textureYSize)
    {
        guiGraphics.blit(GUI_SHEET, posX, posY, 150, 0, 18, 18, textureXSize, textureYSize);
    }


    public boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY)
    {
        return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
    }
}
