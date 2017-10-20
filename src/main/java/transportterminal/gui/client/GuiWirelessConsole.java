package transportterminal.gui.client;

import cofh.redstoneflux.api.IEnergyContainerItem;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.gui.button.GuiConsoleButton;
import transportterminal.gui.server.ContainerTerminal;
import transportterminal.network.message.ButtonMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;

@SideOnly(Side.CLIENT)
public class GuiWirelessConsole extends GuiContainer {

	private static final ResourceLocation GUI_TRANSPORTER = new ResourceLocation("transportterminal:textures/gui/transport_terminal_gui.png");
	private EntityPlayer playerSent;

	public GuiWirelessConsole(EntityPlayer player) {
		super(new ContainerTerminal(player, new TileEntityTransportTerminal(), 0));
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
			buttonList.add(new GuiConsoleButton(rowTop, xOffSet + 44 + rowTop * 18 - 36, yOffSet + 18, 0, 0, ""));
		for (int rowBottom = 9; rowBottom <= 15; ++rowBottom)
			buttonList.add(new GuiConsoleButton(rowBottom, xOffSet + 44 + rowBottom * 18 - 162, yOffSet + 63, 0, 0, ""));
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		ItemStack stack = playerSent.getHeldItemMainhand();
		int xx = stack.getTagCompound().getInteger("homeX");
		int yy = stack.getTagCompound().getInteger("homeY");
		int zz = stack.getTagCompound().getInteger("homeZ");
		fontRenderer.drawString(I18n.format("Location X: " + xx + " Y: " + yy + " Z: " + zz), 8, 6, 4210752);
		fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
		fontRenderer.drawString(I18n.format("RF: " + ((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack)), 100, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_TRANSPORTER);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		ItemStack stack = playerSent.getHeldItemMainhand();
		int newDim = stack.getTagCompound().getInteger("dim");
		int x = stack.getTagCompound().getInteger("homeX");
		int y = stack.getTagCompound().getInteger("homeY");
		int z = stack.getTagCompound().getInteger("homeZ");

		if (guibutton instanceof GuiButton)
			if (guibutton.id >= 2 && guibutton.id <= 15) {
				TransportTerminal.NETWORK_WRAPPER.sendToServer(new ButtonMessage(mc.player, guibutton.id, x, y, z, newDim));
				mc.player.closeScreen();
			}
	}
}