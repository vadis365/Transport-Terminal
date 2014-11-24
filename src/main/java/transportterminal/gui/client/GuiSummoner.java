package transportterminal.gui.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.gui.server.ContainerSummoner;
import transportterminal.network.message.PlayerSummonMessage;
import transportterminal.tileentites.TileEntitySummoner;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSummoner extends GuiContainer {

	private static final ResourceLocation GUI_SUMMONER = new ResourceLocation("transportterminal:textures/gui/transportSummonerGui.png");
	private final TileEntitySummoner tile;

	public GuiSummoner(InventoryPlayer playerInventory, TileEntitySummoner tile) {
		super(new ContainerSummoner(playerInventory, tile));
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
		fontRendererObj.drawString(StatCollector.translateToLocal(tile.getInventoryName()), 8, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		if (TransportTerminal.IS_RF_PRESENT)
			fontRendererObj.drawString(StatCollector.translateToLocal("RF: " + tile.getEnergyStored(ForgeDirection.UNKNOWN)), 100, ySize - 96 + 2, 4210752);
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
		int xx = tile.xCoord;
		int yy = tile.yCoord;
		int zz = tile.zCoord;

		if (guibutton instanceof GuiButton)
			if (guibutton.id == 0) {
				if (tile.getStackInSlot(guibutton.id) != null && tile.getStackInSlot(guibutton.id).hasDisplayName())
					if (tile.canTeleport() && ConfigHandler.ALLOW_TELEPORT_SUMMON_PLAYER)
						TransportTerminal.networkWrapper.sendToServer(new PlayerSummonMessage(mc.thePlayer, tile.getStackInSlot(guibutton.id).getDisplayName(), xx, yy, zz));
				mc.thePlayer.closeScreen();
			}
	}
}