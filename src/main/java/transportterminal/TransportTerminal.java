package transportterminal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.blocks.BlockCharger;
import transportterminal.blocks.BlockChipUtilities;
import transportterminal.blocks.BlockEnergyCube;
import transportterminal.blocks.BlockGenerator;
import transportterminal.blocks.BlockSummoner;
import transportterminal.blocks.BlockTransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.items.ItemChip;
import transportterminal.items.ItemPlayerChip;
import transportterminal.items.ItemRemote;
import transportterminal.items.ItemRemoteTerminal;
import transportterminal.items.ItemSpeedUpgradeChip;
import transportterminal.network.handler.ChipUtilsPacketHandler;
import transportterminal.network.handler.ConsolePacketHandler;
import transportterminal.network.handler.ContainerPacketHandler;
import transportterminal.network.handler.EnergyCubePacketHandler;
import transportterminal.network.handler.GeneratorPacketHandler;
import transportterminal.network.handler.NamingPacketHandler;
import transportterminal.network.handler.PlayerSummonPacketHandler;
import transportterminal.network.handler.RemotePacketHandler;
import transportterminal.network.message.ButtonMessage;
import transportterminal.network.message.ChipUtilsMessage;
import transportterminal.network.message.ContainerMessage;
import transportterminal.network.message.EnergyCubeMessage;
import transportterminal.network.message.GeneratorMessage;
import transportterminal.network.message.NamingMessage;
import transportterminal.network.message.PlayerSummonMessage;
import transportterminal.network.message.TeleportMessage;
import transportterminal.proxy.CommonProxy;

@Mod(modid = "transportterminal", name = "transportterminal", version = "1.0.1", guiFactory = "transportterminal.core.confighandler.ConfigGuiFactory")
public class TransportTerminal {

	@Instance("transportterminal")
	public static TransportTerminal INSTANCE;

	@SidedProxy(clientSide = "transportterminal.proxy.ClientProxy", serverSide = "transportterminal.proxy.CommonProxy")
	public static CommonProxy PROXY;
	public static Item REMOTE, REMOTE_TERMINAL, CHIP, PLAYER_CHIP, TERMINAL_ITEM, UTILS_ITEM, CHARGER_ITEM, SUMMONER_ITEM, ENERGY_CUBE_ITEM, GENERATOR_ITEM, UPGRADE_CHIP;
	public static Block TERMINAL, UTILS, CHARGER, SUMMONER, ENERGY_CUBE, GENERATOR;
	public static SimpleNetworkWrapper NETWORK_WRAPPER;
	public static SoundEvent OK_SOUND;
	public static SoundEvent ERROR_SOUND;
	public static SoundEvent TELEPORT_SOUND;

	public static CreativeTabs tab = new CreativeTabs("transportterminal") {
		@Override
		public Item getTabIconItem() {
			return TransportTerminal.REMOTE;
		}
	};

	public static boolean IS_RF_PRESENT;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		IS_RF_PRESENT = ModAPIManager.INSTANCE.hasAPI("CoFHAPI|energy");
		ConfigHandler.INSTANCE.loadConfig(event);

		// Items
		REMOTE = new ItemRemote();
		REMOTE_TERMINAL = new ItemRemoteTerminal();
		CHIP = new ItemChip();
		PLAYER_CHIP = new ItemPlayerChip();
		UPGRADE_CHIP = new ItemSpeedUpgradeChip();

		// Blocks
		TERMINAL = new BlockTransportTerminal().setHardness(3.0F);
		UTILS = new BlockChipUtilities().setHardness(3.0F);
		CHARGER = new BlockCharger().setHardness(3.0F);
		SUMMONER = new BlockSummoner().setHardness(3.0F);
		ENERGY_CUBE = new BlockEnergyCube().setHardness(3.0F);
		GENERATOR = new BlockGenerator().setHardness(3.0F);

		TERMINAL_ITEM = new ItemBlock(TERMINAL) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add("Used to teleport to locations stored on chips.");
				list.add("Can teleport to other players.");
			}
		};

		UTILS_ITEM = new ItemBlock(UTILS) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add("Used to copy, erase and name chips.");
				list.add("Requires no RF to run.");
			}
		};

		SUMMONER_ITEM = new ItemBlock(SUMMONER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add("Used to summon players to it's location.");
				list.add("Must contain a named player location chip to work.");
			}
		};

		CHARGER_ITEM = new ItemBlock(CHARGER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add("Can charge up to 6 RF items at a time.");
			}
		};

		ENERGY_CUBE_ITEM = new ItemBlock(ENERGY_CUBE) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add("Stores and outputs RF.");
				list.add("All sides configurable.");
				list.add("Right Click to open configuration gui or");
				list.add("Sneak + Right Click to toggle side's state.");
			}
		};

		GENERATOR_ITEM = new ItemBlock(GENERATOR) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
				list.add("Uses redstone to provide RF.");
			}
		};

		GameRegistry.register(REMOTE.setRegistryName("transportterminal", "remote").setUnlocalizedName("transportterminal.remote"));
		GameRegistry.register(REMOTE_TERMINAL.setRegistryName("transportterminal", "remoteTerminal").setUnlocalizedName("transportterminal.remoteTerminal"));
		GameRegistry.register(CHIP.setRegistryName("transportterminal", "chip").setUnlocalizedName("transportterminal.chip"));
		GameRegistry.register(PLAYER_CHIP.setRegistryName("transportterminal", "player_chip").setUnlocalizedName("transportterminal.player_chip"));
		GameRegistry.register(UPGRADE_CHIP.setRegistryName("transportterminal", "upgrade_chip").setUnlocalizedName("transportterminal.upgrade_chip"));
		
		GameRegistry.register(TERMINAL.setRegistryName("transportterminal", "console").setUnlocalizedName("transportterminal.console"));
		GameRegistry.register(UTILS.setRegistryName("transportterminal", "utils").setUnlocalizedName("transportterminal.utils"));
		GameRegistry.register(SUMMONER.setRegistryName("transportterminal", "summoner").setUnlocalizedName("transportterminal.summoner"));
		if (IS_RF_PRESENT) // No need for a charger if there's no RF
			GameRegistry.register(CHARGER.setRegistryName("transportterminal", "charger").setUnlocalizedName("transportterminal.charger"));
		GameRegistry.register(ENERGY_CUBE.setRegistryName("transportterminal", "energy_cube").setUnlocalizedName("transportterminal.energy_cube"));
		GameRegistry.register(GENERATOR.setRegistryName("transportterminal", "generator").setUnlocalizedName("transportterminal.generator"));

		GameRegistry.register(TERMINAL_ITEM.setRegistryName(TERMINAL.getRegistryName()).setUnlocalizedName("transportterminal.console"));
		GameRegistry.register(UTILS_ITEM.setRegistryName(UTILS.getRegistryName()).setUnlocalizedName("transportterminal.utils"));
		GameRegistry.register(SUMMONER_ITEM.setRegistryName(SUMMONER.getRegistryName()).setUnlocalizedName("transportterminal.summoner"));
		if (IS_RF_PRESENT) // No need for a charger if there's no RF
			GameRegistry.register(CHARGER_ITEM.setRegistryName(CHARGER.getRegistryName()).setUnlocalizedName("transportterminal.charger"));
		GameRegistry.register(ENERGY_CUBE_ITEM.setRegistryName(ENERGY_CUBE.getRegistryName()).setUnlocalizedName("transportterminal.energy_cube"));
		GameRegistry.register(GENERATOR_ITEM.setRegistryName(GENERATOR.getRegistryName()).setUnlocalizedName("transportterminal.generator"));

		PROXY.registerTileEntities();
		PROXY.registerRenderInformation();

		ModRecipes.addRecipes();

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

		ForgeChunkManager.setForcedChunkLoadingCallback(INSTANCE, null);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		OK_SOUND = new SoundEvent(new ResourceLocation("transportterminal", "oksound")).setRegistryName("transportterminal", "oksound");
		ERROR_SOUND = new SoundEvent(new ResourceLocation("transportterminal", "errorsound")).setRegistryName("transportterminal", "errorsound");
		TELEPORT_SOUND = new SoundEvent(new ResourceLocation("transportterminal", "teleportsound")).setRegistryName("transportterminal", "teleportsound");
		GameRegistry.register(OK_SOUND);
		GameRegistry.register(ERROR_SOUND);
		GameRegistry.register(TELEPORT_SOUND);
		MinecraftForge.EVENT_BUS.register(ConfigHandler.INSTANCE);
	}
}