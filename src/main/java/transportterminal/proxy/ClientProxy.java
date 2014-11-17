package transportterminal.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import transportterminal.TransportTerminal;
import transportterminal.models.ItemTransportTerminalRenderer;
import transportterminal.models.TileEntityTransportTerminalRenderer;
import transportterminal.tileentites.TileEntityTransportTerminal;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderInformation() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransportTerminal.class, new TileEntityTransportTerminalRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TransportTerminal.terminal), new ItemTransportTerminalRenderer());
	}
}
