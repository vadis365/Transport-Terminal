package transportterminal.core.confighandler;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler {

	public static final ConfigHandler INSTANCE = new ConfigHandler();
	public Configuration config;
	public static int ENERGY_PER_TELEPORT;
	public static int REMOTE_MAX_ENERGY;
	public static int TERMINAL_MAX_ENERGY;
	public static int CHARGER_MAX_ENERGY;
	public static boolean ALLOW_TELEPORT_TO_PLAYER;
	public final String[] usedCategories = { "RF Energy Settings", "Game Settings" };

	public void loadConfig(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		syncConfigs();
	}

	private void syncConfigs() {
		ENERGY_PER_TELEPORT = config.get("RF Energy Settings", "RF Used to Teleport", 10000).getInt(10000);
		REMOTE_MAX_ENERGY = config.get("RF Energy Settings", "Max RF Stored on Remotes", 50000).getInt(50000);
		TERMINAL_MAX_ENERGY = config.get("RF Energy Settings", "Max RF Stored in Consoles", 320000).getInt(320000);
		CHARGER_MAX_ENERGY = config.get("RF Energy Settings", "Max RF Stored in Charger", 320000).getInt(320000);
		ALLOW_TELEPORT_TO_PLAYER = config.get("Game Settings", "Enable Player Location Chips", true).getBoolean(true);
		if (config.hasChanged())
			config.save();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals("transportterminal"))
			syncConfigs();
	}
}