package transportterminal.models;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import transportterminal.tileentites.TileEntityTransportTerminal;

@SideOnly(Side.CLIENT)
public class TileEntityTransportTerminalRenderer extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation("transportterminal:textures/special/tiles/transportTerminal.png");
	private static final ModelTransportTerminal model = new ModelTransportTerminal();

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTickTime, int something) {
		TileEntityTransportTerminal terminal = (TileEntityTransportTerminal) tile;
		int meta = terminal.getBlockMetadata();
		bindTexture(texture);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		switch (meta) {
			case 2:
				GL11.glRotatef(180F, 0.0F, 1F, 0F);
				break;
			case 3:
				GL11.glRotatef(0F, 0.0F, 1F, 0F);
				break;
			case 4:
				GL11.glRotatef(90F, 0.0F, 1F, 0F);
				break;
			case 5:
				GL11.glRotatef(-90F, 0.0F, 1F, 0F);
				break;
		}
		model.render();
		GL11.glPopMatrix();
	}

}