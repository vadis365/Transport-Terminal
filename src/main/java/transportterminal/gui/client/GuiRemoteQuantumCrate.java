package transportterminal.gui.client;

import org.lwjgl.opengl.GL11;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.gui.server.ContainerQuantumCrate;
import transportterminal.tileentites.TileEntityQuantumCrate;

@SideOnly(Side.CLIENT)
public class GuiRemoteQuantumCrate extends GuiContainer {

	private static final ResourceLocation GUI_CRATE = new ResourceLocation("transportterminal:textures/gui/quantumCrateGui.png");
	private EntityPlayer playerSent;

	public GuiRemoteQuantumCrate(EntityPlayer player) {
		super(new ContainerQuantumCrate(player, new TileEntityQuantumCrate()));
		playerSent = player;
		allowUserInput = false;
		xSize = 256;
		ySize = 256;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		ItemStack stack = playerSent.getHeldItemMainhand();
		int xx = stack.getTagCompound().getInteger("homeX");
		int yy = stack.getTagCompound().getInteger("homeY");
		int zz = stack.getTagCompound().getInteger("homeZ");
		fontRendererObj.drawString(I18n.format("Quantum Crate Location X: " + xx + " Y: " + yy + " Z: " + zz), 12, 4, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 48, ySize - 96, 4210752);
		fontRendererObj.drawString(I18n.format("RF: " + ((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack)), 156, ySize - 96, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_CRATE);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}

}