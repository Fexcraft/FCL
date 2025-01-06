package net.fexcraft.mod.fcl.local;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.fexcraft.mod.fcl.util.CraftingModel;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

import java.util.ArrayList;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftingRenderer implements BlockEntityRenderer<CraftingEntity> {

	public static final IDL TEXTURE = IDLManager.getIDLCached("fcl:textures/block/crafting.png");
	private static CraftingModel MODEL = new CraftingModel();

	@Override
	public void render(CraftingEntity tile, float ticks, PoseStack pose, MultiBufferSource buffer, int light, int overlay){
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		for(ArrayList<ModelRendererTurbo> group : MODEL.groups){
			for(ModelRendererTurbo turbo : group){
				//TODO turbo.render();
			}
		}
		pose.popPose();
	}

	@Override
	public int getViewDistance(){
        return 128;
    }

}
