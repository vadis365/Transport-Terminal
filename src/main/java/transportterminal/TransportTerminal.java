package transportterminal;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeChunkManager;
import transportterminal.blocks.BlockCharger;
import transportterminal.blocks.BlockChipUtilities;
import transportterminal.blocks.BlockSummoner;
import transportterminal.blocks.BlockTransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.items.ItemRemoteTerminal;
import transportterminal.items.ItemTransportTerminalChip;
import transportterminal.items.ItemTransportTerminalPlayerChip;
import transportterminal.items.ItemTransportTerminalRemote;
import transportterminal.network.handler.ChipUtilsPacketHandler;
import transportterminal.network.handler.ConsolePacketHandler;
import transportterminal.network.handler.NamingPacketHandler;
import transportterminal.network.handler.PlayerSummonPacketHandler;
import transportterminal.network.handler.RemotePacketHandler;
import transportterminal.network.message.ButtonMessage;
import transportterminal.network.message.ChipUtilsMessage;
import transportterminal.network.message.NamingMessage;
import transportterminal.network.message.PlayerSummonMessage;
import transportterminal.network.message.TeleportMessage;
import transportterminal.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "transportterminal", name = "Transport Terminals", version = "1.0b", guiFactory = "transportterminal.core.confighandler.ConfigGuiFactory")
public class TransportTerminal {

	@Instance("transportterminal")
	public static TransportTerminal instance;

	@SidedProxy(clientSide = "transportterminal.proxy.ClientProxy", serverSide = "transportterminal.proxy.CommonProxy")
	public static CommonProxy proxy;
	public static Item remote, remoteTerminal, chip, playerChip;
	public static Block terminal, utils, charger, summoner;
	public static SimpleNetworkWrapper networkWrapper;
	public static CreativeTabs tab = new CreativeTabs("TransportTerminals") {

		@Override
		public Item getTabIconItem() {
			return TransportTerminal.remote;
		}
	};

	public static boolean IS_RF_PRESENT;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		IS_RF_PRESENT = ModAPIManager.INSTANCE.hasAPI("CoFHAPI");
		ConfigHandler.INSTANCE.loadConfig(event);

		remote = new ItemTransportTerminalRemote().setUnlocalizedName("transportTerminalRemote").setTextureName("transportterminal:transportTerminalRemote");
		remoteTerminal = new ItemRemoteTerminal().setUnlocalizedName("transportTerminalRemoteTerminal").setTextureName("transportterminal:transportRemoteTerminal");
		chip = new ItemTransportTerminalChip().setUnlocalizedName("transportTerminalChip").setTextureName("transportterminal:transportTerminalChipBlank");
		terminal = new BlockTransportTerminal().setHardness(3.0F).setBlockName("transportTerminal").setBlockTextureName("transportterminal:transportTerminal");
		playerChip = new ItemTransportTerminalPlayerChip().setUnlocalizedName("transportTerminalPlayerChip").setTextureName("transportterminal:transportTerminalPlayerChip");
		utils = new BlockChipUtilities().setHardness(3.0F).setBlockName("transportUtils").setBlockTextureName("transportterminal:transportUtils");
		charger = new BlockCharger().setHardness(3.0F).setBlockName("transportCharger").setBlockTextureName("transportterminal:transportCharger");
		summoner = new BlockSummoner().setHardness(3.0F).setBlockName("transportSummoner").setBlockTextureName("transportterminal:transportSummoner");

		GameRegistry.registerItem(remote, "Transport Terminal Remote");
		GameRegistry.registerItem(remoteTerminal, "Transport Terminal Interface");
		GameRegistry.registerItem(chip, "Transport Terminal Chip");
		GameRegistry.registerBlock(terminal, "Transport Terminal");
		GameRegistry.registerItem(playerChip, "Player Location Chip");
		GameRegistry.registerBlock(utils, "Transport Chip Utilities");
		GameRegistry.registerBlock(summoner, "Player Summoner");
		if (IS_RF_PRESENT) // No need for a charger if there's no RF
			GameRegistry.registerBlock(charger, "Transport Charger");
			
		ModRecipes.addRecipes();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("transportterminal");
		networkWrapper.registerMessage(RemotePacketHandler.class, TeleportMessage.class, 0, Side.SERVER);
		networkWrapper.registerMessage(NamingPacketHandler.class, NamingMessage.class, 1, Side.SERVER);
		networkWrapper.registerMessage(ChipUtilsPacketHandler.class, ChipUtilsMessage.class, 2, Side.SERVER);
		networkWrapper.registerMessage(ConsolePacketHandler.class, ButtonMessage.class, 3, Side.SERVER);
		networkWrapper.registerMessage(PlayerSummonPacketHandler.class, PlayerSummonMessage.class, 4, Side.SERVER);
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, null);
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		proxy.registerTileEntities();
		proxy.registerRenderInformation();
		FMLCommonHandler.instance().bus().register(ConfigHandler.INSTANCE);
	}
}