package net.fexcraft.lib.mc.network;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fexcraft.lib.common.utils.HttpUtil;
import net.fexcraft.lib.mc.FCL;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.lib.mc.utils.Static;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Network {
	
	private static boolean fcl_version_checked = false;
	
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
				ResourceLocation rs = new ResourceLocation(s);
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
				ResourceLocation rs = new ResourceLocation(s);
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
	public static MinecraftServer getMinecraftServer(){
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}
	
	public static void browse(ICommandSender sender, String url){
    	try{ Desktop.getDesktop().browse(new URI(url)); }
    	catch(IOException | URISyntaxException e){
			Print.chat(sender, FCL.prefix + "Error, couldn't open link.");
			e.printStackTrace();
		}
	}

	private static JsonObject status_data;
	
	public JsonObject getStatusJson(){
		return status_data;
	}
	
}