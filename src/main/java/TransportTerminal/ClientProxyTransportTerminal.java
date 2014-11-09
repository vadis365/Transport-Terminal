package TransportTerminal;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import TransportTerminal.models.ItemTransportTerminalRenderer;
import TransportTerminal.models.TileEntityTransportTerminalRenderer;
import TransportTerminal.tileentites.TileEntityTransportTerminal;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxyTransportTerminal extends CommonProxyTransportTerminal {

	@Override
	public void registerRenderInformation() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransportTerminal.class, new TileEntityTransportTerminalRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TransportTerminal.transportTerminal), new ItemTransportTerminalRenderer());
	}
}
