package transportterminal.gui.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import transportterminal.gui.server.ContainerCharger;
import transportterminal.tileentites.TileEntityCharger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCharger extends GuiContainer {

	private static final ResourceLocation GUI_CHARGER = new ResourceLocation("transportterminal:textures/gui/transportChargerGui.png");
	private final TileEntityCharger tile;

	public GuiCharger(InventoryPlayer playerInventory, TileEntityCharger tile) {
		super(new ContainerCharger(playerInventory, tile));
		this.tile = tile;
		ySize = 168;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(StatCollector.translateToLocal(tile.getInventoryName()), 8, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("RF: " + tile.getEnergyStored(ForgeDirection.UNKNOWN)), 100, ySize - 96 + 2, 4210752);
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