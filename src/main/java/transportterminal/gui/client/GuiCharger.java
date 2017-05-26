package transportterminal.gui.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
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
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
		fontRendererObj.drawString(I18n.format("RF: " + tile.getEnergyStored(null)), 100, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_CHARGER);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}