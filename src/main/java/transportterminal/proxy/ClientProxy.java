package transportterminal.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderInformation() {
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.CHIP, 0, new ModelResourceLocation("transportterminal:chip", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.PLAYER_CHIP, 0, new ModelResourceLocation("transportterminal:playerChip", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.REMOTE, 0, new ModelResourceLocation("transportterminal:remote", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.REMOTE_TERMINAL, 0, new ModelResourceLocation("transportterminal:remoteTerminal", "inventory"));

		ModelLoader.setCustomModelResourceLocation(TransportTerminal.UTILS_ITEM, 0, new ModelResourceLocation("transportterminal:utils", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.SUMMONER_ITEM, 0, new ModelResourceLocation("transportterminal:summoner", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.CHARGER_ITEM, 0, new ModelResourceLocation("transportterminal:charger", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TransportTerminal.TERMINAL_ITEM, 0, new ModelResourceLocation("transportterminal:console", "inventory"));
	}
}
