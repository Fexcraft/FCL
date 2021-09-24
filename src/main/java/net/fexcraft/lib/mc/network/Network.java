package net.fexcraft.lib.mc.network;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.utils.HttpUtil;
import net.fexcraft.lib.mc.FCL;
import net.fexcraft.lib.mc.utils.Formatter;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Network {
	
	private static boolean fcl_version_checked = false;
	private static boolean check_notifications = false;

	public static void checkConfig(){
		check_notifications = FCL.CONFIG.getValue("notifications", false, "Should FCL check for notifications from fexcraft.net? This is UUID based, you may need to configure your fexcraft.net account for this to work.");
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
					Print.log("THIS VERSION OF " + modid.toUpperCase() + " IS BLOCKED/REMOVED, PLEASE UPDATE;");
					Static.halt(1);
					break;
				}
			}
		}
		else if(obj.has("blocked_versions") && current_version == null && !fcl_version_checked){
			JsonObject fcl = HttpUtil.request("http://fexcraft.net/minecraft/fcl/request", "mode=requestdata&modid=fcl", 1000);
			ArrayList<String> arr = new ArrayList<String>();
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
					Print.log("THIS VERSION OF " + modid.toUpperCase() + " IS BLOCKED/REMOVED, PLEASE UPDATE;");
					Static.halt(1);
					break;
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
	
	public static void browse(PlayerEntity sender, String url){
    	try{
    		Desktop.getDesktop().browse(new URI(url));
    	}
    	catch(IOException | URISyntaxException e){
			Print.chat(sender, FCL.prefix + "Error, couldn't open link.");
			e.printStackTrace();
		}
	}

	public static void checkStatus(){
		if(!check_notifications || !FCL.CLIENT) return;
		try{
			String id = MinecraftClient.getInstance().getSession().getUuid();
			JsonObject obj = HttpUtil.request("https://fexcraft.net/minecraft/fcl/request", "mode=status&id=" + id, 1000);
			if(obj == null || obj.entrySet().isEmpty()) return;
			if(obj.has("notify")){
				JsonArray notes = obj.get("notify").getAsJsonArray();
				notifications = new String[notes.size()];
				for(int i = 0; i < notes.size(); i++){
					if(notes.get(i).isJsonPrimitive()){
						notifications[i] = notes.get(i).getAsString();
					}
					else{
						//TODO if necessary someday, advanced parsing and so on.
					}
				}
			}
			status_data = obj;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static String[] notifications;
	private static JsonObject status_data;

	public static boolean anyNewNotifications(){
		return check_notifications && notifications != null && notifications.length > 0;
	}

	public static void notify(PlayerEntity player){
		for(String str : notifications){
			Print.chat(player, Formatter.format(str));
		}
	}
	
	public JsonObject getStatusJson(){
		return status_data;
	}
	
}