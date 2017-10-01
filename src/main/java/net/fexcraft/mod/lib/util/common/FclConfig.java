package net.fexcraft.mod.lib.util.common;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FclConfig {
	
	public static boolean serverSideOnly = false;
	//
	public static boolean uuid_logging;
	public static boolean remove_from_db;
	public static boolean private_server;
	public static boolean remind;
	private static Configuration config;

	public static final void initalize(FMLPreInitializationEvent event, File file) {
		config = new Configuration(file, "1.0", true);
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		refresh(true);
	}

	public static final Configuration getConfig(){
		return config;
	}
	
	public static final void refresh(){
		refresh(false);
	}
	
	private static final void refresh(boolean f){
		if(f){
			config.load();
			config.setCategoryRequiresMcRestart("Common", true);
			config.setCategoryComment("Common", "Common FCL Settings.");
			config.setCategoryRequiresMcRestart("Statistics", true);
			config.setCategoryComment("Statistics", "Settings about which data get's sent to the FCL Statistic Database.\nConsider to check it out.");
		}
		uuid_logging = config.getBoolean("UUID Logging", "Statistics", true, "Set to 'false' if you don't want your UUID to be sent together with Statistical data.");
		remove_from_db = config.getBoolean("Remove from Database", "Statistics", false, "Set to 'true' if you want all data regarding your UUID to be removed from the Statistics Database.");
		private_server = config.getBoolean("Private Server", "Statistics", false, "Set to 'true' if you don't want your Server to be logged into the Statistics data.");
		remind = config.getBoolean("Remind", "Common", true, "Set to 'false' if you do not want to be reminded to check the config.");
		serverSideOnly = config.getBoolean("ServerSideOnly", "Common", false, "Should client side needed modules be disabled?");
		if(config.hasChanged()){
			config.save();
		}
	}
	
	public static class EventHandler {
		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
			if(event.getModID().equals("fcl")){
				refresh();
			}
		}
	}
	
}