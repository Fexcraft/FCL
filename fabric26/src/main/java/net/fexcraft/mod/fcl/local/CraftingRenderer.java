package net.fexcraft.mod.fcl.local;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.frl.CompactParserBEO;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.fcl.util.FCLRenderTypes;
import net.fexcraft.mod.fcl.util.FCLRenderUtil;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import org.joml.Quaternionf;

import static net.fexcraft.mod.fcl.local.CraftingBlock.FACING;
import static net.fexcraft.mod.fcl.util.Renderer26.AY;
import static net.fexcraft.mod.fcl.util.Renderer26.AZ;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftingRenderer implements BlockEntityRenderer<CraftingEntity, BlockEntityRenderState> {

	public static final IDL TEXTURE = IDLManager.getIDLCached("fcl:textures/block/crafting.png");

	public CraftingRenderer(){
		super();
		try{
			UniFCL.CRAFTING_MODEL = CompactParserBEO.parse(Minecraft.getInstance().getResourceManager().getResource(Identifier.parse("fcl:models/block/crafting.bob")).get().open(), 0.0625f);
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public BlockEntityRenderState createRenderState(){
		return new BlockEntityRenderState();
	}

	@Override
	public void submit(BlockEntityRenderState state, PoseStack pose, SubmitNodeCollector nodecoll, CameraRenderState camera){
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		Direction dir = state.blockState.getValue(FACING);
		pose.mulPose(new Quaternionf().rotateAxis(Static.toRadians(dir.getAxis() == Direction.Axis.Z ? dir.toYRot() : dir.toYRot() - 180), AY));
		pose.mulPose(new Quaternionf().rotateAxis(Static.rad180, AZ));
		FCLRenderUtil.set(pose, nodecoll, FCLRenderTypes.getCutout(TEXTURE), state.lightCoords);
		FCLRenderUtil.render(UniFCL.CRAFTING_MODEL);
		pose.popPose();
	}

	@Override
	public int getViewDistance(){
        return 128;
    }

}
