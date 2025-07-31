package net.fexcraft.mod.fcl.local;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.fexcraft.mod.fcl.util.CraftingModel;
import net.fexcraft.mod.fcl.util.FCLRenderTypes;
import net.fexcraft.mod.fcl.util.Renderer21;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.ArrayList;

import static net.fexcraft.mod.fcl.local.CraftingBlock.FACING;
import static net.fexcraft.mod.fcl.util.Renderer21.AY;
import static net.fexcraft.mod.fcl.util.Renderer21.AZ;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftingRenderer implements BlockEntityRenderer<CraftingEntity> {

	public static final IDL TEXTURE = IDLManager.getIDLCached("fcl:textures/block/crafting.png");
	private static CraftingModel MODEL = new CraftingModel();

	@Override
	public void render(CraftingEntity tile, float ticks, PoseStack pose, MultiBufferSource buffer, int light, int overlay, Vec3 pos){
		Renderer21.pose = pose;
		Renderer21.set(pose, buffer, light, overlay);
		FCLRenderTypes.setCutout(TEXTURE);
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		Direction dir = tile.getBlockState().getValue(FACING);
		pose.mulPose(new Quaternionf().rotateAxis(Static.toRadians(dir.getAxis() == Direction.Axis.Z ? dir.toYRot() : dir.toYRot() - 180), AY));
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
