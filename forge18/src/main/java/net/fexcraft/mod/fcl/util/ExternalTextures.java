package net.fexcraft.mod.fcl.util;

import com.mojang.blaze3d.platform.NativeImage;
import net.fexcraft.mod.uni.impl.ResLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ExternalTextures {

	private static final Map<String, ResLoc> MAP = new HashMap<>();

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

	public static ResourceLocation get(String s, byte[] arr) throws IOException {
		if(MAP.containsKey(s)){
			if(arr != null) MAP.remove(s);
			else return MAP.get(s);
		}
		if(arr == null) return new ResourceLocation(s);
		ResourceLocation tex = new ResourceLocation(s);
		File file = new File("./temp/fcl_download/" + tex.getPath());
		if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
		file.deleteOnExit();
		Minecraft.getInstance().textureManager.register(tex, new DynamicTexture(NativeImage.read(new FileInputStream(file))));
		return tex;
	}

}
