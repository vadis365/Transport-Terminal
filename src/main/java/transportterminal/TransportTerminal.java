package transportterminal;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.core.reference.Reference;
import transportterminal.network.handler.ChipUtilsPacketHandler;
import transportterminal.network.handler.ConsolePacketHandler;
import transportterminal.network.handler.ContainerPacketHandler;
import transportterminal.network.handler.EnergyCubePacketHandler;
import transportterminal.network.handler.GeneratorPacketHandler;
import transportterminal.network.handler.ItemTransporterPacketHandler;
import transportterminal.network.handler.NamingPacketHandler;
import transportterminal.network.handler.PlayerSummonPacketHandler;
import transportterminal.network.handler.RemotePacketHandler;
import transportterminal.network.message.ButtonMessage;
import transportterminal.network.message.ChipUtilsMessage;
import transportterminal.network.message.ContainerMessage;
import transportterminal.network.message.EnergyCubeMessage;
import transportterminal.network.message.GeneratorMessage;
import transportterminal.network.message.ItemTransporterMessage;
import transportterminal.network.message.NamingMessage;
import transportterminal.network.message.PlayerSummonMessage;
import transportterminal.network.message.TeleportMessage;
import transportterminal.proxy.CommonProxy;
import transportterminal.utils.DimensionUtils;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY)
public class TransportTerminal {

	@Instance(Reference.MOD_ID)
	public static TransportTerminal INSTANCE;

	@SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
	public static CommonProxy PROXY;
	
	public static SimpleNetworkWrapper NETWORK_WRAPPER;

	public static CreativeTabs tab = new CreativeTabs(Reference.MOD_NAME) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.REMOTE);
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.INSTANCE.loadConfig(event);
		ModBlocks.init();
		ModItems.init();
		ModSounds.init();
		ModRecipes.init();

		PROXY.registerTileEntities();
		PROXY.registerRenderInformation();

		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, PROXY);
		NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("transportterminal");
		NETWORK_WRAPPER.registerMessage(RemotePacketHandler.class, TeleportMessage.class, 0, Side.SERVER);
		NETWORK_WRAPPER.registerMessage(NamingPacketHandler.class, NamingMessage.class, 1, Side.SERVER);
		NETWORK_WRAPPER.registerMessage(ChipUtilsPacketHandler.class, ChipUtilsMessage.class, 2, Side.SERVER);
		NETWORK_WRAPPER.registerMessage(ConsolePacketHandler.class, ButtonMessage.class, 3, Side.SERVER);
		NETWORK_WRAPPER.registerMessage(PlayerSummonPacketHandler.class, PlayerSummonMessage.class, 4, Side.SERVER);
		NETWORK_WRAPPER.registerMessage(ContainerPacketHandler.class, ContainerMessage.class, 5, Side.CLIENT);
		NETWORK_WRAPPER.registerMessage(EnergyCubePacketHandler.class, EnergyCubeMessage.class, 6, Side.SERVER);
		NETWORK_WRAPPER.registerMessage(GeneratorPacketHandler.class, GeneratorMessage.class, 7, Side.SERVER);
		NETWORK_WRAPPER.registerMessage(ItemTransporterPacketHandler.class, ItemTransporterMessage.class, 8, Side.SERVER);

		ForgeChunkManager.setForcedChunkLoadingCallback(INSTANCE, new DimensionUtils());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(ConfigHandler.INSTANCE);
	}
}