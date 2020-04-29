package net.fexcraft.lib.mc.render;

import java.util.Collection;
import java.util.Map;

import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public interface FCLBlockModel {
	
	public Collection<ModelRendererTurbo> getPolygons(IBlockState state, EnumFacing side, Map<String, String> arguments, long rand);

	public default Collection<ResourceLocation> getTextures(){ return null; }
	
}