package net.fexcraft.mod.fcl.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fexcraft.mod.uni.IDL;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.util.TriState;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Based on the same purpose class in FVTM.
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public class FCLRenderTypes {

	protected static final HashMap<IDL, RenderType> CUTOUTS = new HashMap<>();

	private static final Function<IDL, RenderType> CUTOUT = Util.memoize(idl -> {
		CompositeState state = CompositeState.builder()
			.setTextureState(new RenderStateShard.TextureStateShard(idl.local(), TriState.DEFAULT, false))
			.setLightmapState(RenderStateShard.LIGHTMAP)
			.setOverlayState(RenderStateShard.OVERLAY)
			.createCompositeState(true);
		return RenderType.create("fcl:entity_cutout", 1536, true, false, RenderPipelines.ENTITY_CUTOUT, state);
	});

	public static void setCutout(IDL tex){
		RenderType type = CUTOUTS.get(tex);
		if(type != null){
			Renderer120.rentype = type;
			return;
		}
		type = CUTOUT.apply(tex);
		CUTOUTS.put(tex, type);
		Renderer120.rentype = type;
	}

	public static void setLines(){
		Renderer120.rentype = RenderType.lines();
	}

	public static void setLineStrip(){
		Renderer120.rentype = RenderType.lineStrip();
	}

	public static void setDef(RenderType type){
		Renderer120.rentype = type;
	}

}
