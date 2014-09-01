package TransportTerminal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxyTransportTerminal implements IGuiHandler{
	
	public static final int GUI_ID_TERMINAL = 0, GUI_ID_REMOTE = 1;
	
	public void registerRenderInformation() {
	}
	
	public void registerTileEntities() {
		registerTileEntity(TileEntityTransportTerminal.class, "transportTerminal");
	}

	private void registerTileEntity(Class<? extends TileEntity> cls, String baseName) {
		GameRegistry.registerTileEntity(cls, "tile.transportterminal." + baseName);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_TERMINAL) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityTransportTerminal) {
				return new ContainerTerminal(player.inventory, (TileEntityTransportTerminal) tileentity, 0);
			}
		}
		
		if (ID == GUI_ID_REMOTE) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityTransportTerminal) {
				return new ContainerTerminal(player.inventory, (TileEntityTransportTerminal) tileentity, 1);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_TERMINAL) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityTransportTerminal) {
				return new GuiTerminal(player.inventory, (TileEntityTransportTerminal) tileentity);
			}
		}
	
		if (ID == GUI_ID_REMOTE) {
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityTransportTerminal) {
				return new GuiNaming(player.inventory, (TileEntityTransportTerminal) tileentity);
			}
		}
		return null;
	}
}