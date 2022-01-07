package net.fexcraft.lib.mc.render;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.fexcraft.lib.mc.registry.UCResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;

public class ExternalTextureHelper {
	
	private static final Map<String, UCResourceLocation> map = new HashMap<String, UCResourceLocation>();
	public static final String PREFIX = "fcl:remote/";
	
	public static UCResourceLocation get(String s){
		if(map.containsKey(s)) return map.get(s);
		UCResourceLocation texture = new UCResourceLocation(PREFIX, s);
		ITextureObject object = Minecraft.getMinecraft().renderEngine.getTexture(texture);
        if(object == null){
        	File file = new File("./cache/fcl/img/" + s);
    		ThreadDownloadImageData tdid = new ThreadDownloadImageData(file.exists() ? file : null, s, null, null);
        	Minecraft.getMinecraft().renderEngine.loadTexture(texture, (object = tdid));
        }
        map.put(s, texture);
        return texture;
	}
	
}