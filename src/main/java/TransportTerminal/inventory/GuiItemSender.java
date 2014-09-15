package TransportTerminal.inventory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import TransportTerminal.TransportTerminal;
import TransportTerminal.network.TeleportMessageItems;
import TransportTerminal.tileentites.TileEntityTransportItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiItemSender extends GuiContainer {

	private static final ResourceLocation GUI_ITEMS = new ResourceLocation("transportterminal:textures/gui/transportTerminalItemsGui.png");
	private final TileEntityTransportItems transportInventory;

	public GuiItemSender(InventoryPlayer playerInventory, TileEntityTransportItems tile) {
		super(new ContainerItemSender(playerInventory, tile));
		transportInventory = tile;
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
		buttonList.add(new GuiButton(0, xOffSet + 80, yOffSet + 63, 16, 7, ""));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_ITEMS);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiButton)
			if (guibutton.id == 0)
				if (transportInventory.getStackInSlot(0) != null && transportInventory.getStackInSlot(1) != null && transportInventory.getStackInSlot(0).stackTagCompound.hasKey("chipX")) {
					int newDim = transportInventory.getStackInSlot(0).getTagCompound().getInteger("chipDim");
					int x = transportInventory.getStackInSlot(0).getTagCompound().getInteger("chipX");
					int y = transportInventory.getStackInSlot(0).getTagCompound().getInteger("chipY");
					int z = transportInventory.getStackInSlot(0).getTagCompound().getInteger("chipZ");
					int tileX = transportInventory.xCoord;
					int tileY = transportInventory.yCoord;
					int tileZ = transportInventory.zCoord;
					TransportTerminal.networkWrapper.sendToServer(new TeleportMessageItems(mc.thePlayer, x, y, z, newDim, tileX, tileY, tileZ));
				}
		}
}