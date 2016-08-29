package transportterminal.tileentites;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityItemTransporterRenderer extends TileEntitySpecialRenderer<TileEntityItemTransporter> {

	@Override
	public void renderTileEntityAt(TileEntityItemTransporter tile, double x, double y, double z, float partialTick, int destroyStage) {
		if (!(tile.getEnergyStored(null) > 0))
			return;
		float renderRotation = tile.rotation + (tile.rotation - tile.prevRotation) * partialTick;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		GlStateManager.rotate(renderRotation * 2.0f, 0, 1, 0);
		double shineScale = 0.02f;
		GlStateManager.scale(shineScale, shineScale, shineScale);
		this.renderShine((float) Math.sin(Math.toRadians(renderRotation)) / 2.0f - 0.2f, 80);
		GlStateManager.popMatrix();
	}

	private void renderShine(float rotation, int iterations) {
		Random random = new Random(432L);
		GlStateManager.disableTexture2D();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.disableAlpha();
		GlStateManager.enableCull();
		GlStateManager.depthMask(false);
		GlStateManager.pushMatrix();
		float f1 = rotation;
		float f2 = 0.0f;
		if (f1 > 0.8F) {
			f2 = (f1 - 0.8F) / 0.2F;
		}
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();
		for (int i = 0; (float) i < iterations; ++i) {
			GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F + f1 * 90.0F, 0.0F, 0.0F, 1.0F);
			float pos1 = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
			float pos2 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
			buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(0.0D, 0.0D, 0.0D).color(0, 1, 255, (int) (255.0F * (1.0F - f2))).endVertex();
			buffer.pos(-0.866D * (double) pos2, (double) pos1, (double) (-0.5F * pos2)).color(1, 1, 255, 0).endVertex();
			buffer.pos(0.866D * (double) pos2, (double) pos1, (double) (-0.5F * pos2)).color(1, 1, 255, 0).endVertex();
			buffer.pos(0.0D, (double) pos1, (double) (1.0F * pos2)).color(1, 1, 255, 0).endVertex();
			buffer.pos(-0.866D * (double) pos2, (double) pos1, (double) (-0.5F * pos2)).color(1, 1, 255, 0).endVertex();
			tessellator.draw();
		}
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GL11.glShadeModel(GL11.GL_FLAT);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		RenderHelper.enableStandardItemLighting();
	}
}