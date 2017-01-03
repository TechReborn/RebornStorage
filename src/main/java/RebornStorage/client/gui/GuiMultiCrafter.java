package RebornStorage.client.gui;

import RebornStorage.multiblocks.MultiBlockCrafter;
import RebornStorage.packet.PacketGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import reborncore.client.guibuilder.GuiBuilder;
import reborncore.common.network.NetworkManager;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Mark on 03/01/2017.
 */
public class GuiMultiCrafter extends GuiContainer
{
	GuiBuilder builder = new GuiBuilder(GuiBuilder.defaultTextureSheet);
	MultiBlockCrafter crafter;

	int page;
	BlockPos pos;

	public GuiMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter, int page, BlockPos pos)
	{
		super(new ContainerMultiCrafter(player, crafter, page));
		this.xSize = 240;
		this.ySize = 240;
		this.crafter = crafter;
		this.page = page;
		this.pos = pos;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{

		builder.drawDefaultBackground(this, guiLeft, guiTop, xSize, ySize);
		builder.drawPlayerSlots(this, guiLeft + xSize / 2, guiTop + 140, true);
		int pos = 0;
		int row = 0;
		for (int i = 0; i < crafter.inv.getSlots(); i++) {
			builder.drawSlot(this, guiLeft + 10 + (pos * 18), guiTop + 10 + (row * 18));
			pos++;
			if(pos > 12){
				row ++;
				pos= 0;
			}
		}
		this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, "Page " + page, 50, 50, 4210752);
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(this.page-1, this.guiLeft+13, this.guiTop+172, 20, 20, "<"));
		this.buttonList.add(new GuiButton(this.page+1, this.guiLeft+209, this.guiTop+172, 20, 20, ">"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		NetworkManager.sendToServer(new PacketGui(button.id, pos));
	}
}
