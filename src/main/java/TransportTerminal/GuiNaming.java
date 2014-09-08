package TransportTerminal;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiNaming extends GuiContainer {

	private static final ResourceLocation GUI_REMOTE = new ResourceLocation("transportterminal:textures/gui/transportTerminalRemoteGui.png");
	private GuiTextField textFieldName;
	private final TileEntityTransportTerminal transportInventory;

	public GuiNaming(InventoryPlayer playerInventory, TileEntityTransportTerminal tile) {
		super(new ContainerTerminal(playerInventory, tile, 1));
		xSize = 176;
		ySize = 51;
		transportInventory = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1, 1, 1, 1);
		super.mc.renderEngine.bindTexture(GUI_REMOTE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		textFieldName = new GuiTextField(fontRendererObj, 20, 15, 136, 20);
		textFieldName.setMaxStringLength(20);
		textFieldName.setFocused(false);
		textFieldName.setTextColor(5635925);
		buttonList.clear();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		buttonList.add(new GuiButton(0, xOffSet, yOffSet - 16, 32, 16, "Save"));
	}

	@Override
	public void updateScreen() {
		textFieldName.updateCursorCounter();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		textFieldName.drawTextBox();
	}

	@Override
	protected void keyTyped(char key, int par2) {
		textFieldName.textboxKeyTyped(key, par2);
		if (!(par2 == Keyboard.KEY_E && textFieldName.isFocused()))
			super.keyTyped(key, par2);
		if ((par2 == Keyboard.KEY_ESCAPE))
			TransportTerminal.networkWrapper.sendToServer(new NamingMessage(mc.thePlayer, transportInventory.xCoord, transportInventory.yCoord, transportInventory.zCoord, "Un-named Location"));
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		textFieldName.mouseClicked(20, 15, k);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiButton)
			if (guibutton.id == 0) {
				if (StringUtils.isNullOrEmpty(textFieldName.getText()))
					TransportTerminal.networkWrapper.sendToServer(new NamingMessage(mc.thePlayer, transportInventory.xCoord, transportInventory.yCoord, transportInventory.zCoord, "Un-named Location"));
				else
					TransportTerminal.networkWrapper.sendToServer(new NamingMessage(mc.thePlayer, transportInventory.xCoord, transportInventory.yCoord, transportInventory.zCoord, textFieldName.getText()));
				mc.thePlayer.closeScreen();
			}
	}
}