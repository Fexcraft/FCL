package net.fexcraft.mod.fcl.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.frl.*;
import net.fexcraft.mod.uni.IDL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import org.joml.*;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Renderer21 extends Renderer<GLObject> {

	public static final Vector3f AY = new Vector3f(0, 1, 0);
	public static final Vector3f AX = new Vector3f(1, 0, 0);
	public static final Vector3f AZ = new Vector3f(0, 0, 1);
	public static final Vector3f NULLVEC = new Vector3f(0, 0, 0);
	public static Renderer21 REN_IN = new Renderer21();
	//
	private static int color = 0xffffffff;
	//
	private static BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
	public static PoseStack.Pose pose;
	public static VertexConsumer cons;
	public static PoseStack stack;
	public static int overlay = OverlayTexture.NO_OVERLAY;
	public static int light;
	public static RenderType type;

	public static void setColor(RGB col){
		color = col.packed + 0xff000000;
	}

	public static void setColor(RGB col, float al){
		color = col.packed + (int)(0xff000000 * al);
	}

	public static void setColor(int col){
		color = col;
	}

	public static void resetColor(){
		color = 0xffffffff;
	}

	public void render(Polyhedron<GLObject> poly){
		if(!poly.visible) return;
		pose.translate(poly.posX, poly.posY, poly.posZ);
		if(poly.rotX != 0.0F || poly.rotY != 0.0F || poly.rotZ != 0.0F){
			pose.mulPose(new Matrix4f().identity()
				.rotate(Static.toRadians(poly.rotY), 0, 1, 0)
				.rotate(Static.toRadians(poly.rotX), 1, 0, 0)
				.rotate(Static.toRadians(poly.rotZ), 0, 0, 1)
			);
		}
		Matrix4f verma = pose.pose();
		Matrix3f norma = pose.normal();
		for(Polygon poli : poly.polygons){
			if(type.mode() == VertexFormat.Mode.QUADS){
				for(Vertex vert : poli.vertices){
					fillVert(verma, norma, vert);
				}
				if(poli.vertices.length < 4){
					fillVert(verma, norma, poli.vertices[2]);
				}
			}
			else{
				if(poli.vertices.length == 4){
					fillVert(verma, norma, poli.vertices[0]);
					fillVert(verma, norma, poli.vertices[1]);
					fillVert(verma, norma, poli.vertices[2]);
					fillVert(verma, norma, poli.vertices[0]);
					fillVert(verma, norma, poli.vertices[2]);
					fillVert(verma, norma, poli.vertices[3]);
				}
				else{
					for(Vertex vert : poli.vertices){
						fillVert(verma, norma, vert);
					}
				}
			}
		}
	}

	private void fillVert(Matrix4f verma, Matrix3f norma, Vertex vert){
		Vector4f vec = verma.transform(new Vector4f(vert.vector.x, vert.vector.y, vert.vector.z, 1.0F));
		Vector3f norm = norma.transform(vert.norm == null ? NULLVEC : new Vector3f(vert.norm.x, vert.norm.y, vert.norm.z));
		cons.addVertex(vec.x, vec.y, vec.z, color, vert.u, vert.v, overlay, light, norm.x, norm.y, norm.z);
	}

	public void delete(Polyhedron<GLObject> poly){
		//
	}

	@Override
	public void push(){
		stack.pushPose();
	}

	@Override
	public void pop(){
		stack.popPose();
	}

	@Override
	public void translate(double x, double y, double z){
		stack.translate((float)x, (float)y, (float)z);
	}

	@Override
	public void rotate(float deg, int x, int y, int z){
		stack.mulPose(new Matrix4f().rotate(deg * Static.rad1, x, y, z));
	}

	@Override
	public void rotate(double deg, int x, int y, int z){
		stack.mulPose(new Matrix4f().rotate((float)deg * Static.rad1, x, y, z));
	}

	@Override
	public void rotateRad(float rad, int x, int y, int z){
		stack.mulPose(new Matrix4f().rotate(rad, x, y, z));
	}

	@Override
	public void rotateRad(double rad, int x, int y, int z){
		stack.mulPose(new Matrix4f().rotate((float)rad, x, y, z));
	}

	@Override
	public void scale(double x, double y, double z){
		stack.scale((float)x, (float)y, (float)z);
	}

	@Override
	public void bind(IDL tex){
		type = FCLRenderTypes.getCutout(tex);
	}

	@Override
	public void color(int rgb){
		color = rgb;
	}

	@Override
	public void light(V3D vec){
		light = LevelRenderer.getLightCoords(Minecraft.getInstance().level, pos.set(vec.x, vec.y, vec.z));
	}

}