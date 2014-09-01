package TransportTerminal;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "transportterminal", name = "Transport Terminal", version = "0.99a")
public class TransportTerminal {
	
	@Instance("transportterminal")
	public static TransportTerminal instance;
	
	@SidedProxy(clientSide = "TransportTerminal.ClientProxyTransportTerminal", serverSide = "TransportTerminal.CommonProxyTransportTerminal")
	public static CommonProxyTransportTerminal proxy;
	public static Item transportTerminalRemote;
	public static Item transportTerminalChip;
	public static Block transportTerminal;
	public static SimpleNetworkWrapper networkWrapper;
	
	@EventHandler
	public void Init(FMLInitializationEvent event) {
		proxy.registerRenderInformation();
		proxy.registerTileEntities();
		
		transportTerminalRemote = new ItemTransportTerminalRemote().setUnlocalizedName("transportTerminalRemote").setTextureName("transportTerminal:transportTerminalRemote");
		transportTerminalChip = new ItemTransportTerminalChip().setUnlocalizedName("transportTerminalChip").setTextureName("transportTerminal:transportTerminalChipBlank");
		transportTerminal = new BlockTransportTerminal().setHardness(3.0F).setBlockName("transportTerminal").setBlockTextureName("transportTerminal:transportTerminal");

		GameRegistry.registerItem(transportTerminalRemote, "Transport Terminal Remote");
		GameRegistry.registerItem(transportTerminalChip, "Transport Terminal Chip");
		GameRegistry.registerBlock(transportTerminal, "Transport Terminal");
		
		TransportTerminalCrafting.addRecipes();
	}

	@EventHandler
	@SideOnly(Side.CLIENT)
	public void preInit(FMLPreInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("transportterminal");
		networkWrapper.registerMessage(TeleportPacketHandler.class, TeleportMessage.class, 0, Side.SERVER); 
		networkWrapper.registerMessage(NamingPacketHandler.class, NamingMessage.class, 1, Side.SERVER);
	}
}