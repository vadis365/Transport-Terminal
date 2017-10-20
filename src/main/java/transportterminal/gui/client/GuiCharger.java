package transportterminal.gui.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.gui.server.ContainerCharger;
import transportterminal.tileentites.TileEntityCharger;

@SideOnly(Side.CLIENT)
public class GuiCharger extends GuiContainer {

	private static final ResourceLocation GUI_CHARGER = new ResourceLocation("transportterminal:textures/gui/transport_charger_gui.png");
	private final TileEntityCharger tile;

	public GuiCharger(EntityPlayer player, TileEntityCharger tile) {
		super(new ContainerCharger(player, tile));
		this.tile = tile;
		ySize = 168;
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
		fontRenderer.drawString(I18n.format("RF: " + tile.getEnergyStored(null)), 100, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_CHARGER);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}