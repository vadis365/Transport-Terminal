package TransportTerminal;

import TransportTerminal.models.TileEntityTransportTerminalRenderer;
import TransportTerminal.tileentites.TileEntityTransportTerminal;
import cpw.mods.fml.client.registry.ClientRegistry;


public class ClientProxyTransportTerminal extends CommonProxyTransportTerminal {
	@Override
	public void registerRenderInformation() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransportTerminal.class, new TileEntityTransportTerminalRenderer());
	}
}
