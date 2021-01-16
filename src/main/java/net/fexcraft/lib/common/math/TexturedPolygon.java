package net.fexcraft.lib.common.math;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

/**
 * Based on TMT, also compatible/required by the TMT branch shipped with FCL.
 * Further updates and patches by FEX___96 (Ferdinand Calo')
 * 
 */
public class TexturedPolygon {

	public static boolean TRIANGULATED_QUADS = true;
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
		        case 4: GL11.glBegin(TRIANGULATED_QUADS ? GL11.GL_TRIANGLES : GL11.GL_QUADS); break;
		        default: GL11.glBegin(GL11.GL_POLYGON); break;
	        };
		}
		boolean gnorm = list.isEmpty() || list.size() != vertices.length;
        if(gnorm) checkGenerated();
        if(TRIANGULATED_QUADS && vertices.length == 4){
            if(rgb == null && color == null){
            	triangleT(scale, 0, 1, 2, gnorm);
            }
            else{
            	(color == null ? rgb : color).glColorApply();
            	triangleC(scale, 0, 1, 2, gnorm);
            }
            if(rgb == null && color == null){
            	triangleT(scale, 0, 2, 3, gnorm);
            }
            else{
            	(color == null ? rgb : color).glColorApply();
            	triangleC(scale, 0, 2, 3, gnorm);
            }
        }
        else{
            for(int i = 0; i < vertices.length; i++){
            	TexturedVertex texvex = vertices[i];
            	if(!gnorm){
                	Vec3f norm = list.get(i);
                	if(invert) GL11.glNormal3f(-norm.xCoord, -norm.yCoord, -norm.zCoord);
                	else GL11.glNormal3f(norm.xCoord, norm.yCoord, norm.zCoord);
            	}
            	else{
                	if(invert){ GL11.glNormal3f(-normals[0], -normals[1], -normals[2]); }
                	else{ GL11.glNormal3f(normals[0], normals[1], normals[2]); }
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
        }
        GL11.glEnd();
    }

    private void triangleT(float scale, int x, int y, int z, boolean gnorm){
    	if(gnorm){
    		if(invert){ GL11.glNormal3f(-normals[0], -normals[1], -normals[2]); }
        	else{ GL11.glNormal3f(normals[0], normals[1], normals[2]); }
    	}
    	if(!gnorm) norm(x);
    	GL11.glTexCoord2f(vertices[x].textureX, vertices[x].textureY);
    	GL11.glVertex3f(vertices[x].vector.xCoord * scale, vertices[x].vector.yCoord * scale, vertices[x].vector.zCoord * scale);
    	if(!gnorm) norm(y);
    	GL11.glTexCoord2f(vertices[y].textureX, vertices[y].textureY);
    	GL11.glVertex3f(vertices[y].vector.xCoord * scale, vertices[y].vector.yCoord * scale, vertices[y].vector.zCoord * scale);
    	if(!gnorm) norm(z);
    	GL11.glTexCoord2f(vertices[z].textureX, vertices[z].textureY);
    	GL11.glVertex3f(vertices[z].vector.xCoord * scale, vertices[z].vector.yCoord * scale, vertices[z].vector.zCoord * scale);
	}

	private void triangleC(float scale, int x, int y, int z, boolean gnorm){
    	if(gnorm){
    		if(invert){ GL11.glNormal3f(-normals[3], -normals[4], -normals[5]); }
        	else{ GL11.glNormal3f(normals[3], normals[4], normals[5]); }
    	}
    	if(!gnorm) norm(x);
    	GL11.glVertex3f(vertices[x].vector.xCoord * scale, vertices[x].vector.yCoord * scale, vertices[x].vector.zCoord * scale);
    	if(!gnorm) norm(y);
    	GL11.glVertex3f(vertices[y].vector.xCoord * scale, vertices[y].vector.yCoord * scale, vertices[y].vector.zCoord * scale);
    	if(!gnorm) norm(z);
    	GL11.glVertex3f(vertices[z].vector.xCoord * scale, vertices[z].vector.yCoord * scale, vertices[z].vector.zCoord * scale);
	}
    
    private void norm(int i){
    	Vec3f norm = list.get(i);
    	if(invert) GL11.glNormal3f(-norm.xCoord, -norm.yCoord, -norm.zCoord);
    	else GL11.glNormal3f(norm.xCoord, norm.yCoord, norm.zCoord);
	}

	private void checkGenerated(){
    	if(normals.length >= 3) return;
        if(TRIANGULATED_QUADS && vertices.length == 4){
	        Vec3f vec0 = new Vec3f(vertices[1].vector.subtract(vertices[0].vector));
	        Vec3f vec1 = new Vec3f(vertices[1].vector.subtract(vertices[2].vector));
	        Vec3f vec2 = vec1.crossProduct(vec0).normalize();
	        vec0 = new Vec3f(vertices[2].vector.subtract(vertices[0].vector));
	        vec1 = new Vec3f(vertices[2].vector.subtract(vertices[3].vector));
	        Vec3f vec3 = vec1.crossProduct(vec0).normalize();
	        normals = new float[]{ vec2.xCoord, vec2.yCoord, vec2.zCoord, vec3.xCoord, vec3.yCoord, vec3.zCoord };
        }
		else if(vertices.length >= 3){
	        Vec3f vec0 = new Vec3f(vertices[1].vector.subtract(vertices[0].vector));
	        Vec3f vec1 = new Vec3f(vertices[1].vector.subtract(vertices[2].vector));
	        Vec3f vec2 = vec1.crossProduct(vec0).normalize();
	        normals = new float[]{ vec2.xCoord, vec2.yCoord, vec2.zCoord };
        }
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

	public List<Vec3f> getNormalVerts(){
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