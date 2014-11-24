package transportterminal.gui.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import transportterminal.TransportTerminal;
import transportterminal.gui.server.ContainerTerminal;
import transportterminal.network.message.ButtonMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTerminalShadow extends GuiContainer {

	private static final ResourceLocation GUI_TRANSPORTER = new ResourceLocation("transportterminal:textures/gui/transportTerminalGui.png");
	private EntityPlayer playerSent;

	public GuiTerminalShadow(InventoryPlayer inventory, EntityPlayer player) {
		super(new ContainerTerminal(inventory, new TileEntityTransportTerminal(), 0));
		playerSent = player;
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
		ItemStack stack = playerSent.getCurrentEquippedItem();
		int xx = stack.getTagCompound().getInteger("homeX");
		int yy = stack.getTagCompound().getInteger("homeY");
		int zz = stack.getTagCompound().getInteger("homeZ");
		fontRendererObj.drawString(StatCollector.translateToLocal("Location X: " + xx + " Y: " + yy + " Z: " + zz), 8, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		if (TransportTerminal.IS_RF_PRESENT)
			fontRendererObj.drawString(StatCollector.translateToLocal("RF: " + ((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack)), 100, ySize - 96 + 2, 4210752);
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
		ItemStack stack = playerSent.getCurrentEquippedItem();
		int xx = stack.getTagCompound().getInteger("homeX");
		int yy = stack.getTagCompound().getInteger("homeY");
		int zz = stack.getTagCompound().getInteger("homeZ");
		
		if (guibutton instanceof GuiButton)
			if (guibutton.id >= 2 && guibutton.id <= 15) {
				TransportTerminal.networkWrapper.sendToServer(new ButtonMessage(mc.thePlayer, guibutton.id,  xx, yy, zz));
				mc.thePlayer.closeScreen();
			}
	}
}