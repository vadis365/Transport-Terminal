package transportterminal.core.confighandler;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler {

	public static final ConfigHandler INSTANCE = new ConfigHandler();
	public Configuration config;
	public static int ENERGY_PER_TELEPORT;
	public static int REMOTE_MAX_ENERGY;
	public static int REMOTE_TERMINAL_MAX_ENERGY;
	public static int TERMINAL_MAX_ENERGY;
	public static int CHARGER_MAX_ENERGY;
	public static int SUMMONER_MAX_ENERGY;
	public static int ENERGY_CUBE_MAX_ENERGY;
	public static int GENERATOR_MAX_ENERGY;
	public static int GENERATOR_PROCESSING_TIME;
	public static int GENERATOR_MAX_EXTRACT_PER_TICK;
	public static boolean ALLOW_TELEPORT_TO_PLAYER;
	public static boolean ALLOW_TELEPORT_SUMMON_PLAYER;
	public final String[] usedCategories = { "RF Energy Settings", "Game Settings" };

	public void loadConfig(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		syncConfigs();
	}

	private void syncConfigs() {
		ENERGY_PER_TELEPORT = config.get("RF Energy Settings", "RF Used to Teleport", 10000).getInt(10000);
		REMOTE_MAX_ENERGY = config.get("RF Energy Settings", "Max RF Stored on Remotes", 50000).getInt(50000);
		REMOTE_TERMINAL_MAX_ENERGY = config.get("RF Energy Settings", "Max RF Stored on Wireless Terminal Interface", 50000).getInt(50000);
		TERMINAL_MAX_ENERGY = config.get("RF Energy Settings", "Max RF Stored in Consoles", 320000).getInt(320000);
		CHARGER_MAX_ENERGY = config.get("RF Energy Settings", "Max RF Stored in Charger", 320000).getInt(320000);
		SUMMONER_MAX_ENERGY = config.get("RF Energy Settings", "Max RF Stored in Player Summoner", 320000).getInt(320000);
		ENERGY_CUBE_MAX_ENERGY = config.get("RF Energy Settings", "Max RF Stored in Energy Cube", 1280000).getInt(1280000);
		GENERATOR_MAX_ENERGY = config.get("RF Energy Settings", "Max RF Stored in Generator", 1280000).getInt(1280000);
		GENERATOR_PROCESSING_TIME = config.get("RF Energy Settings", "Generator Redstone to RF processing time (in ticks)", 40).getInt(40);
		GENERATOR_MAX_EXTRACT_PER_TICK = config.get("RF Energy Settings", "Generator Max RF Extract per tick", 100).getInt(100);
		ALLOW_TELEPORT_TO_PLAYER = config.get("Game Settings", "Enable Player Location Chips", true).getBoolean(true);
		ALLOW_TELEPORT_SUMMON_PLAYER = config.get("Game Settings", "Enable Player Summoning Block", true).getBoolean(true);
		
		if (config.hasChanged())
			config.save();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals("transportterminal"))
			syncConfigs();
	}
}