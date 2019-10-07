package net.fexcraft.lib.mc.render;

import java.util.HashMap;
import java.util.Map;
import net.fexcraft.lib.mc.registry.UCResourceLocation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Texture;

@Deprecated//till updated
public class ExternalTextureHelper {
	
	private static final Map<String, UCResourceLocation> map = new HashMap<String, UCResourceLocation>();
	
	public static UCResourceLocation get(String s){
		if(map.containsKey(s)){ return map.get(s); }
		UCResourceLocation texture = new UCResourceLocation("fcl:remote/", s);
		Texture object = MinecraftClient.getInstance().getTextureManager().getTexture(texture);
        if(object == null){ //File file = new File(s);
        	//ThreadDownloadImageData tdid = new ThreadDownloadImageData(file.exists() ? file : null, s, null, null);
        	//MinecraftClient.getInstance().getTextureManager().loadTextureAsync(texture, (object = tdid));
        } map.put(s, texture); return texture;
	}
	
}