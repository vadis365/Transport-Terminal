package TransportTerminal;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeChunkManager;
import TransportTerminal.blocks.BlockTransportTerminal;
import TransportTerminal.items.ItemTransportTerminalChip;
import TransportTerminal.items.ItemTransportTerminalRemote;
import TransportTerminal.network.EnergyMessage;
import TransportTerminal.network.NamingMessage;
import TransportTerminal.network.NamingPacketHandler;
import TransportTerminal.network.TeleportEnergyPacketHandler;
import TransportTerminal.network.TeleportMessage;
import TransportTerminal.network.TeleportPacketHandler;
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

@Mod(modid = "transportterminal", name = "Transport Terminal", version = "1.0a")
public class TransportTerminal {

	@Instance("transportterminal")
	public static TransportTerminal instance;

	@SidedProxy(clientSide = "TransportTerminal.ClientProxyTransportTerminal", serverSide = "TransportTerminal.CommonProxyTransportTerminal")
	public static CommonProxyTransportTerminal proxy;

	public static Item transportTerminalRemote;
	public static Item transportTerminalChip;
	public static Block transportTerminal;

	public static SimpleNetworkWrapper networkWrapper;
	public static CreativeTabs creativeTabsTT = new CreativeTabsTransportTerminal("TransportTerminals");

	// add configs for these
	public static int ENERGY_PER_TELEPORT = 10000;
	public static int REMOTE_MAX_ENERGY = 50000;
	public static int TERMINAL_MAX_ENERGY = 320000;
	public static int CHARGER_MAX_ENERGY = 320000;

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		proxy.registerTileEntities();
		proxy.registerRenderInformation();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		transportTerminalRemote = new ItemTransportTerminalRemote().setUnlocalizedName("transportTerminalRemote").setTextureName("transportterminal:transportTerminalRemote");
		transportTerminalChip = new ItemTransportTerminalChip().setUnlocalizedName("transportTerminalChip").setTextureName("transportterminal:transportTerminalChipBlank");
		transportTerminal = new BlockTransportTerminal().setHardness(3.0F).setBlockName("transportTerminal").setBlockTextureName("transportterminal:transportTerminal");

		GameRegistry.registerItem(transportTerminalRemote, "Transport Terminal Remote");
		GameRegistry.registerItem(transportTerminalChip, "Transport Terminal Chip");
		GameRegistry.registerBlock(transportTerminal, "Transport Terminal");

		TransportTerminalCrafting.addRecipes();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("transportterminal");
		networkWrapper.registerMessage(TeleportPacketHandler.class, TeleportMessage.class, 0, Side.SERVER);
		networkWrapper.registerMessage(NamingPacketHandler.class, NamingMessage.class, 1, Side.SERVER);
		networkWrapper.registerMessage(TeleportEnergyPacketHandler.class, EnergyMessage.class, 2, Side.SERVER);
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, null);
	}
}