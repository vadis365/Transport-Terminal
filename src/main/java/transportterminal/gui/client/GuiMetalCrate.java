package transportterminal.gui.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.gui.server.ContainerMetalCrate;
import transportterminal.tileentites.TileEntityMetalCrate;

@SideOnly(Side.CLIENT)
public class GuiMetalCrate extends GuiContainer {

	private static final ResourceLocation GUI_CRATE = new ResourceLocation("transportterminal:textures/gui/metal_crate_gui.png");
	private final TileEntityMetalCrate crate;

	public GuiMetalCrate(EntityPlayer player, TileEntityMetalCrate tile) {
		super(new ContainerMetalCrate(player, tile));
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