package transportterminal.gui.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import transportterminal.TransportTerminal;
import transportterminal.gui.server.ContainerChipUtils;
import transportterminal.network.message.ChipUtilsMessage;
import transportterminal.tileentites.TileEntityChipUtilities;

@SideOnly(Side.CLIENT)
public class GuiChipUtils extends GuiContainer {

	private static final ResourceLocation GUI_CHIP_UTILS = new ResourceLocation("transportterminal:textures/gui/transportChipUtilsGui.png");
	private final TileEntityChipUtilities tile;
	public final int COPY_CHIP = 0, ERASE_CHIP = 1, ERASE_PLAYER_CHIP = 2, NAME_PLAYER_CHIP = 3;

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
		buttonList.add(new GuiButton(0, xOffSet + 61, yOffSet + 30, 54, 12, "copy"));
		buttonList.add(new GuiButton(1, xOffSet + 61, yOffSet + 44, 54, 12, "erase"));
		buttonList.add(new GuiButton(2, xOffSet + 61, yOffSet + 58, 54, 12, "name"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		//fontRendererObj.drawString(StatCollector.translateToLocal(tile.getInventoryName()), 8, 6, 4210752);
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
		int x = tile.getPos().getX();
		int y = tile.getPos().getY();
		int z = tile.getPos().getZ();

		if (guibutton instanceof GuiButton) {
			if (guibutton.id == 0) {
				if (tile.getStackInSlot(0) != null && tile.getStackInSlot(1) != null && isBasicChipItem(tile.getStackInSlot(0).getItem()) && isBasicChipItem(tile.getStackInSlot(1).getItem()) && isBlankChip(tile.getStackInSlot(1)))
					TransportTerminal.networkWrapper.sendToServer(new ChipUtilsMessage(mc.thePlayer, "", x, y, z, COPY_CHIP));

				if (tile.getStackInSlot(0) != null && tile.getStackInSlot(1) != null && isPlayerChipItem(tile.getStackInSlot(0).getItem()) && isPlayerChipItem(tile.getStackInSlot(1).getItem()) && isBlankPlayerChip(tile.getStackInSlot(1)))
					TransportTerminal.networkWrapper.sendToServer(new ChipUtilsMessage(mc.thePlayer, "", x, y, z, COPY_CHIP));
			}
			if (guibutton.id == 1)
				if (tile.getStackInSlot(1) == null) {
					if (tile.getStackInSlot(0) != null && isBasicChipItem(tile.getStackInSlot(0).getItem()))
						TransportTerminal.networkWrapper.sendToServer(new ChipUtilsMessage(mc.thePlayer, "", x, y, z, ERASE_CHIP));

					if (tile.getStackInSlot(0) != null && isPlayerChipItem(tile.getStackInSlot(0).getItem()))
						TransportTerminal.networkWrapper.sendToServer(new ChipUtilsMessage(mc.thePlayer, "", x, y, z, ERASE_PLAYER_CHIP));
				}

			if (guibutton.id == 2)
				if (tile.getStackInSlot(1) == null)
					if (tile.getStackInSlot(0) != null && isPlayerChipItem(tile.getStackInSlot(0).getItem()))
						mc.thePlayer.openGui(TransportTerminal.instance, TransportTerminal.proxy.GUI_ID_CHIP_UTILS_NAMING, mc.thePlayer.worldObj, x, y, z);
		}
	}

	public boolean isBlankChip(ItemStack stack) {
		return stack.getTagCompound() != null && !stack.getTagCompound().hasKey("chipDim");
	}

	public boolean isBlankPlayerChip(ItemStack stack) {
		return stack.getTagCompound() != null && !stack.hasDisplayName();
	}

	public boolean isBasicChipItem(Item item) {
		return item != null && item == TransportTerminal.chip;
	}

	public boolean isPlayerChipItem(Item item) {
		return item != null && item == TransportTerminal.playerChip;
	}

}
