package net.fexcraft.mod.fcl.local;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.frl.CompactParserBEO;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.fcl.util.FCLRenderTypes;
import net.fexcraft.mod.fcl.util.Renderer20;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

import static net.fexcraft.mod.fcl.local.CraftingBlock.FACING;
import static net.fexcraft.mod.fcl.util.Renderer20.AY;
import static net.fexcraft.mod.fcl.util.Renderer20.AZ;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftingRenderer implements BlockEntityRenderer<CraftingEntity> {

	public static final IDL TEXTURE = IDLManager.getIDLCached("fcl:textures/block/crafting.png");

	public CraftingRenderer(){
		super();
		try{
			UniFCL.CRAFTING_MODEL = CompactParserBEO.parse(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("fcl:models/block/crafting.bob")).get().open(), 0.0625f);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void render(CraftingEntity tile, float ticks, PoseStack pose, MultiBufferSource buffer, int light, int overlay){
		Renderer20.pose = pose;
		Renderer20.set(pose, buffer, light, overlay);
		FCLRenderTypes.setCutout(TEXTURE);
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		Direction dir = tile.getBlockState().getValue(FACING);
		pose.mulPose(new Quaternionf().rotateAxis(Static.toRadians(dir.getAxis() == Direction.Axis.Z ? dir.toYRot() : dir.toYRot() - 180), AY));
		pose.mulPose(new Quaternionf().rotateAxis(Static.rad180, AZ));
		UniFCL.CRAFTING_MODEL.render();
		pose.popPose();
	}

	@Override
	public int getViewDistance(){
        return 128;
    }

}
