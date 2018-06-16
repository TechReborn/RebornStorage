package me.modmuss50.rebornstorage.client.gui;

import me.modmuss50.rebornstorage.multiblocks.MultiBlockCrafter;
import me.modmuss50.rebornstorage.packet.PacketGui;
import me.modmuss50.rebornstorage.packet.PacketSetPageName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
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
public class GuiMultiCrafter extends GuiContainer {
	GuiBuilder builder = new GuiBuilder(GuiBuilder.defaultTextureSheet);
	MultiBlockCrafter crafter;

	GuiTextField textField;

	int page = 0;
	BlockPos pos;
	public static int maxSlotsPerPage = 78;
	public static String pageName;

	public GuiMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter, int page, BlockPos pos) {
		super(new ContainerMultiCrafter(player, crafter, page));
		this.xSize = 250;
		this.ySize = 240;
		this.crafter = crafter;
		this.page = page;
		this.pos = pos;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		if (crafter.invs.size() != 0) {
			if (page > 1) {
				this.buttonList.add(new GuiButton(this.page - 2, this.guiLeft + 13, this.guiTop + 172, 20, 20, "<"));
			}
			if (crafter.invs.size() > page) {
				this.buttonList.add(new GuiButton(this.page, this.guiLeft + 209, this.guiTop + 172, 20, 20, ">"));
			}
		}


		textField = new GuiTextField(101, fontRenderer, this.guiLeft + 9, this.guiTop + 6, 200, 10);
		textField.setMaxStringLength(50);
		if(!pageName.isEmpty()){
			textField.setText(pageName);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		builder.drawDefaultBackground(this, guiLeft, guiTop, xSize, ySize);
		builder.drawPlayerSlots(this, guiLeft + xSize / 2, guiTop + 140, true);
		if (crafter.invs.size() != 0) {
			drawSlots(13, 6, maxSlotsPerPage);
		}
	}

	public void drawSlots(int col, int rows, int max) {
		int i = 0;
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < col; x++) {
				i++;
				if (i <= max)
					builder.drawSlot(this, guiLeft + 8 + x * 18, guiTop + 20 + y * 18);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		if (crafter.invs.size() == 0) {
			drawCenteredString(Minecraft.getMinecraft().fontRenderer, "Multiblock must contain at least 1 storage block", xSize / 2, 75, Color.RED.getRGB());
		} else {
			this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, "Page " + page + " of " + crafter.invs.size(), 125, 130, 4210752);
		}
	}

	@Override
	public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
		fontRendererIn.drawString(text, (float) (x - fontRendererIn.getStringWidth(text) / 2), (float) y, color, false);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		NetworkManager.sendToServer(new PacketGui(button.id, pos));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		textField.drawTextBox();
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		textField.updateCursorCounter();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		String text = textField.getText();
		textField.textboxKeyTyped(typedChar, keyCode);
		if(!textField.getText().equals(text)){
			NetworkManager.sendToServer(new PacketSetPageName(pos, page -1, textField.getText()));
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		textField.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
