package transportterminal.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderInformation() {

		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		mesher.register(TransportTerminal.chip, 0, new ModelResourceLocation("transportterminal:chip", "inventory"));
		mesher.register(TransportTerminal.playerChip, 0, new ModelResourceLocation("transportterminal:playerChip", "inventory"));
		mesher.register(TransportTerminal.remote, 0, new ModelResourceLocation("transportterminal:remote", "inventory"));
		mesher.register(TransportTerminal.remoteTerminal, 0, new ModelResourceLocation("transportterminal:remoteTerminal", "inventory"));

		mesher.register(Item.getItemFromBlock(TransportTerminal.utils), 0, new ModelResourceLocation("transportterminal:utils", "inventory"));
		mesher.register(Item.getItemFromBlock(TransportTerminal.summoner), 0, new ModelResourceLocation("transportterminal:summoner", "inventory"));
		mesher.register(Item.getItemFromBlock(TransportTerminal.charger), 0, new ModelResourceLocation("transportterminal:charger", "inventory"));
		mesher.register(Item.getItemFromBlock(TransportTerminal.terminal), 0, new ModelResourceLocation("transportterminal:console", "inventory"));
	}
}
