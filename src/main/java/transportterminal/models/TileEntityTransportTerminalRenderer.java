package transportterminal.models;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.tileentites.TileEntityTransportTerminal;

@SideOnly(Side.CLIENT)
public class TileEntityTransportTerminalRenderer extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation("transportterminal:textures/special/tiles/transportTerminal.png");
	private static final ModelTransportTerminal model = new ModelTransportTerminal();

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTickTime, int breakStage) {
		TileEntityTransportTerminal terminal = (TileEntityTransportTerminal) tile;
		int meta = terminal.getBlockMetadata();

		if (breakStage >= 0) {
			bindTexture(DESTROY_STAGES[breakStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		} else
			bindTexture(texture);

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GlStateManager.scale(1F, -1F, -1F);
		switch (meta) {
			case 2:
				GlStateManager.rotate(180F, 0.0F, 1F, 0F);
				break;
			case 3:
				GlStateManager.rotate(0F, 0.0F, 1F, 0F);
				break;
			case 4:
				GlStateManager.rotate(90F, 0.0F, 1F, 0F);
				break;
			case 5:
				GlStateManager.rotate(-90F, 0.0F, 1F, 0F);
				break;
		}
		model.render();
		GlStateManager.popMatrix();
		if (breakStage >= 0) {
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}
}