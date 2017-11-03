package net.fexcraft.mod.lib.util.render;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.fexcraft.mod.lib.util.registry.UCResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;

public class ExternalTextureHelper {
	
	private static final Map<String, UCResourceLocation> map = new HashMap<String, UCResourceLocation>();
	private static final Map<String, UCResourceLocation> tempmap = new HashMap<String, UCResourceLocation>();
	
	public static UCResourceLocation get(String s){
		if(map.containsKey(s)){
			return map.get(s);
		}
		UCResourceLocation texture = new UCResourceLocation("fcl:remote/", s);
		ITextureObject object = Minecraft.getMinecraft().renderEngine.getTexture(texture);
        if(object == null){
    		ThreadDownloadImageData tdid = new ThreadDownloadImageData((File)null, s, null, null);
        	object = tdid;
        	Minecraft.getMinecraft().renderEngine.loadTexture(texture, object);
        }
        map.put(s, texture);
		return texture;
	}
	
	public static UCResourceLocation getTemp(String s){
		if(tempmap.containsKey(s)){
			return tempmap.get(s);
		}
		UCResourceLocation texture = new UCResourceLocation("fcl:remote/", s);
		ITextureObject object = Minecraft.getMinecraft().renderEngine.getTexture(texture);
        if(object == null){
    		ThreadDownloadImageData tdid = new ThreadDownloadImageData((File)null, s, null, null);
        	object = tdid;
        	Minecraft.getMinecraft().renderEngine.loadTexture(texture, object);
        }
        tempmap.put(s, texture);
		return texture;
	}
	
}