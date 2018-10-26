package net.fexcraft.lib.mc;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

import net.fexcraft.lib.common.utils.HttpUtil;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Ferdinand Calo' (FEX___96)
 *
 * MC version/mirror of net.fexcraft.lib.common.Static.class
 */
public class Static extends net.fexcraft.lib.common.Static {
	
	private static final HashMap<UUID, String> UUID_PLAYER_CACHE = new HashMap<UUID, String>();

	public static final void halt(int errid){
		FMLCommonHandler.instance().exitJava(errid, true);
	}

	public static final boolean isServer(){
		return FMLCommonHandler.instance().getSide().isServer();
	}

	public static final boolean isClient(){
		return FMLCommonHandler.instance().getSide().isClient();
	}
	
	public static final MinecraftServer getServer(){
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}
	
	public static final Side side(){
		return FMLCommonHandler.instance().getSide();
	}
	
	public static final String sideString(){
		return side().isClient() ? "Client" : "Server";
	}
	
	public static final boolean isOp(String name){
		return getServer().getPlayerList().getOppedPlayers().getGameProfileFromName(name) != null;
	}
	
	public static final String getPlayerNameByUUID(@Nullable UUID uuid){
		if(uuid == null){ return "<null-uuid>"; }
		if(UUID_PLAYER_CACHE.containsKey(uuid)){
			return UUID_PLAYER_CACHE.get(uuid);
		}
		GameProfile prof = getServer().getPlayerProfileCache().getProfileByUUID(uuid);
		if(prof != null){ UUID_PLAYER_CACHE.put(uuid, prof.getName()); return prof.getName(); }
		JsonElement obj = HttpUtil.request("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", ""));
		try{
			JsonObject elm = obj.getAsJsonObject();
			UUID_PLAYER_CACHE.put(uuid, elm.get("name").getAsString());
			return elm.get("name").getAsString();
		}
		catch(Exception e){
			Print.debug(obj == null ? "null:" + e.getMessage() : obj.toString());
			UUID_PLAYER_CACHE.put(uuid, "<null/errored/0>");
			return "<null/errored/0>";
		}
	}

	public static final String toString(BlockPos pos){
		return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
	}

	public static String getPlayerNameByUUID(String string){
		try{
			return getPlayerNameByUUID(UUID.fromString(string));
		}
		catch(Exception e){
			return "<null/uuid-parse-error>";
		}
	}
	
	public static float divide(float x, float y){
		return x == 0 || y == 0 ? 0 : x / y;
	}
	
	public static double divide(double x, double y){
		return x == 0 || y == 0 ? 0 : x / y;
	}
	
	/** 
	 * Generic command to print (throw) exceptions/errors into console/log without the need for extra writing of try/catch.
	 * @param exception the exception to be printed into console/log
	 * @param stop if the game should be force-closed
	 * **/
	public static void exception(Exception exception, boolean stop){
		try{ throw exception == null ? new Exception() : exception; }
		catch(Exception ex){ ex.printStackTrace(); }
		if(stop) halt();
	}
	
}