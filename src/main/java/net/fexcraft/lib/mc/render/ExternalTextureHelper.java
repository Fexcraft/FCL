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
	
	public static UCResourceLocation get(String s){
		if(map.containsKey(s)){ return map.get(s); }
		UCResourceLocation texture = new UCResourceLocation("fcl:remote/", s);
		ITextureObject object = Minecraft.getMinecraft().renderEngine.getTexture(texture);
        if(object == null){
        	File file = new File(s);
    		ThreadDownloadImageData tdid = new ThreadDownloadImageData(file.exists() ? file : null, s, null, null);
        	object = tdid;
        	Minecraft.getMinecraft().renderEngine.loadTexture(texture, object);
        }
        map.put(s, texture);
		return texture;
	}
	
}