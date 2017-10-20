package transportterminal.gui.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.gui.server.ContainerQuantumCrate;
import transportterminal.tileentites.TileEntityQuantumCrate;

@SideOnly(Side.CLIENT)
public class GuiQuantumCrate extends GuiContainer {

	private static final ResourceLocation GUI_CRATE = new ResourceLocation("transportterminal:textures/gui/quantum_crate_gui.png");
	private final TileEntityQuantumCrate crate;

	public GuiQuantumCrate(EntityPlayer player, TileEntityQuantumCrate tile) {
		super(new ContainerQuantumCrate(player, tile));
		crate = tile;
		allowUserInput = false;
		xSize = 256;
		ySize = 256;
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRenderer.drawString(I18n.format("container.inventory"), 48, ySize - 96, 4210752);
		fontRenderer.drawString(I18n.format(crate.getName()), 12, 4, 4210752);
		fontRenderer.drawString(I18n.format("RF: " + crate.getEnergyStored(null)), 156, ySize - 96, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_CRATE);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}