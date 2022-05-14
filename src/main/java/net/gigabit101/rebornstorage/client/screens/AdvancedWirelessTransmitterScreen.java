package net.gigabit101.rebornstorage.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;
import net.gigabit101.rebornstorage.blockentities.BlockEntityAdvancedWirelessTransmitter;
import net.gigabit101.rebornstorage.containers.AdvancedWirelessTransmitterContainer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedWirelessTransmitterScreen extends BaseScreen<AdvancedWirelessTransmitterContainer>
{
    public AdvancedWirelessTransmitterScreen(AdvancedWirelessTransmitterContainer containerMenu, Inventory inventory, Component title)
    {
        super(containerMenu, 211, 137, inventory, title);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        renderBackground(poseStack, leftPos, topPos, mouseX, mouseY);
    }

    @Override
    public void onPostInit(int i, int i1)
    {
        addSideButton(new RedstoneModeSideButton(this, NetworkNodeBlockEntity.REDSTONE_MODE));
    }

    @Override
    public void tick(int i, int i1) {}

    @Override
    public void renderBackground(PoseStack poseStack, int x, int y, int i2, int i3)
    {
        bindTexture(RS.ID, "gui/wireless_transmitter.png");
        blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void renderForeground(PoseStack poseStack, int i, int i1)
    {
        renderString(poseStack, 7, 7, title.getString());
        renderString(poseStack, 28, 25, I18n.get("gui.refinedstorage.wireless_transmitter.distance", BlockEntityAdvancedWirelessTransmitter.RANGE.getValue()));
        renderString(poseStack, 7, 43, I18n.get("container.inventory"));
    }
}
