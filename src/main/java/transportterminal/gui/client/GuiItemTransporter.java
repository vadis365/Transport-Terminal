package transportterminal.gui.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.gui.button.GuiConsoleButton;
import transportterminal.gui.server.ContainerItemTransporter;
import transportterminal.network.message.ItemTransporterMessage;
import transportterminal.tileentites.TileEntityItemTransporter;

@SideOnly(Side.CLIENT)
public class GuiItemTransporter extends GuiContainer {

	private static final ResourceLocation GUI_ITEMS = new ResourceLocation("transportterminal:textures/gui/transportTerminalItemsGui.png");
	private final TileEntityItemTransporter tile;

	public GuiItemTransporter(EntityPlayer player, TileEntityItemTransporter tile) {
		super(new ContainerItemTransporter(player, tile));
		this.tile = tile;
		allowUserInput = false;
		ySize = 168;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		buttonList.clear();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		buttonList.add(new GuiConsoleButton(0, xOffSet + 80, yOffSet + 64, 0, 0, ""));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
		fontRendererObj.drawString(I18n.format("RF: " + tile.getEnergyStored(null)), 100, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_ITEMS);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		BlockPos pos = tile.getPos();
		if (guibutton instanceof GuiButton)
			if (guibutton.id == 0)
				TransportTerminal.NETWORK_WRAPPER.sendToServer(new ItemTransporterMessage(mc.player, guibutton.id, pos));
	}
}
