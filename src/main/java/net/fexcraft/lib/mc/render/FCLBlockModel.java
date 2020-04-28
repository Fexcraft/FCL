package net.fexcraft.lib.mc.render;

import java.util.Collection;
import java.util.Map;

import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.minecraft.util.ResourceLocation;

public interface FCLBlockModel {
	
	public Collection<ModelRendererTurbo> getPolygons(Map<String, String> arguments);

	public default Collection<ResourceLocation> getTextures(){ return null; }
	
}