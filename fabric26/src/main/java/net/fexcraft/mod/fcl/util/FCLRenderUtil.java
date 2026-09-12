package net.fexcraft.mod.fcl.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fexcraft.lib.frl.CompactModel;
import net.fexcraft.lib.frl.Polyhedron;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;

import static net.fexcraft.lib.frl.Renderer.RENDERER;

/**
 * Based on the one in FVTM.
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public class FCLRenderUtil {

	public static SubmitNodeCollector noco;

	public static void set(PoseStack pose, SubmitNodeCollector nocoll, RenderType type, int lc){
		Renderer26.stack = pose;
		noco = nocoll;
		Renderer26.type = type;
		Renderer26.light = lc;
	}

	public static void render(Polyhedron poly){
		if(!poly.visible) return;
		int col = Renderer26.color;
		int lig = Renderer26.light;
		noco.submitCustomGeometry(Renderer26.stack, Renderer26.type, (last, cons) -> {
			Renderer26.setColor(col);
			Renderer26.pose = last;
			Renderer26.cons = cons;
			Renderer26.light = lig;
			RENDERER.render(poly);
			Renderer26.pose = null;
		});
	}

	public static void render(CompactModel model){
		for(CompactModel.CompactGroup group : model.groups.values()){
			for(Polyhedron poly : group.polyhedrons){
				render(poly);
			}
		}
	}

}
