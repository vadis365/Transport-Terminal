package TransportTerminal;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeChunkManager;
import TransportTerminal.blocks.BlockTransportItems;
import TransportTerminal.blocks.BlockTransportTerminal;
import TransportTerminal.items.ItemTransportTerminalChip;
import TransportTerminal.items.ItemTransportTerminalRemote;
import TransportTerminal.network.NamingMessage;
import TransportTerminal.network.NamingPacketHandler;
import TransportTerminal.network.TeleportMessage;
import TransportTerminal.network.TeleportMessageItems;
import TransportTerminal.network.TeleportPacketHandler;
import TransportTerminal.network.TeleportPacketHandlerItems;
import TransportTerminal.recipescreativetabs.CreativeTabsTransportTerminal;
import TransportTerminal.recipescreativetabs.TransportTerminalCrafting;
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

@Mod(modid = "transportterminal", name = "Transport Terminal", version = "0.99a")
public class TransportTerminal {

	@Instance("transportterminal")
	public static TransportTerminal instance;

	@SidedProxy(clientSide = "TransportTerminal.ClientProxyTransportTerminal", serverSide = "TransportTerminal.CommonProxyTransportTerminal")
	public static CommonProxyTransportTerminal proxy;

	public static Item transportTerminalRemote;
	public static Item transportTerminalChip;
	public static Block transportTerminal;
	public static Block transportItems;
	public static SimpleNetworkWrapper networkWrapper;
	public static CreativeTabs creativeTabsTT = new CreativeTabsTransportTerminal("TransportTerminals");
	@EventHandler
	public void Init(FMLInitializationEvent event) {
		proxy.registerRenderInformation();
		proxy.registerTileEntities();

		transportTerminalRemote = new ItemTransportTerminalRemote().setUnlocalizedName("transportTerminalRemote").setTextureName("transportterminal:transportTerminalRemote");
		transportTerminalChip = new ItemTransportTerminalChip().setUnlocalizedName("transportTerminalChip").setTextureName("transportterminal:transportTerminalChipBlank");
		transportTerminal = new BlockTransportTerminal().setHardness(3.0F).setBlockName("transportTerminal").setBlockTextureName("transportterminal:transportTerminal");
		transportItems = new BlockTransportItems().setHardness(3.0F).setBlockName("transportItems").setBlockTextureName("transportterminal:transportItemsFront");
		
		GameRegistry.registerItem(transportTerminalRemote, "Transport Terminal Remote");
		GameRegistry.registerItem(transportTerminalChip, "Transport Terminal Chip");
		GameRegistry.registerBlock(transportTerminal, "Transport Terminal");
		GameRegistry.registerBlock(transportItems, "Transport Items");

		TransportTerminalCrafting.addRecipes();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("transportterminal");
		networkWrapper.registerMessage(TeleportPacketHandler.class, TeleportMessage.class, 0, Side.SERVER);
		networkWrapper.registerMessage(NamingPacketHandler.class, NamingMessage.class, 1, Side.SERVER);
		networkWrapper.registerMessage(TeleportPacketHandlerItems.class, TeleportMessageItems.class, 2, Side.SERVER);
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, null);
	}
}