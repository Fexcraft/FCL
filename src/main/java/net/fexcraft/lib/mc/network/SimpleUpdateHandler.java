package net.fexcraft.lib.mc.network;

import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.lib.mc.FCL;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.lib.mc.utils.Static;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Deprecated
public class SimpleUpdateHandler{
	
	private static JsonObject obj = new JsonObject();
	private static HashSet<String> modids = new HashSet<String>();
	private static HashMap<String, String> update_message_queue = new HashMap<String, String>();
	private static HashMap<String, String> versions = new HashMap<String, String>();
	private static HashSet<String> mods_to_update = new HashSet<String>();
	private static boolean loaded = false;
	
	private static void checkForMissingModData(){
		for(String string : modids){
			if(!obj.has(string)){
				JsonObject data = Network.getModData(string);
				if(data != null){
					obj.add(string, data);
				}
			}
		}
	}
	
	/**
	 * Registers a <i><b>"Simple Update Handler"</b></i><br>
	 * Note that you need to register the mod firstly in the FCL database.<br>
	 * 
	 * @param modid Modid of the Mod.
	 * @param userid Fexcraft.net User ID the mod was registered under
	 * @param current_version Current version of the mod.
	 */
	public static void register(String modid, int userid, String current_version){
		if(Network.isModRegistered(modid)){
			obj.add(modid, Network.getModData(modid));
			modids.add(modid);
			versions.put(modid, current_version);
			return;
		}
		else{
			Print.log("Tried to register an SimpleUpdateHandler for '" + modid + "', but mod seems not to be registered in the FCL database.");
			//Static.stop();
		}
	}

	public static void postInit(){
		new Thread("FCL-SUH"){
			@Override
			public void run(){
				checkForMissingModData();
				checkIfUpdateAvaible();
				loaded = true;
			}
		}.start();
	}
	
	private static void checkIfUpdateAvaible(){
		Print.log("Checking for available updates.");
		for(String modid : modids){
			if(obj.has(modid)){
				String latest_version = getLatestVersionOf(modid);
				if(!latest_version.equals(versions.get(modid))){
					Print.log("Found update for '" + modid + "'!");
					mods_to_update.add(modid);
				}
			}
		}
		Print.log("Done checking for updates.");
	}
	
	/** 
	 * Used to set the Update message after SUH registration.<br>
	 */
	public static void setUpdateMessage(String modid, String string){
		update_message_queue.put(modid, string);
	}
	
	public static boolean loaded(){
		return loaded;
	}
	
	/**
	 * @return Latest version of the speficied mod.
	 */
	public static String getLatestVersionOf(String modid){
		if(obj.has(modid)){
			try{
				if(obj.get(modid).getAsJsonObject().has("versions")){
					for(JsonElement elm : obj.get(modid).getAsJsonObject().get("versions").getAsJsonArray()){
						if(elm.getAsJsonObject().get("version").getAsString().equals(FCL.mcv)){
							return elm.getAsJsonObject().get("latest_version").getAsString();
						}
					}
				}
				else{
					if(obj.get(modid).getAsJsonObject().has("latest_version")){
						return obj.get(modid).getAsJsonObject().get("latest_version").getAsString();
					}
					else return "<< NO VERSION DATA IN OBJ --- CHECK MOD JSON >>";
				}
			}
			catch(Exception ex){
				Print.log("-----------------------");
				Print.log("Malformed Moddata JSON for modid '" + modid + "'!");
				Print.log(obj.get(modid).toString());
				Print.log("-----------------------");
				Static.stop();
				return "error";
			}
		}
		return "null";
	}

	public static class EventHandler {
		
		@SubscribeEvent
		public void onJoin(PlayerEvent.PlayerLoggedInEvent event){
			new Thread("FCL-SUH"){
				@Override
				public void run(){
					for(String modid : mods_to_update){
						String string = update_message_queue.get(modid);
						if(string == null || string.length() < 4) continue;
						if(Static.dev()){
							Print.log(Formatter.format(update_message_queue.get(modid)));
						}
						else{
							Print.chat(event.player, Formatter.format(update_message_queue.get(modid)));
						}
					}
				}
			}.start();
		}
		
	}
		
}