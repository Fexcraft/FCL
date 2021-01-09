package net.fexcraft.lib.common.math;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

/**
 * Based on TMT, also compatible/required by the TMT branch shipped with FCL.
 */
public class TexturedPolygon {

    private boolean invert = false;//, oppositetriangles;
    private float[] normals;
    private RGB color = null;
    private ArrayList<Vec3f> list;
    private TexturedVertex[] vertices;
	
	public TexturedPolygon(TexturedVertex[] verts){
		this.vertices = verts;
		normals = new float[0];
		list = new ArrayList<Vec3f>();
    }

	public TexturedPolygon(ArrayList<TexturedVertex> verts){
		this(verts.toArray(new TexturedVertex[0]));
	}

	public void setInvert(boolean bool){ invert = bool; }
	
	public void setNormals(float x, float y, float z){ normals = new float[] {x, y, z}; }
	
	public void setNormals(ArrayList<Vec3f> normallist){ list = normallist; }
	
	public void draw(float scale, RGB lincol, RGB rgb){
		if(lincol != null){
			GL11.glBegin(GL11.GL_LINE_STRIP); lincol.glColorApply();
			//
			/*if(triline && vertices.length == 4){
				TexturedVertex texvex = null;
				//tess.startDrawing(GL11.GL_LINE_STRIP); tess.setColor(lincol);
            	texvex = vertices[0]; tess.addVertex(texvex.vector.xCoord * scale, texvex.vector.yCoord * scale, texvex.vector.zCoord * scale);
            	texvex = vertices[1]; tess.addVertex(texvex.vector.xCoord * scale, texvex.vector.yCoord * scale, texvex.vector.zCoord * scale);
            	texvex = vertices[2]; tess.addVertex(texvex.vector.xCoord * scale, texvex.vector.yCoord * scale, texvex.vector.zCoord * scale);
            	texvex = vertices[0]; tess.addVertex(texvex.vector.xCoord * scale, texvex.vector.yCoord * scale, texvex.vector.zCoord * scale);
            	texvex = vertices[2]; tess.addVertex(texvex.vector.xCoord * scale, texvex.vector.yCoord * scale, texvex.vector.zCoord * scale);
            	texvex = vertices[3]; tess.addVertex(texvex.vector.xCoord * scale, texvex.vector.yCoord * scale, texvex.vector.zCoord * scale);
		        tess.draw(); return;
			}*/
		}
		else{
	        switch(vertices.length){
		        case 3: GL11.glBegin(GL11.GL_TRIANGLES); break;
		        case 4: GL11.glBegin(GL11.GL_QUADS); break;
		        default: GL11.glBegin(GL11.GL_POLYGON); break;
	        };
		}
        if(list.isEmpty()){
	        if(normals.length == 3){
	        	if(invert){ GL11.glNormal3f(-normals[0], -normals[1], -normals[2]); }
	        	else{ GL11.glNormal3f(normals[0], normals[1], normals[2]); }
	        }
	        else if(vertices.length >= 3){
		        Vec3f vec0 = new Vec3f(vertices[1].vector.subtract(vertices[0].vector));
		        Vec3f vec1 = new Vec3f(vertices[1].vector.subtract(vertices[2].vector));
		        Vec3f vec2 = vec1.crossProduct(vec0).normalize();
		        normals = new float[]{ vec2.xCoord, vec2.yCoord, vec2.zCoord };
		        if(invert){ GL11.glNormal3f(-vec2.xCoord, -vec2.yCoord, -vec2.zCoord); }
		        else{ GL11.glNormal3f(vec2.xCoord, vec2.yCoord, vec2.zCoord); }
	        }
	        else return;
        }
        for(int i = 0; i < vertices.length; i++){
        	TexturedVertex texvex = vertices[i];
            if(i < list.size()){
            	if(invert){ GL11.glNormal3f(-list.get(i).xCoord, -list.get(i).yCoord, -list.get(i).zCoord); }
            	else{ GL11.glNormal3f(list.get(i).xCoord, list.get(i).yCoord, list.get(i).zCoord); }
            }
            if(rgb == null && color == null){
            	GL11.glTexCoord2f(texvex.textureX, texvex.textureY);
            	GL11.glVertex3f(texvex.vector.xCoord * scale, texvex.vector.yCoord * scale, texvex.vector.zCoord * scale);
            }
            else{
            	(color == null ? rgb : color).glColorApply();
            	GL11.glVertex3f(texvex.vector.xCoord * scale, texvex.vector.yCoord * scale, texvex.vector.zCoord * scale);
            }
        }
        GL11.glEnd();
    }

    public void flipFace(){
        TexturedVertex[] verts = new TexturedVertex[vertices.length];
        for(int i = 0; i < vertices.length; ++i){
            verts[i] = vertices[vertices.length - i - 1];
        }
        vertices = verts;
    }

	public TexturedVertex[] getVertices(){
		return vertices;
	}

	public void clearNormals(){
		normals = new float[0]; list = new ArrayList<Vec3f>();
	}

	public boolean isInverted(){
		return invert;
	}

	public float[] getNormals(){
		return normals;
	}

	public List<Vec3f> getVectors(){
		return list;
	}

	public TexturedPolygon setColor(RGB rgb){
		this.color = rgb; return this;
	}

	/*public TexturedPolygon setOppositeTriangles(boolean bool){
		if(this.vertices.length != 4 || bool == oppositetriangles) return this;
		TexturedVertex[] verts = new TexturedVertex[4];
		verts[0] = vertices[1]; verts[1] = vertices[0];
		verts[2] = vertices[3]; verts[3] = vertices[2];
		vertices = verts; this.oppositetriangles = bool; return this;
	}*/
	
}