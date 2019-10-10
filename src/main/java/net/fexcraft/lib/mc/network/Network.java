package net.fexcraft.lib.mc.network;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.api.EnvType;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.utils.HttpUtil;
import net.fexcraft.lib.mc.FCL;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Identifier;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Network{
	
	private static boolean fcl_version_checked = false;

	/** Checks if connection (to main server) is available. */
	@Deprecated
	public static boolean isConnected(){
		try{
			URL url = new URL("http://fexcraft.net/index");
			url.openConnection().connect();
			return true;
		}
		catch(IOException e){
			return false;
		}
	}
	
	public static JsonObject getModData(String modid){
		return getModData(modid, null);
	}
	
	public static JsonObject getModData(String modid, String current_version){
		JsonObject obj = HttpUtil.request("http://fexcraft.net/minecraft/fcl/request", "mode=requestdata&modid=" + modid, 1000);
		if(obj == null){
			return null;
		}
		if(obj.has("blocked_versions") && current_version != null){
			ArrayList<String> arr = new ArrayList<String>();
			for(JsonElement elm : obj.get("blocked_versions").getAsJsonArray()){
				arr.add(elm.getAsString());
			}
			ArrayList<String> array = new ArrayList<String>();
			for(String s : arr){
				Identifier rs = new Identifier(s);
				if(rs.getNamespace().equals(FCL.mcv)){
					array.add(rs.getPath());
				}
			}
			for(String s : array){
				if(s.equals(current_version)){
					Print.log("THIS VERSION OF " + modid.toUpperCase() + " IS BLOCKED/REMOVED;");
					Static.halt(1);
					break;
				}
			}
		}
		else if(obj.has("blocked_versions") && current_version == null && !fcl_version_checked){
			JsonObject fcl = HttpUtil.request("http://fexcraft.net/minecraft/fcl/request", "mode=requestdata&modid=fcl", 1000);
			ArrayList<String> arr = new ArrayList<String>();
			if(fcl.has("blocked_versions") && fcl.get("blocked_versions").isJsonArray()){
				for(JsonElement elm : fcl.get("blocked_versions").getAsJsonArray()){
					arr.add(elm.getAsString());
				}
				ArrayList<String> array = new ArrayList<String>();
				for(String s : arr){
					Identifier rs = new Identifier(s);
					if(rs.getNamespace().equals(FCL.mcv)){
						array.add(s);
					}
				}
				for(String s : array){
					if(s.equals(current_version)){
						Print.log("THIS VERSION OF " + modid.toUpperCase() + " IS BLOCKED/REMOVED;");
						Static.halt(1);
						break;
					}
				}
			}
			fcl_version_checked = true;
		}
		return obj;
	}
	
	public static boolean isModRegistered(String modid){
		try{
			JsonObject obj = HttpUtil.request("http://fexcraft.net/minecraft/fcl/request", "mode=exists&modid=" + modid, 1000);
			return obj == null ? false : obj.get("exists").getAsBoolean();
		}
		catch(Exception e){
			return false;
		}
	}
	
	public static boolean isBanned(UUID id){
		return ServerValidator.isBanned(id);
	}

	public static MinecraftServer getMinecraftServer(){
		return net.fexcraft.lib.mc.utils.Static.getServer();
	}
	
	public static void initializeValidator(EnvType side){
		if(side == EnvType.SERVER){
			ServerValidator.initialize();
		}
		else{
			ClientValidator.initialize();
		}
	}
	
	public static class ClientValidator {

		public static void initialize(){
			String uuid = net.minecraft.client.MinecraftClient.getInstance().getSession().getUuid();
			JsonObject elm = HttpUtil.request("http://fexcraft.net/minecraft/fcl/request", "mode=blacklist&id=" + uuid, 1000);
			if(elm != null && elm.has("unbanned") && !elm.get("unbanned").getAsBoolean()){
				Static.halt(0);
			}
		}
		
	}
	
	public static class ServerValidator {
		
		private static final Set<UUID> blist = new TreeSet<UUID>();

		public static void initialize(){
			JsonObject check = HttpUtil.request("http://fexcraft.net/minecraft/fcl/request", "mode=blacklist&id=server", 1000);
			if(check == null){
				Print.log("Couldn't validate Server.");
			}
			if(check != null && check.has("unbanned") && !check.get("unbanned").getAsBoolean()){
				Print.log("ERROR, SERVER IS BLACKLISTED;");
				Print.log("CONTACT FEXCRAFT.NET STAFF IF YOU THINK IS AN ERROR;");
				Static.halt(0);
			}
			//
			JsonObject obj = HttpUtil.request("http://fexcraft.net/minecraft/fcl/request", "mode=blacklist");
			if(obj == null){
				Print.log("Couldn't retrieve BL.");
				return;
			}
			for(JsonElement elm : obj.get("blacklist").getAsJsonArray()){
				try{
					blist.add(UUID.fromString(elm.getAsString()));
				}
				catch(Exception e){
					//Print.debug("[BL] Couldn't parse " + elm.toString() + ".");
				}
			}
		}
		
		public static boolean isBanned(UUID id){
			return blist.contains(id);
		}
		
	}
	
	public static void browse(CommandOutput sender, String url){
    	try{ Desktop.getDesktop().browse(new URI(url)); }
    	catch(IOException | URISyntaxException e){
			Print.chat(sender, FCL.prefix + "Error, couldn't open link.");
			e.printStackTrace();
		}
	}
	
}