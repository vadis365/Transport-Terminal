package transportterminal;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
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
import transportterminal.network.handler.NamingPacketHandler;
import transportterminal.network.handler.PlayerChipPacketHandler;
import transportterminal.network.handler.PlayerSummonPacketHandler;
import transportterminal.network.handler.ShadowTeleportPacketHandler;
import transportterminal.network.handler.TeleportEnergyPacketHandler;
import transportterminal.network.handler.TeleportPacketHandler;
import transportterminal.network.message.ButtonMessage;
import transportterminal.network.message.ChipUtilsMessage;
import transportterminal.network.message.EnergyMessage;
import transportterminal.network.message.NamingMessage;
import transportterminal.network.message.PlayerChipMessage;
import transportterminal.network.message.PlayerSummonMessage;
import transportterminal.network.message.TeleportMessage;
import transportterminal.proxy.CommonProxy;

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
		IS_RF_PRESENT = false;//ModAPIManager.INSTANCE.hasAPI("CoFHAPI");
		ConfigHandler.INSTANCE.loadConfig(event);

		// Items
		remote = new ItemTransportTerminalRemote().setUnlocalizedName("remote");
		remoteTerminal = new ItemRemoteTerminal().setUnlocalizedName("remoteTerminal");
		chip = new ItemTransportTerminalChip().setUnlocalizedName("chip");
		playerChip = new ItemTransportTerminalPlayerChip().setUnlocalizedName("playerChip");

		// Blocks
		terminal = new BlockTransportTerminal().setHardness(3.0F).setUnlocalizedName("console");
		utils = new BlockChipUtilities().setHardness(3.0F).setUnlocalizedName("utils");
		charger = new BlockCharger().setHardness(3.0F).setUnlocalizedName("charger");
		summoner = new BlockSummoner().setHardness(3.0F).setUnlocalizedName("summoner");

		GameRegistry.registerItem(remote, "remote");
		GameRegistry.registerItem(remoteTerminal, "remoteTerminal");
		GameRegistry.registerItem(chip, "chip");
		GameRegistry.registerBlock(terminal, "console");
		GameRegistry.registerItem(playerChip, "playerChip");
		GameRegistry.registerBlock(utils, "utils");
		GameRegistry.registerBlock(summoner, "summoner");
		if (IS_RF_PRESENT) // No need for a charger if there's no RF
			GameRegistry.registerBlock(charger, "charger");

		ModRecipes.addRecipes();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("transportterminal");
		networkWrapper.registerMessage(TeleportPacketHandler.class, TeleportMessage.class, 0, Side.SERVER);
		networkWrapper.registerMessage(NamingPacketHandler.class, NamingMessage.class, 1, Side.SERVER);
		networkWrapper.registerMessage(TeleportEnergyPacketHandler.class, EnergyMessage.class, 2, Side.SERVER);
		networkWrapper.registerMessage(PlayerChipPacketHandler.class, PlayerChipMessage.class, 3, Side.SERVER);
		networkWrapper.registerMessage(ChipUtilsPacketHandler.class, ChipUtilsMessage.class, 4, Side.SERVER);
		networkWrapper.registerMessage(ShadowTeleportPacketHandler.class, ButtonMessage.class, 5, Side.SERVER);
		networkWrapper.registerMessage(PlayerSummonPacketHandler.class, PlayerSummonMessage.class, 6, Side.SERVER);
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, null);
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		proxy.registerTileEntities();
		proxy.registerRenderInformation();
		FMLCommonHandler.instance().bus().register(ConfigHandler.INSTANCE);
	}
}