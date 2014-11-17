package transportterminal;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeChunkManager;
import transportterminal.blocks.BlockCharger;
import transportterminal.blocks.BlockChipUtilities;
import transportterminal.blocks.BlockTransportTerminal;
import transportterminal.items.ItemTransportTerminalChip;
import transportterminal.items.ItemTransportTerminalPlayerChip;
import transportterminal.items.ItemTransportTerminalRemote;
import transportterminal.network.handler.ChipUtilsPacketHandler;
import transportterminal.network.handler.NamingPacketHandler;
import transportterminal.network.handler.PlayerChipPacketHandler;
import transportterminal.network.handler.TeleportEnergyPacketHandler;
import transportterminal.network.handler.TeleportPacketHandler;
import transportterminal.network.message.ChipUtilsMessage;
import transportterminal.network.message.EnergyMessage;
import transportterminal.network.message.NamingMessage;
import transportterminal.network.message.PlayerChipMessage;
import transportterminal.network.message.TeleportMessage;
import transportterminal.proxy.CommonProxy;
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

@Mod(modid = "transportterminal", name = "Transport Terminals", version = "1.0b")
public class TransportTerminal {

	@Instance("transportterminal")
	public static TransportTerminal instance;

	@SidedProxy(clientSide = "transportterminal.proxy.ClientProxy", serverSide = "transportterminal.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static Item remote, chip, playerChip;
	public static Block terminal, utils, charger;

	public static SimpleNetworkWrapper networkWrapper;

	public static CreativeTabs tab = new CreativeTabs("TransportTerminals") {

		@Override
		public Item getTabIconItem() {
			return TransportTerminal.remote;
		}
	};

	public static boolean IS_RF_PRESENT;

	// add configs for these
	public static int ENERGY_PER_TELEPORT = 10000;
	public static int REMOTE_MAX_ENERGY = 50000;
	public static int TERMINAL_MAX_ENERGY = 320000;
	public static int CHARGER_MAX_ENERGY = 320000;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		IS_RF_PRESENT = ModAPIManager.INSTANCE.hasAPI("CoFHAPI");

		remote = new ItemTransportTerminalRemote().setUnlocalizedName("transportTerminalRemote").setTextureName("transportterminal:transportTerminalRemote");
		chip = new ItemTransportTerminalChip().setUnlocalizedName("transportTerminalChip").setTextureName("transportterminal:transportTerminalChipBlank");
		terminal = new BlockTransportTerminal().setHardness(3.0F).setBlockName("transportTerminal").setBlockTextureName("transportterminal:transportTerminal");
		playerChip = new ItemTransportTerminalPlayerChip().setUnlocalizedName("transportTerminalPlayerChip").setTextureName("transportterminal:transportTerminalPlayerChip");
		utils = new BlockChipUtilities().setHardness(3.0F).setBlockName("transportUtils").setBlockTextureName("transportterminal:transportUtils");
		charger = new BlockCharger().setHardness(3.0F).setBlockName("transportCharger").setBlockTextureName("transportterminal:transportCharger");

		GameRegistry.registerItem(remote, "Transport Terminal Remote");
		GameRegistry.registerItem(chip, "Transport Terminal Chip");
		GameRegistry.registerBlock(terminal, "Transport Terminal");
		GameRegistry.registerItem(playerChip, "Player Location Chip");
		GameRegistry.registerBlock(utils, "Transport Chip Utilities");
		if (IS_RF_PRESENT) // No need for a charger if there's no RF
			GameRegistry.registerBlock(charger, "Transport Charger");

		ModRecipes.addRecipes();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("transportterminal");
		networkWrapper.registerMessage(TeleportPacketHandler.class, TeleportMessage.class, 0, Side.SERVER);
		networkWrapper.registerMessage(NamingPacketHandler.class, NamingMessage.class, 1, Side.SERVER);
		networkWrapper.registerMessage(TeleportEnergyPacketHandler.class, EnergyMessage.class, 2, Side.SERVER);
		networkWrapper.registerMessage(PlayerChipPacketHandler.class, PlayerChipMessage.class, 3, Side.SERVER);
		networkWrapper.registerMessage(ChipUtilsPacketHandler.class, ChipUtilsMessage.class, 4, Side.SERVER);
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, null);
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		proxy.registerTileEntities();
		proxy.registerRenderInformation();
	}
}