package transportterminal.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.tileentites.TileEntityItemTransporter;
import transportterminal.tileentites.TileEntityItemTransporterRenderer;
import transportterminal.tileentites.TileEntitySummoner;
import transportterminal.tileentites.TileEntitySummonerRenderer;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderInformation() {
/*		
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.CHIP, 0, new ModelResourceLocation("transportterminal:chip", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.PLAYER_CHIP, 0, new ModelResourceLocation("transportterminal:player_chip", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.REMOTE, 0, new ModelResourceLocation("transportterminal:remote", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.REMOTE_TERMINAL, 0, new ModelResourceLocation("transportterminal:remote_terminal", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.REMOTE_QUANTUM_CRATE, 0, new ModelResourceLocation("transportterminal:remote_quantum_crate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.UPGRADE_CHIP, 0, new ModelResourceLocation("transportterminal:upgrade_chip_red", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.UPGRADE_CHIP, 1, new ModelResourceLocation("transportterminal:upgrade_chip_green", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.UPGRADE_CHIP, 2, new ModelResourceLocation("transportterminal:upgrade_chip_blue", "inventory"));

		ModelLoader.setCustomModelResourceLocation(TransportTerminal.UTILS_ITEM, 0, new ModelResourceLocation("transportterminal:utils", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.SUMMONER_ITEM, 0, new ModelResourceLocation("transportterminal:summoner", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.CHARGER_ITEM, 0, new ModelResourceLocation("transportterminal:charger", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.TERMINAL_ITEM, 0, new ModelResourceLocation("transportterminal:console", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.ENERGY_CUBE_ITEM, 0, new ModelResourceLocation("transportterminal:energy_cube", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.GENERATOR_ITEM, 0, new ModelResourceLocation("transportterminal:generator", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.METAL_CRATE_ITEM, 0, new ModelResourceLocation("transportterminal:metal_crate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.QUANTUM_CRATE_ITEM, 0, new ModelResourceLocation("transportterminal:quantum_crate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.ITEM_TRANSPORTER_ITEM, 0, new ModelResourceLocation("transportterminal:item_transporter", "inventory"));
*/
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySummoner.class, new TileEntitySummonerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemTransporter.class, new TileEntityItemTransporterRenderer());
	}
}
