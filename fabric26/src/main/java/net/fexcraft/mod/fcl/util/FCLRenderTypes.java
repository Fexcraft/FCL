package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.uni.IDL;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.Util;

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
		RenderSetup setup = RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT_CULL).withTexture("Sampler0", idl.local())
			.useLightmap().useOverlay().affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).sortOnUpload().createRenderSetup();
		return RenderType.create("fcl:entity_cutout", setup);
	});

	public static RenderType getCutout(IDL tex){
		RenderType type = CUTOUTS.get(tex);
		if(type != null){
			return type;
		}
		type = CUTOUT.apply(tex);
		CUTOUTS.put(tex, type);
		return type;
	}

}
