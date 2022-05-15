package net.gigabit101.rebornstorage.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.gigabit101.rebornstorage.containers.ContainerMultiCrafter;
import net.gigabit101.rebornstorage.multiblocks.MultiBlockCrafter;
import net.gigabit101.rebornstorage.packet.PacketGui;
import net.gigabit101.rebornstorage.packet.PacketHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
        buttonNext = new Button(this.leftPos + 209, this.topPos + 172, 20, 20, new TextComponent(">"), p_onPress_1_ ->
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
        });
        buttonBack = new Button(this.leftPos + 13, this.topPos + 172, 20, 20, new TextComponent("<"), p_onPress_1_ ->
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
        });
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
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
    {
        this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
        this.font.draw(poseStack, this.playerInventoryTitle, (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);

        this.font.draw(matrixStack, new TextComponent("Page " + crafter.currentPage + " of " + crafter.invs.size()), leftPos + 10, topPos + 224, 4210752);
        if(buttonNext.isMouseOver(mouseX, mouseY) || buttonBack.isMouseOver(mouseX, mouseY))
        {
            renderTooltip(matrixStack, new TextComponent("Hold <Left-Shift> to increment by 10"), mouseX, mouseY);
        }
    }

    @Override
    public void renderBg(PoseStack matrixStack, float partialTicks, int x, int y)
    {
        builder.drawDefaultBackground(this, matrixStack, leftPos, topPos, getXSize(), getYSize(), 256, 256);
        builder.drawPlayerSlots(this, matrixStack, leftPos + getXSize() / 2, topPos + 140, true, 256, 256);
        if (crafter != null)
        {
            if (crafter.invs.size() > 0 && crafter.currentPage <= crafter.invs.size())
            {
                drawSlots(13, 6, maxSlotsPerPage, matrixStack);
            }
        }
    }

    public void drawSlots(int col, int rows, int max, PoseStack matrixStack)
    {
        int i = 0;
        for (int y = 0; y < rows; y++)
        {
            for (int x = 0; x < col; x++)
            {
                i++;
                if (i <= max) builder.drawSlot(this, matrixStack, leftPos + 8 + x * 18, topPos + 20 + y * 18, 256, 256);
            }
        }
    }
}
