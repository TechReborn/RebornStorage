package RebornStorage.client.gui;

import RebornStorage.multiblocks.MultiBlockCrafter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import reborncore.client.guibuilder.GuiBuilder;

/**
 * Created by Mark on 03/01/2017.
 */
public class GuiMultiCrafter extends GuiContainer
{
	GuiBuilder builder = new GuiBuilder(GuiBuilder.defaultTextureSheet);

	public GuiMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter)
	{
		super(new ContainerMultiCrafter(player, crafter));
		this.xSize = 250;
		this.ySize = 240;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		builder.drawDefaultBackground(this, guiLeft, guiTop, xSize, ySize);
		builder.drawPlayerSlots(this, guiLeft + xSize / 2, guiTop + 140, true);
		builder.drawSlot(this, guiLeft + 25, guiTop + 100);
	}
}
