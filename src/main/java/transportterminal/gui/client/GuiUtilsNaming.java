package transportterminal.gui.client;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import transportterminal.TransportTerminal;
import transportterminal.gui.server.ContainerTerminal;
import transportterminal.network.message.ChipUtilsMessage;
import transportterminal.tileentites.TileEntityChipUtilities;

@SideOnly(Side.CLIENT)
public class GuiUtilsNaming extends GuiContainer {

	private static final ResourceLocation GUI_REMOTE = new ResourceLocation("transportterminal:textures/gui/transportTerminalRemoteGui.png");
	private GuiTextField textFieldName;
	private EntityPlayer playerSent;
	private final TileEntityChipUtilities tile;
	public final int NAME_PLAYER_CHIP = 3;

	public GuiUtilsNaming(EntityPlayer player, TileEntityChipUtilities tile) {
		super(new ContainerTerminal(player, null, 1));
		xSize = 176;
		ySize = 51;
		playerSent = player;
		this.tile = tile;
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
		textFieldName = new GuiTextField(16, fontRendererObj, 20, 15, 136, 20);
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
	protected void keyTyped(char key, int keycode) throws IOException {
		textFieldName.textboxKeyTyped(key, keycode);
		if (!(keycode != Keyboard.KEY_NONE && textFieldName.isFocused()))
			super.keyTyped(key, keycode);
	}

	@Override
	public void mouseClicked(int i, int j, int k) throws IOException {
		super.mouseClicked(i, j, k);
		textFieldName.mouseClicked(20, 15, k);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiButton)
			if (guibutton.id == 0) {
				if (StringUtils.isNullOrEmpty(textFieldName.getText()))
					TransportTerminal.NETWORK_WRAPPER.sendToServer(new ChipUtilsMessage(playerSent, "Arch Stanton", tile.getPos(), NAME_PLAYER_CHIP));
				else
					TransportTerminal.NETWORK_WRAPPER.sendToServer(new ChipUtilsMessage(playerSent, textFieldName.getText(), tile.getPos(), NAME_PLAYER_CHIP));
				playerSent.closeScreen();
			}
	}
}