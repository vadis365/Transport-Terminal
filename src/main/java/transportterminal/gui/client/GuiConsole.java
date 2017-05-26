package transportterminal.gui.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.gui.button.GuiConsoleButton;
import transportterminal.gui.server.ContainerTerminal;
import transportterminal.network.message.ButtonMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;

@SideOnly(Side.CLIENT)
public class GuiConsole extends GuiContainer {
	EntityPlayer player;
	private static final ResourceLocation GUI_TRANSPORTER = new ResourceLocation("transportterminal:textures/gui/transportTerminalGui.png");
	private final TileEntityTransportTerminal tile;

	public GuiConsole(EntityPlayer player, TileEntityTransportTerminal tile, int id) {
		super(new ContainerTerminal(player, tile, id));
		this.tile = tile;
		allowUserInput = false;
		ySize = 168;
		this.player = player;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		buttonList.clear();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		for (int rowTop = 2; rowTop <= 8; ++rowTop)
			buttonList.add(new GuiConsoleButton(rowTop, xOffSet + 44 + rowTop * 18 - 36, yOffSet + 18, 0, 0, ""));

		for (int rowBottom = 9; rowBottom <= 15; ++rowBottom)
			buttonList.add(new GuiConsoleButton(rowBottom, xOffSet + 44 + rowBottom * 18 - 162, yOffSet + 63, 0, 0, ""));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(I18n.format(tile.getName()), 8, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
		fontRendererObj.drawString(I18n.format("RF: " + tile.getEnergyStored(null)), 100, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_TRANSPORTER);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiButton)
			if (guibutton.id >= 2 && guibutton.id <= 15) {
				int newDim = player.dimension;
				TransportTerminal.NETWORK_WRAPPER.sendToServer(new ButtonMessage(mc.player, guibutton.id, tile.getPos(), newDim));
				mc.player.closeScreen();
			}
	}
}