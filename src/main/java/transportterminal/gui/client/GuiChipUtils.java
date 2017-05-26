package transportterminal.gui.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import transportterminal.TransportTerminal;
import transportterminal.gui.button.GuiLargeButton;
import transportterminal.gui.server.ContainerChipUtils;
import transportterminal.network.message.ChipUtilsMessage;
import transportterminal.tileentites.TileEntityChipUtilities;

@SideOnly(Side.CLIENT)
public class GuiChipUtils extends GuiContainer {

	private static final ResourceLocation GUI_CHIP_UTILS = new ResourceLocation("transportterminal:textures/gui/transport_chip_utils_gui.png");
	private final TileEntityChipUtilities tile;
	public final int COPY_CHIP = 0, ERASE_CHIP = 1, ERASE_PLAYER_CHIP = 2, NAME_PLAYER_CHIP = 3;

	public GuiChipUtils(EntityPlayer player, TileEntityChipUtilities tile) {
		super(new ContainerChipUtils(player, tile));
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
		buttonList.add(new GuiLargeButton(0, xOffSet + 61, yOffSet + 30, 0, 27, "Copy"));
		buttonList.add(new GuiLargeButton(1, xOffSet + 61, yOffSet + 44, 0, 27, "Erase"));
		buttonList.add(new GuiLargeButton(2, xOffSet + 61, yOffSet + 58, 0, 27, "Name"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
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
		BlockPos pos = tile.getPos();

		if (guibutton instanceof GuiButton) {
			if (guibutton.id == 0) {
				if (tile.getStackInSlot(0) != null && tile.getStackInSlot(1) != null && isBasicChipItem(tile.getStackInSlot(0).getItem()) && isBasicChipItem(tile.getStackInSlot(1).getItem()) && isBlankChip(tile.getStackInSlot(1)))
					TransportTerminal.NETWORK_WRAPPER.sendToServer(new ChipUtilsMessage(mc.player, "", pos, COPY_CHIP));

				if (tile.getStackInSlot(0) != null && tile.getStackInSlot(1) != null && isPlayerChipItem(tile.getStackInSlot(0).getItem()) && isPlayerChipItem(tile.getStackInSlot(1).getItem()) && isBlankPlayerChip(tile.getStackInSlot(1)))
					TransportTerminal.NETWORK_WRAPPER.sendToServer(new ChipUtilsMessage(mc.player, "", pos, COPY_CHIP));
			}
			if (guibutton.id == 1)
				if (tile.getStackInSlot(1) == null) {
					if (tile.getStackInSlot(0) != null && isBasicChipItem(tile.getStackInSlot(0).getItem()))
						TransportTerminal.NETWORK_WRAPPER.sendToServer(new ChipUtilsMessage(mc.player, "", pos, ERASE_CHIP));

					if (tile.getStackInSlot(0) != null && isPlayerChipItem(tile.getStackInSlot(0).getItem()))
						TransportTerminal.NETWORK_WRAPPER.sendToServer(new ChipUtilsMessage(mc.player, "", pos, ERASE_PLAYER_CHIP));
				}

			if (guibutton.id == 2)
				if (tile.getStackInSlot(1) == null)
					if (tile.getStackInSlot(0) != null && isPlayerChipItem(tile.getStackInSlot(0).getItem()))
						mc.player.openGui(TransportTerminal.INSTANCE, TransportTerminal.PROXY.GUI_ID_CHIP_UTILS_NAMING, mc.player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ());
		}
	}

	public boolean isBlankChip(ItemStack stack) {
		return stack.getTagCompound() != null && !stack.getTagCompound().hasKey("chipDim");
	}

	public boolean isBlankPlayerChip(ItemStack stack) {
		return stack.getTagCompound() != null && !stack.hasDisplayName();
	}

	public boolean isBasicChipItem(Item item) {
		return item != null && item == TransportTerminal.CHIP;
	}

	public boolean isPlayerChipItem(Item item) {
		return item != null && item == TransportTerminal.PLAYER_CHIP;
	}
}