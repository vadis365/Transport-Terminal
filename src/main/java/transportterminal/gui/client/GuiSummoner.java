package transportterminal.gui.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import transportterminal.TransportTerminal;
import transportterminal.gui.server.ContainerSummoner;
import transportterminal.network.message.PlayerSummonMessage;
import transportterminal.tileentites.TileEntitySummoner;

@SideOnly(Side.CLIENT)
public class GuiSummoner extends GuiContainer {

	private static final ResourceLocation GUI_SUMMONER = new ResourceLocation("transportterminal:textures/gui/transportSummonerGui.png");
	private final TileEntitySummoner tile;

	public GuiSummoner(EntityPlayer player, TileEntitySummoner tile) {
		super(new ContainerSummoner(player, tile));
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
		buttonList.add(new GuiButton(0, xOffSet + 61, yOffSet + 48, 54, 12, "Teleport"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(I18n.format(tile.getInventoryName()), xSize / 2 - fontRendererObj.getStringWidth(I18n.format(tile.getInventoryName())) / 2, ySize - 136, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
		if (TransportTerminal.IS_RF_PRESENT)
			fontRendererObj.drawString(I18n.format("RF: " + tile.getEnergyStored(null)), 100, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_SUMMONER);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		BlockPos pos = tile.getPos();

		if (guibutton instanceof GuiButton)
			if (guibutton.id == 0) {
				TransportTerminal.NETWORK_WRAPPER.sendToServer(new PlayerSummonMessage(mc.thePlayer, guibutton.id, pos));
				mc.thePlayer.closeScreen();
			}
	}
}