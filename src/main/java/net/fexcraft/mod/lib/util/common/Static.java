package net.fexcraft.mod.lib.util.common;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

import net.fexcraft.mod.lib.network.Network;
import net.minecraft.entity.MoverType;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class Static{
	
	public static final boolean NO = false;
	public static final boolean YES = true;
	
	public static final float sixteenth = 0.0625F;
	public static final float eighth = 0.125F;
	public static final float quarter = 0.250F;
	public static final float half = 0.5F;

	public static final float rad180 = 3.14159f;
	public static final float rad160 = 2.79253f;
	public static final float rad120 = 2.0944f;
	public static final float rad90 = 1.5708f;
	public static final float rad60 = 1.0472f;
	public static final float rad45 = 0.785398f;
	public static final float rad30 = 0.523599f;
	public static final float rad20 = 0.349066f;
	public static final float rad12 = 0.20944f;
	public static final float rad10 = 0.174533f;
	public static final float rad6 = 0.10472f;
	public static final float rad5 = 0.0872665f;
	public static final float rad1 = 0.0174533f;
	
	//
	
	//
	
	public static final String Null_8 = "00000000";
	public static final String Null_16 = "0000000000000000";
	public static final String Null_32 = Null_16 + Null_16;
	public static final String Null_64 = Null_32 + Null_32;
	public static final String Null_128 = Null_64 + Null_64;
	public static final String Null_256 = Null_128 + Null_128;
	public static final String Null_512 = Null_256 + Null_256;
	public static final String Null_1024 = Null_512 + Null_512;
	public static final String Null_2048 = Null_1024 + Null_1024;
	public static final String Null_4096 = Null_2048 + Null_2048;
	public static final String NULL_UUID_STRING = "00000000-0000-0000-0000-000000000000";
	public static final String DEF1_UUID_STRING = "11111111-1111-1111-1111-111111111111";
	public static final MoverType SELF = MoverType.SELF;
	public static final Random random = new Random();
	
	/** soon... **/
	//public static final String Unicode_Block_
	
	/**
	 * Simple method to halt the current Minecraft Instance, "force close".
	 */
	public static void halt(){
		halt(1);
	}
	
	public static void halt(int errc){
		FMLCommonHandler.instance().exitJava(errc, true);
	}
	
	/**
	 * Alternative to {@link #halt()} which will only work in a developement workspace, nice for debugging.
	 * <br>
	 * See also {@link #dev()}
	 */
	public static void stop(){
		if(dev()){
			halt();
		}
	}
	
	public static void exception(int i){
		exception(i, null);
	}
	
	/**
	 * Alternative to {@link #halt()} or {@link #stop()} which will only work in a developement workspace, nice for debugging.
	 * <br>
	 * Which also prints the caller classes into console.
	 * <br>
	 * See also {@link #dev()}
	 */
	public static void exception(int i, String string){
		Exception ex = new Exception();
		for(int j = i; j > 0; j--){
			StackTraceElement elm = ex.getStackTrace()[j];
			Print.log("{" + elm.getClassName() + "#"  + elm.getMethodName() + " [LINE: " + elm.getLineNumber() + "]}");
		}
		if(string != null){
			Print.log(string);
		}
		if(dev()){
			halt();
		}
	}
	
	private static boolean dev = true, cdev = false;
	
	/**
	 * Simple method to find out if the current instance is a development workspace or nah.
	 * @return true if the current instance is a workspace, else false;
	 */
	public static boolean dev(){
		if(cdev){
			return dev;
		}
		else{
			cdev = true;
			dev = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
		}
		return dev;
	}
	
	public static void toggleDebug(){
		cdev = true;
		dev = !dev;
	}

	public static MinecraftServer getServer(){
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}
	
	public static Side side(){
		return FMLCommonHandler.instance().getSide();
	}
	
	public static Side side(boolean isRemote){
		return isRemote ? Side.CLIENT : Side.SERVER;
	}

	public static String sideString(){
		return side().isClient() ? "Client" : "Server";
	}
	
	public static final boolean isOp(String name){
		return getServer().getPlayerList().getOppedPlayers().getGameProfileFromName(name) != null;
	}
	
	private static final HashMap<UUID, String> cache = new HashMap<UUID, String>();
	public static final String getPlayerNameByUUID(UUID uuid){
		if(cache.containsKey(uuid)){
			return cache.get(uuid);
		}
		if(getServer().getPlayerProfileCache().getProfileByUUID(uuid) != null){
			GameProfile gp = getServer().getPlayerProfileCache().getProfileByUUID(uuid);
			cache.put(uuid, gp.getName());
			return gp.getName();
		}
		JsonElement obj = Network.request("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", ""));
		try{
			JsonObject elm = obj.getAsJsonObject();
			cache.put(uuid, elm.get("name").getAsString());
			return elm.get("name").getAsString();
		}
		catch(Exception e){
			Print.debug(obj == null ? "null" : obj.toString());
			e.printStackTrace();
		}
		return "<null/errored>";
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
	
}