package transportterminal.gui.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.gui.button.GuiLargeButton;
import transportterminal.gui.server.ContainerGenerator;
import transportterminal.network.message.GeneratorMessage;
import transportterminal.tileentites.TileEntityGenerator;
import transportterminal.tileentites.TileEntityGenerator.EnumStatus;

@SideOnly(Side.CLIENT)
public class GuiGenerator extends GuiContainer {

	private static final ResourceLocation GUI_GENERATOR = new ResourceLocation("transportterminal:textures/gui/generator_gui.png");
	private final TileEntityGenerator tile;

	public GuiGenerator(EntityPlayer player, TileEntityGenerator tile) {
		super(new ContainerGenerator(player, tile));
		this.tile = tile;
		allowUserInput = false;
		ySize = 168;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		buttonList.add(new GuiLargeButton(0, xOffSet + 8, yOffSet + 7, 0, 27, "Bottom"));
		buttonList.add(new GuiLargeButton(1, xOffSet + 8, yOffSet + 21, 0, 27, "Top"));
		buttonList.add(new GuiLargeButton(2, xOffSet + 8, yOffSet + 35, 0, 27, "North"));
		buttonList.add(new GuiLargeButton(3, xOffSet + 91, yOffSet + 7, 0, 27, "South"));
		buttonList.add(new GuiLargeButton(4, xOffSet + 91, yOffSet + 21, 0, 27, "West"));
		buttonList.add(new GuiLargeButton(5, xOffSet + 91, yOffSet + 35, 0, 27, "East"));
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
		fontRenderer.drawString(I18n.format("RF: " + tile.getEnergyStored(null)), 100, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_GENERATOR);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		int process = tile.getProcessTime(16);
		drawTexturedModalRect(xOffSet, yOffSet, 0, 0, xSize, ySize);
        drawTexturedModalRect(xOffSet + 116, yOffSet + 69 - process, 178, 16 - process, 16, process);

		EnumStatus DOWN = tile.getSideStatus(EnumFacing.DOWN);
		EnumStatus UP = tile.getSideStatus(EnumFacing.UP);
		EnumStatus NORTH = tile.getSideStatus(EnumFacing.NORTH);
		EnumStatus SOUTH = tile.getSideStatus(EnumFacing.SOUTH);
		EnumStatus WEST = tile.getSideStatus(EnumFacing.WEST);
		EnumStatus EAST = tile.getSideStatus(EnumFacing.EAST);

		fontRenderer.drawString(I18n.format(DOWN.getName()), xOffSet + 75 - fontRenderer.getStringWidth(I18n.format(DOWN.getName())) / 2, yOffSet + 9, getModeColour(DOWN.ordinal()));
		fontRenderer.drawString(I18n.format(UP.getName()), xOffSet + 75 - fontRenderer.getStringWidth(I18n.format(UP.getName())) / 2, yOffSet + 23, getModeColour(UP.ordinal()));
		fontRenderer.drawString(I18n.format(NORTH.getName()), xOffSet + 75 - fontRenderer.getStringWidth(I18n.format(NORTH.getName())) / 2, yOffSet + 37, getModeColour(NORTH.ordinal()));
		fontRenderer.drawString(I18n.format(SOUTH.getName()), xOffSet + 158 - fontRenderer.getStringWidth(I18n.format(SOUTH.getName())) / 2, yOffSet + 9, getModeColour(SOUTH.ordinal()));
		fontRenderer.drawString(I18n.format(WEST.getName()), xOffSet + 158 - fontRenderer.getStringWidth(I18n.format(WEST.getName())) / 2, yOffSet + 23, getModeColour(WEST.ordinal()));
		fontRenderer.drawString(I18n.format(EAST.getName()), xOffSet + 158 - fontRenderer.getStringWidth(I18n.format(EAST.getName())) / 2, yOffSet + 37, getModeColour(EAST.ordinal()));
	}

	public int getModeColour(int index) {
		switch (index) {
		case 0:
			return 16776960;
		case 1:
			return 16711680;
		default:
			return 16776960;
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiButton)
			TransportTerminal.NETWORK_WRAPPER.sendToServer(new GeneratorMessage(mc.player, guibutton.id, tile.getPos()));
	}
}