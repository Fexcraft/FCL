package net.fexcraft.mod.fcl.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.common.math.TexturedPolygon;
import net.fexcraft.lib.common.math.TexturedVertex;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.*;

/**
 * Based on the FRL 1.20+ renderer from FVTM
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public class Renderer26MRT extends ModelRendererTurbo.Renderer {

	public static final Vector3f AY = Renderer26.AY;
	public static final Vector3f AX = Renderer26.AX;
	public static final Vector3f AZ = Renderer26.AZ;
	//
	private static int color = 0xffffffff;
	//
	public static RenderType type;
	public static PoseStack stack;
	public static SubmitNodeCollector noco;
	protected static int overlay = OverlayTexture.NO_OVERLAY;
	public static int light;

	public static void setColor(RGB col){
		color = col.packed;
	}

	public static void resetColor(){
		color = 0xffffffff;
	}

	public static void rotateDeg(float by, Vector3f axe){
		stack.mulPose(new Quaternionf().rotateAxis(Static.toRadians(by), axe));
	}

	public static void rotateRad(float by, Vector3f axe){
		stack.mulPose(new Quaternionf().rotateAxis(by, axe));
	}

	public static void rotateDeg(PoseStack pose, float by, Vector3f axe){
		pose.mulPose(new Quaternionf().rotateAxis(Static.toRadians(by), axe));
	}

	public static void rotateRad(PoseStack pose, float by, Vector3f axe){
		pose.mulPose(new Quaternionf().rotateAxis(by, axe));
	}

	public static void pushPose(){
		stack.pushPose();
	}

	public static void popPose(){
		stack.popPose();
	}

	public static void set(PoseStack pstack, RenderType rtype, SubmitNodeCollector node, int lc){
		stack = pstack;
		type = rtype;
		noco = node;
		light = lc;
	}

	public void render(ModelRendererTurbo turbo, float scale){
		if(!turbo.showModel) return;
		stack.pushPose();
		stack.translate(turbo.rotationPointX * scale, turbo.rotationPointY * scale, turbo.rotationPointZ * scale);
		if(turbo.rotationAngleX != 0.0F || turbo.rotationAngleY != 0.0F || turbo.rotationAngleZ != 0.0F){
			stack.mulPose(new Quaternionf()
				.rotateAxis(Static.toRadians(turbo.rotationAngleY), AY)
				.rotateAxis(Static.toRadians(turbo.rotationAngleX), AX)
				.rotateAxis(Static.toRadians(turbo.rotationAngleZ), AZ)
			);
		}
		noco.submitCustomGeometry(stack, type, (pose, cons) -> {
			Matrix4f verma = pose.pose();
			Matrix3f norma = pose.normal();
			for(TexturedPolygon poli : turbo.getFaces()){
				if(type.mode() == VertexFormat.Mode.QUADS){
					for(TexturedVertex vert : poli.getVertices()){
						fillVert(cons, poli, verma, norma, vert, scale);
					}
					if(poli.getVertices().length < 4){
						fillVert(cons, poli, verma, norma, poli.getVertices()[2], scale);
					}
				}
				else{
					if(poli.getVertices().length == 4){
						fillVert(cons, poli, verma, norma, poli.getVertices()[0], scale);
						fillVert(cons, poli, verma, norma, poli.getVertices()[1], scale);
						fillVert(cons, poli, verma, norma, poli.getVertices()[2], scale);
						fillVert(cons, poli, verma, norma, poli.getVertices()[0], scale);
						fillVert(cons, poli, verma, norma, poli.getVertices()[2], scale);
						fillVert(cons, poli, verma, norma, poli.getVertices()[3], scale);
					}
					else{
						for(TexturedVertex vert : poli.getVertices()){
							fillVert(cons, poli, verma, norma, vert, scale);
						}
					}
				}
			}
		});
		stack.popPose();
	}

	private void fillVert(VertexConsumer cons, TexturedPolygon poly, Matrix4f verma, Matrix3f norma, TexturedVertex vert, float scale){
		Vector4f vec = verma.transform(new Vector4f(vert.vector.x * scale, vert.vector.y * scale, vert.vector.z * scale, 1));
		if(poly.getNormals().length == 0) poly.genIfMissingNormals();
		Vector3f norm = norma.transform(new Vector3f(poly.getNormals()[0], poly.getNormals()[1], poly.getNormals()[2]));
		cons.addVertex(vec.x, vec.y, vec.z, color, vert.textureX, vert.textureY, overlay, light, norm.x, norm.y, norm.z);
	}

}