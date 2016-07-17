package transportterminal.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiLargeButton extends GuiButton {
	private static final ResourceLocation TEXTURES = new ResourceLocation("transportterminal:textures/gui/buttons.png");
	private int u;
	private int v;

	public GuiLargeButton(int id, int x, int y, int u, int v, String name) {
		super(id, x, y, 54, 12, name);
		this.u = u;
		this.v = v;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		FontRenderer fontrenderer = mc.fontRendererObj;
		if (visible) {
			mc.getTextureManager().bindTexture(TEXTURES);
			GL11.glColor4f(0.75F, 0.75F, 0.75F, 0.5F);
			boolean hover = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			if(hover)
				GL11.glColor4f(0.75F, 1, 0.75F, 1);	
			drawTexturedModalRect(xPosition, yPosition, u, v, width, height);
			
			int textColour = 14737632;
			if (packedFGColour != 0)
				textColour = packedFGColour;
			else if (!this.enabled)
				textColour = 10526880;
			else if (this.hovered)
				textColour = 16777120;
			drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, textColour);
		}
	}
}
