package net.fexcraft.mod.lib.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fexcraft.mod.lib.FCL;
import net.fexcraft.mod.lib.util.common.FclConfig;
import net.fexcraft.mod.lib.util.common.Print;
import net.fexcraft.mod.lib.util.common.Static;
import net.fexcraft.mod.lib.util.json.JsonUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Network{
	
	private static boolean fcl_version_checked = false;

	/** Checks if connection (to main server) is available. */
	public static boolean isConnected(){
		try{
			URL url = new URL("http://www.fexcraft.net/files/TXT/connection.test");
			url.openConnection().connect();
			return true;
		}
		catch(IOException e){
			return false;
		}
	}
	
	/** Requests a JsonObject from the given adress and parameters, using the POST HTML method. */
	public static JsonObject request(String adress, String parameters){
		/*if(!isConnected()){
			Static.stop();
			return null;
		}*/
		try{
			URL url = new URL(adress);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("User-Agent", "Mozilla/5.0");
				connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				connection.setDoOutput(true);
				
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(parameters);
				wr.flush();
				wr.close();
			
			JsonObject obj = JsonUtil.getObjectFromInputStream(connection.getInputStream()).getAsJsonObject();	
			
			connection.disconnect();
			return obj;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static JsonElement request(String adress){
		try{
			URL url = new URL(adress);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("User-Agent", "Mozilla/5.0");
				connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			JsonElement obj = JsonUtil.getElementFromInputStream(connection.getInputStream());	
			
			connection.disconnect();
			return obj;
		}
		catch(Exception e){
			if(!Static.dev()){
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public static JsonObject getModData(String modid){
		return getModData(modid, null);
	}
	
	public static JsonObject getModData(String modid, String current_version){
		JsonObject obj = request("http://fexcraft.net/minecraft/fcl/request", "mode=requestdata&modid=" + modid);
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
				ResourceLocation rs = new ResourceLocation(s);
				if(rs.getResourceDomain().equals(FCL.mcv)){
					array.add(rs.getResourcePath());
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
			JsonObject fcl = request("http://fexcraft.net/minecraft/fcl/request", "mode=requestdata&modid=fcl");
			ArrayList<String> arr = new ArrayList<String>();
			for(JsonElement elm : fcl.get("blocked_versions").getAsJsonArray()){
				arr.add(elm.getAsString());
			}
			ArrayList<String> array = new ArrayList<String>();
			for(String s : arr){
				ResourceLocation rs = new ResourceLocation(s);
				if(rs.getResourceDomain().equals(FCL.mcv)){
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
			fcl_version_checked = true;
		}
		return obj;
	}
	
	public static boolean isModRegistered(String modid){
		try{
			return request("http://fexcraft.net/minecraft/fcl/request", "mode=exists&modid=" + modid).get("exists").getAsBoolean();
		}
		catch(Exception e){
			return false;
		}
	}
	
	public static boolean isDonator(UUID id){
		return Static.side().isClient() ? ClientValidator.isDonator() : ServerValidator.isDonator(id);
	}
	
	public static boolean isBanned(UUID id){
		return ServerValidator.isBanned(id);
	}

	public static boolean isDonator(EntityPlayer p){
		return isDonator(p.getGameProfile().getId());
	}

	public static MinecraftServer getMinecraftServer(){
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}
	
	public static void initializeValidator(Side side){
		if(side.isServer()){
			ServerValidator.initialize();
		}
		else{
			ClientValidator.initialize();
		}
	}
	
	public static class ClientValidator {
		
		private static boolean checked;

		public static void initialize(){
			String uuid = net.minecraft.client.Minecraft.getMinecraft().getSession().getPlayerID();
			JsonObject elm = Network.request("http://fexcraft.net/minecraft/fcl/request", "mode=blacklist&id=" + uuid);
			if(elm != null && elm.has("unbanned") && !elm.get("unbanned").getAsBoolean()){
				Static.halt(0);
			}
			//
			JsonObject jsn = Network.request("http://fexcraft.net/minecraft/fcl/request", "mode=isDonor&id=" + uuid);
			checked = jsn == null ? false : !jsn.get("expired").getAsBoolean();
			//
			String parameters = "mode=logClient";
			parameters += "&version=" + FCL.mcv + ":" + FCL.version;
			if(Static.dev()){
				JsonObject obj = new JsonObject();
				obj.addProperty("type", "developement_workspace");
				obj.addProperty("uuid", Static.DEF1_UUID_STRING);
				obj.addProperty("settings", "workspace_mode");
				parameters += "&data=" + obj.toString();
			}
			else{
				JsonObject obj = new JsonObject();
				obj.addProperty("type", "client_launch");
				if(FclConfig.uuid_logging){
					obj.addProperty("uuid", uuid);
					obj.addProperty("settings", "enabled");
				}
				else{
					obj.addProperty("uuid", Static.NULL_UUID_STRING);
					obj.addProperty("settings", FclConfig.remove_from_db ? "remove" : "disabled");
				}
				parameters += "&data=" + obj.toString();
			}
			JsonObject object = Network.request("http://fexcraft.net/minecraft/fcl/request", parameters);
			if(object != null){
				Print.debug("C: " + object);
				return;
			}
		}

		public static boolean isDonator(){
			return checked;
		}
		
	}
	
	public static class ServerValidator {
		
		private static final Set<UUID> blist = new TreeSet<UUID>();
		private static final Map<UUID, Boolean> ls = new TreeMap<UUID, Boolean>();

		public static void initialize(){
			JsonObject check = Network.request("http://fexcraft.net/minecraft/fcl/request", "mode=blacklist&id=server");
			if(check == null){
				Print.log("Couldn't validate Server.");
			}
			if(check != null && check.has("unbanned") && !check.get("unbanned").getAsBoolean()){
				Print.log("ERROR, SERVER IS BLACKLISTED;");
				Print.log("CONTACT FEXCRAFT.NET STAFF IF YOU THINK IS AN ERROR;");
				Static.halt(0);
			}
			//
			JsonObject obj = Network.request("http://fexcraft.net/minecraft/fcl/request", "mode=blacklist");
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
			//
			if(!FclConfig.private_server){
				String parameters = "mode=logServer";
				parameters += "&hostname=" + Network.getMinecraftServer().getServerHostname();
				parameters += "&port=" + Network.getMinecraftServer().getServerPort();
				parameters += "&motd=" + Network.getMinecraftServer().getMOTD();
				parameters += "&version=" + FCL.mcv + ":" + FCL.version;
				parameters += "&data={}";// + getModList().toString().replaceAll("'", "`").replaceAll("&", "[and]");
				JsonObject object = Network.request("http://fexcraft.net/minecraft/fcl/request", parameters);
				if(object != null){
					Print.debug("S: " + object);
					return;
				}
			}
			else{
				String parameters = "mode=logServer";
				parameters += "&port=" + Network.getMinecraftServer().getServerPort();
				parameters += "&data=REMOVE";
				JsonObject object = Network.request("http://fexcraft.net/minecraft/fcl/request", parameters);
				if(object != null){
					Print.debug("S: " + object);
					return;
				}
			}
		}
		
		public static boolean isBanned(UUID id){
			return blist.contains(id);
		}

		private static JsonObject getModList(){
			JsonObject object = new JsonObject();
			Map<String, ModContainer> map = Loader.instance().getIndexedModList();
			JsonArray array = new JsonArray();
			for(ModContainer mod : map.values()){
				JsonObject obj = new JsonObject();
				obj.addProperty("modid", mod.getModId());
				obj.addProperty("name", mod.getName());
				obj.addProperty("version", mod.getVersion());
				array.add(obj);
			}
			object.add("mods", array);
			return object;
		}

		public static boolean isDonator(UUID id){
			if(!ls.containsKey(id)){
				JsonObject jsn = Network.request("http://fexcraft.net/minecraft/fcl/request", "mode=isDonor&id=" + id.toString());
				ls.put(id, (jsn != null && jsn.has("expired") && !jsn.get("expired").getAsBoolean()));
			}
			return ls.get(id);
		}
		
	}
	
}