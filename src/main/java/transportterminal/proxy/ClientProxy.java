package transportterminal.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import transportterminal.TransportTerminal;
import transportterminal.models.ItemTransportTerminalRenderer;
import transportterminal.models.SummonerBlockRender;
import transportterminal.models.TileEntityTransportTerminalRenderer;
import transportterminal.tileentites.TileEntityTransportTerminal;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	public enum BlockRenderIDs {
		SUMMON_BLOCK;

		private final int ID;

		BlockRenderIDs() {
			ID = RenderingRegistry.getNextAvailableRenderId();
		}

		public int id() {
			return ID;
		}
	}

	@Override
	public void registerRenderInformation() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransportTerminal.class, new TileEntityTransportTerminalRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TransportTerminal.terminal), new ItemTransportTerminalRenderer());
		RenderingRegistry.registerBlockHandler(new SummonerBlockRender());
	}
}
