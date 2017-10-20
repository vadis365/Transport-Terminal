package transportterminal.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiNamingButton extends GuiButton {
	private static final ResourceLocation TEXTURES = new ResourceLocation("transportterminal:textures/gui/buttons.png");
	private int u;
	private int v;

	public GuiNamingButton(int id, int x, int y, int u, int v, String name) {
		super(id, x, y, 32, 16, name);
		this.u = u;
		this.v = v;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		FontRenderer fontrenderer = mc.fontRenderer;
		if (visible) {
			mc.getTextureManager().bindTexture(TEXTURES);
			GlStateManager.color(0.75F, 0.75F, 0.75F, 0.5F);
			boolean hover = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if(hover)
				GlStateManager.color(0.75F, 1, 0.75F, 1);	
			drawTexturedModalRect(x, y, u, v, width, height);
			int textColour = 14737632;
			if (packedFGColour != 0)
				textColour = packedFGColour;
			else if (!this.enabled)
				textColour = 10526880;
			else if (this.hovered)
				textColour = 16777120;
			drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, textColour);
		}
	}
}
