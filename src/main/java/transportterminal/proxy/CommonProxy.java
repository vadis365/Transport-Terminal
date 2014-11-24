package transportterminal.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import transportterminal.TransportTerminal;
import transportterminal.gui.client.GuiCharger;
import transportterminal.gui.client.GuiChipUtils;
import transportterminal.gui.client.GuiNaming;
import transportterminal.gui.client.GuiTerminal;
import transportterminal.gui.client.GuiTerminalShadow;
import transportterminal.gui.client.GuiUtilsNaming;
import transportterminal.gui.server.ContainerCharger;
import transportterminal.gui.server.ContainerChipUtils;
import transportterminal.gui.server.ContainerTerminal;
import transportterminal.items.ItemTransportTerminalRemote;
import transportterminal.tileentites.TileEntityCharger;
import transportterminal.tileentites.TileEntityChipUtilities;
import transportterminal.tileentites.TileEntityTransportTerminal;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {

	public final int GUI_ID_TERMINAL = 0, GUI_ID_REMOTE = 1, GUI_ID_CHIP_UTILS = 2, GUI_ID_CHIP_UTILS_NAMING = 3, GUI_ID_CHARGER = 4, GUI_ID_REMOTE_TERMINAL = 5;

	public void registerRenderInformation() {
	}

	public void registerTileEntities() {
		registerTileEntity(TileEntityTransportTerminal.class, "transportTerminal");
		registerTileEntity(TileEntityChipUtilities.class, "transportUtils");
		registerTileEntity(TileEntityCharger.class, "transportCharger");
	}

	private void registerTileEntity(Class<? extends TileEntity> cls, String baseName) {
		GameRegistry.registerTileEntity(cls, "tile.transportterminal." + baseName);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_TERMINAL) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityTransportTerminal)
				return new ContainerTerminal(player.inventory, (TileEntityTransportTerminal) tileentity, 0);
		}

		if (ID == GUI_ID_REMOTE) {
			ItemStack stack = player.getCurrentEquippedItem();
			world = DimensionManager.getWorld(player.dimension);
			ItemTransportTerminalRemote.getTile(player, stack, x, y, z);
			return new ContainerTerminal(player.inventory, null, 1);
		}

		if (ID == GUI_ID_CHIP_UTILS) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityChipUtilities)
				return new ContainerChipUtils(player.inventory, (TileEntityChipUtilities) tileentity);
		}

		if (ID == GUI_ID_CHIP_UTILS_NAMING)
			return new ContainerChipUtils(player.inventory, null);

		if (ID == GUI_ID_CHARGER) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityCharger)
				return new ContainerCharger(player.inventory, (TileEntityCharger) tileentity);
		}

		if (ID == GUI_ID_REMOTE_TERMINAL)
			if (getTile(player, world, x, y, z) instanceof TileEntityTransportTerminal)
				return new ContainerTerminal(player.inventory, (TileEntityTransportTerminal) getTile(player, world, x, y, z), 0);

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_TERMINAL) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityTransportTerminal)
				return new GuiTerminal(player.inventory, (TileEntityTransportTerminal) tileentity, 0);
		}

		if (ID == GUI_ID_REMOTE)
			return new GuiNaming(player);

		if (ID == GUI_ID_CHIP_UTILS) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityChipUtilities)
				return new GuiChipUtils(player.inventory, (TileEntityChipUtilities) tileentity);
		}

		if (ID == GUI_ID_CHIP_UTILS_NAMING) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityChipUtilities)
				return new GuiUtilsNaming(player, (TileEntityChipUtilities) tileentity);
		}

		if (ID == GUI_ID_CHARGER) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityCharger)
				return new GuiCharger(player.inventory, (TileEntityCharger) tileentity);
		}

		if (ID == GUI_ID_REMOTE_TERMINAL)
			return new GuiTerminalShadow(player.inventory, player);

		return null;
	}

	public TileEntity getTile(EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileentity;
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null && stack.getItem() == TransportTerminal.remoteTerminal) {
			WorldServer world2 = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));
			int xx = stack.getTagCompound().getInteger("homeX");
			int yy = stack.getTagCompound().getInteger("homeY");
			int zz = stack.getTagCompound().getInteger("homeZ");
			tileentity = world2.getTileEntity(xx, yy, zz);
		} else
			tileentity = world.getTileEntity(x, y, z);
		return tileentity;
	}
}
