package TransportTerminal.inventory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import TransportTerminal.TransportTerminal;
import TransportTerminal.network.EnergyMessage;
import TransportTerminal.network.TeleportMessage;
import TransportTerminal.tileentites.TileEntityTransportTerminal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTerminal extends GuiContainer {

	private static final ResourceLocation GUI_TRANSPORTER = new ResourceLocation("transportterminal:textures/gui/transportTerminalGui.png");
	private final TileEntityTransportTerminal transportInventory;

	public GuiTerminal(InventoryPlayer playerInventory, TileEntityTransportTerminal tile, int id) {
		super(new ContainerTerminal(playerInventory, tile, id));
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
		for (int rowTop = 2; rowTop <= 8; ++rowTop)
			buttonList.add(new GuiButton(rowTop, xOffSet + 44 + rowTop * 18 - 36, yOffSet + 18, 16, 7, ""));
		for (int rowBottom = 9; rowBottom <= 15; ++rowBottom)
			buttonList.add(new GuiButton(rowBottom, xOffSet + 44 + rowBottom * 18 - 162, yOffSet + 63, 16, 7, ""));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(StatCollector.translateToLocal(transportInventory.getInventoryName()), 8, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("RF: " + transportInventory.getEnergyStored(ForgeDirection.UNKNOWN)), 100, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_TRANSPORTER);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiButton)
			if (guibutton.id >= 2 && guibutton.id <= 15)
				if (transportInventory.getStackInSlot(guibutton.id) != null && transportInventory.getStackInSlot(guibutton.id).stackTagCompound.hasKey("chipX")) {
					int newDim = transportInventory.getStackInSlot(guibutton.id).getTagCompound().getInteger("chipDim");
					int x = transportInventory.getStackInSlot(guibutton.id).getTagCompound().getInteger("chipX");
					int y = transportInventory.getStackInSlot(guibutton.id).getTagCompound().getInteger("chipY");
					int z = transportInventory.getStackInSlot(guibutton.id).getTagCompound().getInteger("chipZ");
					int xx = transportInventory.xCoord;
					int yy = transportInventory.yCoord;
					int zz = transportInventory.zCoord;
					
					if (transportInventory.canTeleport()) {
						TransportTerminal.networkWrapper.sendToServer(new EnergyMessage(mc.thePlayer, xx, yy, zz));
						TransportTerminal.networkWrapper.sendToServer(new TeleportMessage(mc.thePlayer, x, y, z, newDim));
					}
					mc.thePlayer.closeScreen();
				}
	}
}