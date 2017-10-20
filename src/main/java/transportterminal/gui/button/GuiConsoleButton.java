package transportterminal.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
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
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (visible) {
			mc.getTextureManager().bindTexture(TEXTURES);
			GlStateManager.color(0.75F, 0.75F, 0.75F, 0.5F);
			boolean hover = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if(hover)
				GlStateManager.color(0.75F, 1, 0.75F, 1);	
			drawTexturedModalRect(x, y, u, v, width, height);
		}
	}
}
