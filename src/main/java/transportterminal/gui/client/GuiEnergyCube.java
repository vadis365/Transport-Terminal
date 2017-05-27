package transportterminal.gui.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.gui.button.GuiLargeButton;
import transportterminal.gui.server.ContainerEnergyCube;
import transportterminal.network.message.EnergyCubeMessage;
import transportterminal.tileentites.TileEntityEnergyCube;
import transportterminal.tileentites.TileEntityEnergyCube.EnumStatus;

@SideOnly(Side.CLIENT)
public class GuiEnergyCube extends GuiContainer {

	private static final ResourceLocation GUI_ENERGY_CUBE = new ResourceLocation("transportterminal:textures/gui/energy_cube_gui.png");
	private final TileEntityEnergyCube tile;

	public GuiEnergyCube(EntityPlayer player, TileEntityEnergyCube tile) {
		super(new ContainerEnergyCube(player, tile));
		this.tile = tile;
		ySize = 168;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		buttonList.clear();
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		buttonList.add(new GuiLargeButton(0, xOffSet + 25, yOffSet + 32, 0, 27, "Bottom"));
		buttonList.add(new GuiLargeButton(1, xOffSet + 25, yOffSet + 52, 0, 27, "Top"));
		buttonList.add(new GuiLargeButton(2, xOffSet + 25, yOffSet + 72, 0, 27, "North"));
		buttonList.add(new GuiLargeButton(3, xOffSet + 25, yOffSet + 92, 0, 27, "South"));
		buttonList.add(new GuiLargeButton(4, xOffSet + 25, yOffSet + 112, 0, 27, "West"));
		buttonList.add(new GuiLargeButton(5, xOffSet + 25, yOffSet + 132, 0, 27, "East"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		String rfEnergy = "RF: " + tile.getEnergyStored(null) +"/" + ConfigHandler.ENERGY_CUBE_MAX_ENERGY;
		fontRendererObj.drawString(I18n.format(rfEnergy), xSize / 2 - fontRendererObj.getStringWidth(I18n.format(rfEnergy)) / 2, ySize - 160, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_ENERGY_CUBE);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;

		EnumStatus DOWN = tile.getSideStatus(EnumFacing.DOWN);
		EnumStatus UP = tile.getSideStatus(EnumFacing.UP);
		EnumStatus NORTH = tile.getSideStatus(EnumFacing.NORTH);
		EnumStatus SOUTH = tile.getSideStatus(EnumFacing.SOUTH);
		EnumStatus WEST = tile.getSideStatus(EnumFacing.WEST);
		EnumStatus EAST = tile.getSideStatus(EnumFacing.EAST);

		fontRendererObj.drawString(I18n.format(DOWN.getName()), xOffSet + 124 - fontRendererObj.getStringWidth(I18n.format(DOWN.getName())) / 2, yOffSet + 34, getModeColour(DOWN.ordinal()));
		fontRendererObj.drawString(I18n.format(UP.getName()), xOffSet + 124 - fontRendererObj.getStringWidth(I18n.format(UP.getName())) / 2, yOffSet + 54, getModeColour(UP.ordinal()));
		fontRendererObj.drawString(I18n.format(NORTH.getName()), xOffSet + 124 - fontRendererObj.getStringWidth(I18n.format(NORTH.getName())) / 2, yOffSet + 74, getModeColour(NORTH.ordinal()));
		fontRendererObj.drawString(I18n.format(SOUTH.getName()), xOffSet + 124 - fontRendererObj.getStringWidth(I18n.format(SOUTH.getName())) / 2, yOffSet + 94, getModeColour(SOUTH.ordinal()));
		fontRendererObj.drawString(I18n.format(WEST.getName()), xOffSet + 124 - fontRendererObj.getStringWidth(I18n.format(WEST.getName())) / 2, yOffSet + 114, getModeColour(WEST.ordinal()));
		fontRendererObj.drawString(I18n.format(EAST.getName()), xOffSet + 124 - fontRendererObj.getStringWidth(I18n.format(EAST.getName())) / 2, yOffSet + 134, getModeColour(EAST.ordinal()));
	}

	public int getModeColour(int index) {
		switch (index) {
		case 0:
			return 16776960;
		case 1:
			return 5285857;
		case 2:
			return 16711680;
		default:
			return 16776960;
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiButton)
			TransportTerminal.NETWORK_WRAPPER.sendToServer(new EnergyCubeMessage(mc.player, guibutton.id, tile.getPos()));
	}

}