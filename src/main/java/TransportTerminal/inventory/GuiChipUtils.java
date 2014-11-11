package TransportTerminal.inventory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import TransportTerminal.tileentites.TileEntityChipUtilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiChipUtils extends GuiContainer {

	private static final ResourceLocation GUI_CHIP_UTILS = new ResourceLocation("transportterminal:textures/gui/transportChipUtils.png");
	private final TileEntityChipUtilities tile;

	public GuiChipUtils(InventoryPlayer playerInventory, TileEntityChipUtilities tile) {
		super(new ContainerChipUtils(playerInventory, tile));
		this.tile = tile;
		allowUserInput = false;
		ySize = 168;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		buttonList.clear();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		buttonList.add(new GuiButton(0, xOffSet + 97, yOffSet + 31, 18, 10, "+"));
		buttonList.add(new GuiButton(1, xOffSet + 97 , yOffSet + 52, 18, 10, "-"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(StatCollector.translateToLocal(tile.getInventoryName()), 8, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_CHIP_UTILS);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiButton) {
			if (guibutton.id == 0) {
				// copychip
				System.out.println("Chip Copying Here");
			}
			
			if (guibutton.id == 1) {
				// erasechip
				System.out.println("Chip Erasing Here");
			}
		}
		//mc.thePlayer.closeScreen();
	}
	}
