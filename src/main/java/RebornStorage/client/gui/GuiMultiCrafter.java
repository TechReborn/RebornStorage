package RebornStorage.client.gui;

import RebornStorage.multiblocks.MultiBlockCrafter;
import RebornStorage.packet.PacketGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
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
    public static int maxSlotsPerPage = 78;

	public GuiMultiCrafter(EntityPlayer player, MultiBlockCrafter crafter, int page, BlockPos pos)
	{
		super(new ContainerMultiCrafter(player, crafter, page));
		if(page > crafter.pages){
			page = crafter.pages;
		}
		this.xSize = 250;
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
        drawSlots(13, 6, maxSlotsPerPage);
	}

    public void drawSlots(int col, int rows, int max)
    {
        int i = 0;
        for (int y = 0; y < rows; y++)
        {
            for (int x = 0; x < col; x++)
            {
                i++;
                if(i <= max)
                    builder.drawSlot(this, guiLeft + 8 + x * 18, guiTop + 20 + y * 18);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, "Page " + page, 125, 130, 181651);
    }

    @Override
	public void initGui() {
        super.initGui();
		this.buttonList.clear();
        if(page != 0)
            this.buttonList.add(new GuiButton(this.page-1, this.guiLeft+13, this.guiTop+172, 20, 20, "<"));
		this.buttonList.add(new GuiButton(this.page+1, this.guiLeft+209, this.guiTop+172, 20, 20, ">"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		NetworkManager.sendToServer(new PacketGui(button.id, pos));
	}
}
