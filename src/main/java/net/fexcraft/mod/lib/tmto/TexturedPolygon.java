package net.fexcraft.mod.lib.tmto;

import org.lwjgl.opengl.GL11;

import net.fexcraft.mod.lib.tmto.Tessellator;
import net.fexcraft.mod.lib.util.math.Vec3f;
import net.minecraft.client.model.PositionTextureVertex;

public class TexturedPolygon {
	
	public TexturedVertex[] vertices;
	
	public TexturedPolygon(TexturedVertex tvt[]){ vertices = tvt; }

	public TexturedPolygon(PositionTextureVertex[] pos){
		vertices = new TexturedVertex[pos.length];
		for(int i = 0; i < vertices.length && i < pos.length; i++){
			vertices[i] = new TexturedVertex(new Vec3f(pos[i].vector3D), pos[i].texturePositionX, pos[i].texturePositionY);
		}
	}

	public void draw(Tessellator tess, float scale){
		switch(vertices.length){
			case 3:{ tess.startDrawing(GL11.GL_TRIANGLES); break; }
			case 4:{ tess.startDrawing(GL11.GL_QUADS); break; }
			default:{ tess.startDrawing(GL11.GL_POLYGON); }
		}
		for(TexturedVertex texv : vertices){
			tess.addVertexWithUV(texv.vector.xCoord * scale, texv.vector.yCoord * scale, texv.vector.zCoord * scale, texv.texX, texv.texY);
		}
		tess.draw();
	}
	
	public void flipFace(){
		TexturedVertex[] tvt = new TexturedVertex[this.vertices.length];
		for(int i = 0; i < this.vertices.length; ++i) {
			tvt[i] = this.vertices[this.vertices.length - i - 1];
		} this.vertices = tvt;
	}
	
}
