package net.gigabit101.rebornstorage.client.screens;

import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;
import net.gigabit101.rebornstorage.containers.AdvancedWirelessTransmitterContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedWirelessTransmitterScreen extends BaseScreen<AdvancedWirelessTransmitterContainer>
{
    AdvancedWirelessTransmitterContainer container;

    public AdvancedWirelessTransmitterScreen(AdvancedWirelessTransmitterContainer containerMenu, Inventory inventory, Component title)
    {
        super(containerMenu, 211, 137, inventory, title);
        this.container = containerMenu;
    }

    @Override
    public void onPostInit(int i, int i1)
    {
        addSideButton(new RedstoneModeSideButton(this, NetworkNodeBlockEntity.REDSTONE_MODE));
    }

    @Override
    public void tick(int i, int i1) {}

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int x, int y, int i2, int i3)
    {
        ResourceLocation TEXTURE = new ResourceLocation("refinedstorage", "textures/gui/wireless_transmitter.png");
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void renderForeground(GuiGraphics guiGraphics, int i, int i1)
    {
        renderString(guiGraphics, 7, 7, title.getString());
        renderString(guiGraphics, 28, 25, I18n.get("gui.refinedstorage.wireless_transmitter.distance", container.getBlockEntity() != null ? container.getBlockEntity().getNode().getRange() : 0));
        renderString(guiGraphics, 7, 43, I18n.get("container.inventory"));
    }
}
