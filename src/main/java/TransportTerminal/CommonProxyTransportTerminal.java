package TransportTerminal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import TransportTerminal.inventory.ContainerItemSender;
import TransportTerminal.inventory.ContainerTerminal;
import TransportTerminal.inventory.GuiItemSender;
import TransportTerminal.inventory.GuiNaming;
import TransportTerminal.inventory.GuiTerminal;
import TransportTerminal.tileentites.TileEntityTransportItems;
import TransportTerminal.tileentites.TileEntityTransportTerminal;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxyTransportTerminal implements IGuiHandler {

	public final int GUI_ID_TERMINAL = 0, GUI_ID_REMOTE = 1, GUI_ID_ITEMS = 2;

	public void registerRenderInformation() {
	}

	public void registerTileEntities() {
		registerTileEntity(TileEntityTransportTerminal.class, "transportTerminal");
		registerTileEntity(TileEntityTransportItems.class, "transportItems");
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

		if (ID == GUI_ID_REMOTE)
			return new ContainerTerminal(player.inventory, null, 1);

		if (ID == GUI_ID_ITEMS) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityTransportItems)
				return new ContainerItemSender(player.inventory, (TileEntityTransportItems) tileentity);
		}

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

		if (ID == GUI_ID_ITEMS) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityTransportItems)
				return new GuiItemSender(player.inventory, (TileEntityTransportItems) tileentity);
		}
		return null;
	}
}