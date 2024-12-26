package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.uni.impl.ResLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.util.*;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ExternalTextures {

	private static final Map<String, ResLoc> MAP = new HashMap<>();
	private static final HashSet<String> KEY = new HashSet<>();
	static{ KEY.add("documents"); }

	public static ResLoc get(String mid, String url){
		if(MAP.containsKey(url)) return MAP.get(url);
		ResLoc texture = new ResLoc(mid, url.replaceAll("[^a-z0-9_.-]", ""));
		MAP.put(url, texture);
		File file = new File("./temp/fcl_download/" + texture.getPath());
		if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
		file.deleteOnExit();
		Minecraft.getInstance().textureManager.register(texture, new HttpTexture(file, url, texture, false, null));
		return texture;
	}

}
