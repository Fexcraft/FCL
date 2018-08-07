package net.fexcraft.mod.lib.fmr;

import java.util.TreeMap;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Ferdinand Calo' (FEX___96)
**/
@SideOnly(Side.CLIENT)
public class TextureManager {

	private static final TreeMap<String, Integer> CACHED_TEXTURE_IDS = new TreeMap<String, Integer>();
	
	private static Integer tempid = 0;
	
	public static final void bindTexture(ResourceLocation rs){
		tempid = CACHED_TEXTURE_IDS.get(rs.toString());
		if(tempid == null){ tempid = findAndCache(rs, null); }
		bind(tempid);
	}
	
	public static final void bindTexture(String resloc){
		tempid = CACHED_TEXTURE_IDS.get(resloc);
		if(tempid == null){ tempid = findAndCache(null, resloc); }
		bind(tempid);
	}

	private static void bind(int id){
		if(GL11.glGetInteger(GL11.GL_TEXTURE_2D) != id){ GL11.glBindTexture(GL11.GL_TEXTURE_2D, id); }
	}

	private static final int findAndCache(ResourceLocation resloc, String str){
		ResourceLocation loc = resloc == null ? new ResourceLocation(str) : resloc;
        ITextureObject object = Minecraft.getMinecraft().getTextureManager().getTexture(loc);
        if(object == null){
            Minecraft.getMinecraft().getTextureManager().loadTexture(loc, object = new SimpleTexture(loc));
            CACHED_TEXTURE_IDS.put(loc.toString(), object.getGlTextureId());
        }
        return object.getGlTextureId();
	}
	
}
