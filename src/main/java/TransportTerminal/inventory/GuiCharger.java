package TransportTerminal.inventory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import TransportTerminal.TransportTerminal;
import TransportTerminal.tileentites.TileEntityCharger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCharger extends GuiContainer {

	private static final ResourceLocation GUI_CHARGER = new ResourceLocation("transportterminal:textures/gui/transportChargerGui.png");
	private final TileEntityCharger tile;

	public GuiCharger(InventoryPlayer playerInventory, TileEntityCharger tile) {
		super(new ContainerCharger(playerInventory, tile));
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
		buttonList.add(new GuiButton(0, xOffSet + 84, yOffSet + 11, 31, 12, "+"));
		buttonList.add(new GuiButton(1, xOffSet + 84, yOffSet + 33, 31, 12, "+"));
		buttonList.add(new GuiButton(2, xOffSet + 84, yOffSet + 55, 31, 12, "+"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(StatCollector.translateToLocal(tile.getInventoryName()), 8, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		if (TransportTerminal.IS_RF_PRESENT)
			fontRendererObj.drawString(StatCollector.translateToLocal("RF: " + tile.getEnergyStored(ForgeDirection.UNKNOWN)), 100, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime,int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_CHARGER);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int x = tile.xCoord;
		int y = tile.yCoord;
		int z = tile.zCoord;

		if (guibutton instanceof GuiButton) {
			
			if (guibutton.id == 0) {
				//Do something
				}
			
			if (guibutton.id == 1) {
				//Do something
			}
			
			if (guibutton.id == 2) {
				//Do something
			}
		}
	}

}
