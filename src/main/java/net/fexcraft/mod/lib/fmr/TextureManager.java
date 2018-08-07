package net.fexcraft.mod.lib.fmr;

import java.util.HashMap;
import java.util.Map;
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

	private static final Map<ResourceLocation, Integer> CACHED_TEXTURE_IDS = new HashMap<>();
	
	private static Integer tempid = 0;
	private static ITextureObject texobj;
	
	public static final void bindTexture(ResourceLocation rs){
		if((tempid = CACHED_TEXTURE_IDS.get(rs)) == null){ tempid = findAndCache(rs); }
		if(GL11.glGetInteger(GL11.GL_TEXTURE_2D) != tempid){ GL11.glBindTexture(GL11.GL_TEXTURE_2D, tempid); }
	}

	private static final int findAndCache(ResourceLocation resloc){
        if((texobj = Minecraft.getMinecraft().getTextureManager().getTexture(resloc)) == null){
            Minecraft.getMinecraft().getTextureManager().loadTexture(resloc, texobj = new SimpleTexture(resloc));
            CACHED_TEXTURE_IDS.put(resloc, texobj.getGlTextureId());
        }
        return texobj.getGlTextureId();
	}
	
}
