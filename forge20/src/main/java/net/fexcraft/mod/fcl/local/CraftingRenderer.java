package net.fexcraft.mod.fcl.local;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.fexcraft.mod.fcl.util.CraftingModel;
import net.fexcraft.mod.fcl.util.FCLRenderTypes;
import net.fexcraft.mod.fcl.util.Renderer120;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import org.joml.Quaternionf;

import java.util.ArrayList;

import static net.fexcraft.mod.fcl.local.CraftingBlock.FACING;
import static net.fexcraft.mod.fcl.util.Renderer120.*;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftingRenderer implements BlockEntityRenderer<CraftingEntity> {

	public static final IDL TEXTURE = IDLManager.getIDLCached("fcl:textures/block/crafting.png");
	private static CraftingModel MODEL = new CraftingModel();

	@Override
	public void render(CraftingEntity tile, float ticks, PoseStack pose, MultiBufferSource buffer, int light, int overlay){
		Renderer120.pose = pose;
		Renderer120.set(pose, buffer, light, overlay);
		FCLRenderTypes.setCutout(TEXTURE);
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		pose.mulPose(new Quaternionf().rotateAxis(Static.toRadians(tile.getBlockState().getValue(FACING).toYRot()), AY));
		pose.mulPose(new Quaternionf().rotateAxis(Static.rad180, AZ));
		for(ArrayList<ModelRendererTurbo> group : MODEL.groups){
			for(ModelRendererTurbo turbo : group){
				turbo.render();
			}
		}
		pose.popPose();
	}

	@Override
	public int getViewDistance(){
        return 128;
    }

}
