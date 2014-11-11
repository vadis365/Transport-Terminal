package TransportTerminal.inventory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import TransportTerminal.TransportTerminal;
import TransportTerminal.network.ChipUtilsMessage;
import TransportTerminal.tileentites.TileEntityChipUtilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiChipUtils extends GuiContainer {

	private static final ResourceLocation GUI_CHIP_UTILS = new ResourceLocation("transportterminal:textures/gui/transportChipUtils.png");
	private final TileEntityChipUtilities tile;
	public final int COPY_CHIP = 0, ERASE_CHIP = 1, ERASE_PLAYER_CHIP = 2;

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
		buttonList.add(new GuiButton(0, xOffSet + 97, yOffSet + 31, 18, 10, "+"));
		buttonList.add(new GuiButton(1, xOffSet + 97, yOffSet + 42, 18, 10, "-"));
		buttonList.add(new GuiButton(2, xOffSet + 97, yOffSet + 53, 18, 10, "?"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(StatCollector.translateToLocal(tile.getInventoryName()), 8, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime,int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_CHIP_UTILS);
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
				if (tile.getStackInSlot(1) != null && tile.getStackInSlot(2) != null && isBasicChipItem(tile.getStackInSlot(1).getItem()) && isBasicChipItem(tile.getStackInSlot(2).getItem()) && isBlankChip(tile.getStackInSlot(2)))
					TransportTerminal.networkWrapper.sendToServer(new ChipUtilsMessage(mc.thePlayer, "", x, y, z, COPY_CHIP));

				if (tile.getStackInSlot(1) != null && tile.getStackInSlot(2) != null && isPlayerChipItem(tile.getStackInSlot(1).getItem()) && isPlayerChipItem(tile.getStackInSlot(2).getItem()) && isBlankPlayerChip(tile.getStackInSlot(2)))
					TransportTerminal.networkWrapper.sendToServer(new ChipUtilsMessage(mc.thePlayer, "", x, y, z, COPY_CHIP));
			}

			if (guibutton.id == 1) {
				if (tile.getStackInSlot(2) != null && isBasicChipItem(tile.getStackInSlot(2).getItem()))
					TransportTerminal.networkWrapper.sendToServer(new ChipUtilsMessage(mc.thePlayer, "", x, y, z, ERASE_CHIP));

				if (tile.getStackInSlot(2) != null && isPlayerChipItem(tile.getStackInSlot(2).getItem()))
					TransportTerminal.networkWrapper.sendToServer(new ChipUtilsMessage(mc.thePlayer, "", x, y, z, ERASE_PLAYER_CHIP));
			}
		}
	}

	public boolean isBlankChip(ItemStack stack) {
		return stack.stackTagCompound != null && !stack.stackTagCompound.hasKey("chipDim");
	}

	public boolean isBlankPlayerChip(ItemStack stack) {
		return stack.stackTagCompound != null && !stack.hasDisplayName();
	}

	public boolean isBasicChipItem(Item item) {
		return item != null && item == TransportTerminal.transportTerminalChip;
	}

	public boolean isPlayerChipItem(Item item) {
		return item != null && item == TransportTerminal.transportTerminalPlayerChip;
	}

}
