package net.gigabit101.rebornstorage.client.screens;

import net.gigabit101.rebornstorage.containers.ContainerMultiCrafter;
import net.gigabit101.rebornstorage.multiblocks.MultiBlockCrafter;
import net.gigabit101.rebornstorage.packet.PacketGui;
import net.gigabit101.rebornstorage.packet.PacketHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenMultiCrafter extends AbstractContainerScreen<ContainerMultiCrafter>
{
    ScreenBuilder builder = new ScreenBuilder();
    MultiBlockCrafter crafter;
    ContainerMultiCrafter containerMultiCrafter;
    public static int maxSlotsPerPage = 78;
    Button buttonNext;
    Button buttonBack;

    public ScreenMultiCrafter(ContainerMultiCrafter containerMultiCrafter, Inventory playerInventory, Component title)
    {
        super(containerMultiCrafter, playerInventory, title);
        this.imageWidth = 250;
        this.imageHeight = 240;
        this.inventoryLabelY = 130;
        this.containerMultiCrafter = containerMultiCrafter;
        this.crafter = containerMultiCrafter.crafter;
    }

    @Override
    public void init()
    {
        super.init();
        buttonNext = Button.builder(Component.literal(">"), p_93751_ ->
        {
            boolean shift = Screen.hasShiftDown();
            int next = crafter.currentPage + 1;
            if(shift)
            {
                int test = crafter.currentPage + 10;
                if(crafter.invs.size() >= test)
                {
                    next = test;
                }
            }
            crafter.currentPage = next;
            if(containerMultiCrafter != null && containerMultiCrafter.blockPos != null)
                PacketHandler.sendToServer(new PacketGui(next, containerMultiCrafter.blockPos));
        }).pos(this.leftPos + 209, this.topPos + 172).size(20, 20).build();

        buttonBack = Button.builder(Component.literal("<"), p_93751_ ->
        {
            boolean shift = Screen.hasShiftDown();
            int next = crafter.currentPage - 1;
            if(shift)
            {
                int test = crafter.currentPage - 10;
                if(test > 0)
                {
                    next = test;
                }
            }
            crafter.currentPage = next;
            if(containerMultiCrafter != null && containerMultiCrafter.blockPos != null)
                PacketHandler.sendToServer(new PacketGui(next, containerMultiCrafter.blockPos));
        }).pos(this.leftPos + 13, this.topPos + 172).size(20, 20).build();

        addRenderableWidget(buttonBack);
        addRenderableWidget(buttonNext);

        if (crafter != null && crafter.invs != null)
        {
            if (crafter.invs.size() != 0)
            {
                if (crafter.invs.size() == crafter.currentPage)
                {
                    buttonNext.active = false;
                }
                if (crafter.currentPage == 1)
                {
                    buttonBack.active = false;
                }
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        guiGraphics.drawString(font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        guiGraphics.drawString(font, Component.literal("Page " + crafter.currentPage + " of " + crafter.invs.size()), leftPos + 10, topPos + 224, 4210752, false);
        if(buttonNext.isMouseOver(mouseX, mouseY) || buttonBack.isMouseOver(mouseX, mouseY))
        {
            guiGraphics.renderTooltip(font, Component.literal("Hold <Left-Shift> to increment by 10"), mouseX, mouseY);
        }
    }

    @Override
    public void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y)
    {
        builder.drawDefaultBackground(guiGraphics, leftPos, topPos, getXSize(), getYSize(), 256, 256);
        builder.drawPlayerSlots(guiGraphics, leftPos + getXSize() / 2, topPos + 140, true, 256, 256);
        if (crafter != null)
        {
            if (crafter.invs.size() > 0 && crafter.currentPage <= crafter.invs.size())
            {
                drawSlots(13, 6, maxSlotsPerPage, guiGraphics);
            }
        }
    }

    public void drawSlots(int col, int rows, int max, GuiGraphics guiGraphics)
    {
        int i = 0;
        for (int y = 0; y < rows; y++)
        {
            for (int x = 0; x < col; x++)
            {
                i++;
                if (i <= max) builder.drawSlot(guiGraphics, leftPos + 8 + x * 18, topPos + 20 + y * 18, 256, 256);
            }
        }
    }
}
