package net.fexcraft.lib.mc.utils;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FclConfig {
	
	public static boolean serverSideOnly = false;
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
	
	private static final void refresh(boolean bool){
		if(bool){
			config.load();
			config.setCategoryRequiresMcRestart("Common", true);
			config.setCategoryComment("Common", "Common FCL Settings.");
			config.setCategoryRequiresMcRestart("Statistics", true);
			config.setCategoryComment("Statistics", "Settings about which data get's sent to the FCL Statistic Database.\nConsider to check it out.");
		}
		serverSideOnly = config.getBoolean("ServerSideOnly", "Common", false, "Should client side needed modules be disabled?");
		if(config.hasChanged()){ config.save(); }
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