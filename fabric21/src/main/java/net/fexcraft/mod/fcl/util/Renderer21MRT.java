package net.fexcraft.mod.fcl.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.common.math.TexturedPolygon;
import net.fexcraft.lib.common.math.TexturedVertex;
import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.*;

/**
 * Based on the FRL 1.20+ renderer from FVTM
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public class Renderer21MRT extends ModelRendererTurbo.Renderer {

	public static final Vector3f AY = new Vector3f(0, 1, 0);
	public static final Vector3f AX = new Vector3f(1, 0, 0);
	public static final Vector3f AZ = new Vector3f(0, 0, 1);
	public static final Vector3f NULLVEC = new Vector3f(0, 0, 0);
	//
	public static final Vec3f DEFCOLOR = new Vec3f(1, 1, 1);
	private static int color = 0xffffffff;
	//
	public static PoseStack pose;
	private static MultiBufferSource buffer;
	private static VertexConsumer cons;
	protected static RenderType rentype;
	protected static int overlay = OverlayTexture.NO_OVERLAY;
	public static int light;

	public static void setColor(RGB col){
		color = col.packed;
	}

	public static void setColor(Vec3f col){
		//TODO color.copy(col);
	}

	public static void resetColor(){
		color = 0xffffffff;
	}

	public static void rotateDeg(float by, Vector3f axe){
		pose.mulPose(new Quaternionf().rotateAxis(Static.toRadians(by), axe));
	}

	public static void rotateRad(float by, Vector3f axe){
		pose.mulPose(new Quaternionf().rotateAxis(by, axe));
	}

	public static void rotateDeg(PoseStack pose, float by, Vector3f axe){
		pose.mulPose(new Quaternionf().rotateAxis(Static.toRadians(by), axe));
	}

	public static void rotateRad(PoseStack pose, float by, Vector3f axe){
		pose.mulPose(new Quaternionf().rotateAxis(by, axe));
	}

	public static void pushPose(){
		pose.pushPose();
	}

	public static void popPose(){
		pose.popPose();
	}

	public static RenderType rentype(){
		return rentype;
	}

	public void render(ModelRendererTurbo turbo, float scale){
		if(!turbo.showModel) return;
		pose.pushPose();
		pose.translate(turbo.rotationPointX * scale, turbo.rotationPointY * scale, turbo.rotationPointZ * scale);
		if(turbo.rotationAngleX != 0.0F || turbo.rotationAngleY != 0.0F || turbo.rotationAngleZ != 0.0F){
			pose.mulPose(new Quaternionf()
				.rotateAxis(Static.toRadians(turbo.rotationAngleY), AY)
				.rotateAxis(Static.toRadians(turbo.rotationAngleX), AX)
				.rotateAxis(Static.toRadians(turbo.rotationAngleZ), AZ)
			);
		}
		if(buffer != null) cons = buffer.getBuffer(rentype);
		Matrix4f verma = pose.last().pose();
		Matrix3f norma = pose.last().normal();
		for(TexturedPolygon poli : turbo.getFaces()){
			if(rentype.mode() == VertexFormat.Mode.QUADS){
				for(TexturedVertex vert : poli.getVertices()){
					fillVert(poli, verma, norma, vert, scale);
				}
				if(poli.getVertices().length < 4){
					fillVert(poli, verma, norma, poli.getVertices()[2], scale);
				}
			}
			else{
				if(poli.getVertices().length == 4){
					fillVert(poli, verma, norma, poli.getVertices()[0], scale);
					fillVert(poli, verma, norma, poli.getVertices()[1], scale);
					fillVert(poli, verma, norma, poli.getVertices()[2], scale);
					fillVert(poli, verma, norma, poli.getVertices()[0], scale);
					fillVert(poli, verma, norma, poli.getVertices()[2], scale);
					fillVert(poli, verma, norma, poli.getVertices()[3], scale);
				}
				else{
					for(TexturedVertex vert : poli.getVertices()){
						fillVert(poli, verma, norma, vert, scale);
					}
				}
			}
		}
		pose.popPose();
	}

	private void fillVert(TexturedPolygon poly, Matrix4f verma, Matrix3f norma, TexturedVertex vert, float scale){
		Vector4f vec = verma.transform(new Vector4f(vert.vector.x * scale, vert.vector.y * scale, vert.vector.z * scale, 1));
		if(poly.getNormals().length == 0) poly.genIfMissingNormals();
		Vector3f norm = norma.transform(new Vector3f(poly.getNormals()[0], poly.getNormals()[1], poly.getNormals()[2]));
		cons.addVertex(vec.x, vec.y, vec.z, color, vert.textureX, vert.textureY, overlay, light, norm.x, norm.y, norm.z);
	}

	public static void set(PoseStack ps, MultiBufferSource mbs, int lgt, int ol){
		pose = ps;
		buffer = mbs;
		cons = null;
		light = lgt;
		overlay = ol;
	}

	public static void set(PoseStack ps, MultiBufferSource mbs, int lgt){
		pose = ps;
		buffer = mbs;
		cons = null;
		light = lgt;
		overlay = OverlayTexture.NO_OVERLAY;
	}

	public static void set(PoseStack ps, VertexConsumer con, int lgt, int ol){
		pose = ps;
		buffer = null;
		cons = con;
		light = lgt;
		overlay = ol;
	}

	public static void set(PoseStack ps, VertexConsumer con, int lgt){
		pose = ps;
		buffer = null;
		cons = con;
		light = lgt;
		overlay = OverlayTexture.NO_OVERLAY;
	}

}