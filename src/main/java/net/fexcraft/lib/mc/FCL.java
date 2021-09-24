package net.fexcraft.lib.mc;

import java.io.File;
import java.util.UUID;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.mc.network.Network;
import net.fexcraft.lib.mc.network.SimpleUpdateHandler;
import net.fexcraft.lib.mc.utils.JsonConfig;
import net.minecraft.util.Formatting;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 * 
 * @description This Library is usually delivered as an own jar,
 * if you got it from anywhere else then Fexcraft.net or another <i>official</i> download site,
 * consider deleting it instantly for security reasons!
 * 
 */
public class FCL implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
	
	public static final String prefix = Formatting.BLACK + "[" + Formatting.DARK_AQUA + "FCL" + Formatting.BLACK + "]" + Formatting.GRAY + " ";
	public static final String version = "1.12.68";
	public static final String mcv = "1.17.1";
	public static final UUID[] authors = new UUID[]{ UUID.fromString("01e4af9b-2a30-471e-addf-f6338ffce04b") };
	public static boolean CLIENT = false;
	public static JsonConfig CONFIG;
	private static FCL instance;
	
	@Override
	public void onInitialize(){
		instance = this;
		System.out.println("[FCL] Initializing.");
		Static.setAsMcLib(true);
		Static.setDevmode(false);//TODO
		CONFIG = new JsonConfig(new File("./config/fcl.json")).load();
		Network.checkConfig();
		//TODO register new SimpleUpdateHandler.EventHandler();
		//TODO register guis
		//
		SimpleUpdateHandler.register("fcl", "1", version);
		SimpleUpdateHandler.setUpdateMessage("fcl", prefix + "Update available! (" + SimpleUpdateHandler.getLatestVersionOf("fcl") + ")");
		if(CONFIG.getValue("check-for-updates", true, "Should the FCL update handler check for mod updates?")){
			SimpleUpdateHandler.init();//TODO find a later call hook
		}
		//
		//TODO init packets
		//TODO register fcl gui packet handler
		System.out.println("[FCL] Loading complete.");
	}
	
	@Override
	public void onInitializeClient(){
		System.out.println("[FCL] Initializing (CLIENT).");
		CLIENT = true;
		//TODO register custom model loaders, if existent
		//TODO register fcl gui packet handler
	}
	
	@Override
	public void onInitializeServer(){
		System.out.println("[FCL] Initializing (SERVER).");
		//
	}
	
	public static FCL getInstance(){
		return instance;
	}

	public static final String getVersion(){
		return version;
	}

	public static final String getMinecraftVersion(){
		return mcv;
	}
	
}