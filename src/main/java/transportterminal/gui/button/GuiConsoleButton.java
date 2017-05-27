package transportterminal.gui.button;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiConsoleButton extends GuiButton {
	private static final ResourceLocation TEXTURES = new ResourceLocation("transportterminal:textures/gui/buttons.png");

	private int u;
	private int v;

	public GuiConsoleButton(int id, int x, int y, int u, int v, String name) {
		super(id, x, y, 16, 7, name);
		this.u = u;
		this.v = v;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (visible) {
			mc.getTextureManager().bindTexture(TEXTURES);
			GL11.glColor4f(0.75F, 0.75F, 0.75F, 0.5F);
			boolean hover = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			if(hover)
				GL11.glColor4f(0.75F, 1, 0.75F, 1);	
			drawTexturedModalRect(xPosition, yPosition, u, v, width, height);
		}
	}
}
