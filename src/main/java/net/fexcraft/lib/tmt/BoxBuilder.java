package net.fexcraft.lib.tmt;

import net.fexcraft.lib.common.math.TexturedPolygon;
import net.fexcraft.lib.common.math.TexturedVertex;
import net.fexcraft.lib.common.math.Vec3f;

/**
 * Box Builder, initially intended to be part of FMR, but as things stand it will be part of TMT for now.
 * @author Ferdinand Calo' (FEX___96)
 */
public class BoxBuilder {
	
	private final ModelRendererTurbo root;
	private float x, y, z, expansion, w, h, d;
	private boolean[] invisible = new boolean[6];
	private float[][] uv = new float[6][];
	private Vec3f[] corners = new Vec3f[8];
	private static final Vec3f NULLVEC = new Vec3f(0, 0, 0);
	
	public BoxBuilder(){
		this(null);
	}
	
	public BoxBuilder(ModelRendererTurbo root){
		this.root = root == null ? new ModelRendererTurbo(null) : root;
	}
	
	public BoxBuilder setOffset(float x, float y, float z){
		this.x = x; this.y = y; this.z = z;
		return this;
	}
	
	public BoxBuilder setSize(float w, float h, float d){
		this.w = w; this.h = h; this.d = d;
		return this;
	}
	
	public BoxBuilder setExpansion(float exp){
		this.expansion = exp;
		return this;
	}
	
	public BoxBuilder removePolygon(int... poly_indices){
		for(int index : poly_indices){
			if(index < 0 || index > 5) continue;
			invisible[index] = true;
		}
		return this;
	}

	public BoxBuilder removePolygon(boolean... sides){
		for(int index = 0; index < 6; index++){
			if(sides.length >= (index + 1) && sides[index]) invisible[index] = true;
		}
		return this;
	}
	
	public BoxBuilder setPolygonUV(int poly_index, float[] uv){
		if(poly_index < 0 || poly_index > 5) return this;
		this.uv[poly_index] = uv;
		return this;
	}
	
	public BoxBuilder setPolygonUVs(int[] poly_indices, float[][] uvs){
		for(int i = 0; i < poly_indices.length; i++){
			if(poly_indices[i] < 0 || poly_indices[i] > 5) continue;
			setPolygonUV(poly_indices[i], uvs[i]);
		}
		return this;
	}
	
	public BoxBuilder setPolygonUVs(float[][] uvs){
		for(int index = 0; index < 6; index++){
			if(index >= uvs.length) break;
			setPolygonUV(index, uvs[index]);
		}
		return this;
	}

	public BoxBuilder setCorner(int index, Vec3f corner){
		if(index < 0 || index > 7) return this;
		corners[index] = corner;
		return this;
	}

	public BoxBuilder setCorner(int index, float x, float y, float z){
		if(index < 0 || index > 7) return this;
		corners[index] = new Vec3f(x, y, z);
		return this;
	}

	public BoxBuilder setCorners(Vec3f cor0, Vec3f cor1, Vec3f cor2, Vec3f cor3, Vec3f cor4, Vec3f cor5, Vec3f cor6, Vec3f cor7){
		corners[0] = cor0;
		corners[1] = cor1;
		corners[2] = cor2;
		corners[3] = cor3;
		corners[4] = cor4;
		corners[5] = cor5;
		corners[6] = cor6;
		corners[7] = cor7;
		return this;
	}
	
	public BoxBuilder setCorners(float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float x5, float y5, float z5, float x6, float y6, float z6, float x7, float y7, float z7){
		corners[0] = new Vec3f(x0, y0, z0);
		corners[1] = new Vec3f(x1, y1, z1);
		corners[2] = new Vec3f(x2, y2, z2);
		corners[3] = new Vec3f(x3, y3, z3);
		corners[4] = new Vec3f(x4, y4, z4);
		corners[5] = new Vec3f(x5, y5, z5);
		corners[6] = new Vec3f(x6, y6, z6);
		corners[7] = new Vec3f(x7, y7, z7);
		return this;
	}
	
	public ModelRendererTurbo build(){
		boolean isshapebox = false;
		for(Vec3f corner : corners){
			if(corner != null){
				isshapebox = true;
				break;
			}
		}
		boolean anyoff = false;
		for(boolean bool : invisible){
			if(bool){
				anyoff = true;
				break;
			}
		}
		float[] v0, v1, v2, v3, v4, v5, v6, v7;
		if(isshapebox){
			Vec3f c0 = corners[0] == null ? NULLVEC : corners[0];
			Vec3f c1 = corners[1] == null ? NULLVEC : corners[1];
			Vec3f c2 = corners[2] == null ? NULLVEC : corners[2];
			Vec3f c3 = corners[3] == null ? NULLVEC : corners[3];
			Vec3f c4 = corners[4] == null ? NULLVEC : corners[4];
			Vec3f c5 = corners[5] == null ? NULLVEC : corners[5];
			Vec3f c6 = corners[6] == null ? NULLVEC : corners[6];
			Vec3f c7 = corners[7] == null ? NULLVEC : corners[7];
			float xw = x + w, yh = y + h, zd = z + d;
			if(root.mirror){ float fl = xw; xw = x; x = fl; }
			v0 = new float[]{ x  - c0.xCoord, y  - c0.yCoord, z  - c0.zCoord };
			v1 = new float[]{ xw + c1.xCoord, y  - c1.yCoord, z  - c1.zCoord };
			v2 = new float[]{ xw + c5.xCoord, yh + c5.yCoord, z  - c5.zCoord };
			v3 = new float[]{ x  - c4.xCoord, yh + c4.yCoord, z  - c4.zCoord };
			v4 = new float[]{ x  - c3.xCoord, y  - c3.yCoord, zd + c3.zCoord };
			v5 = new float[]{ xw + c2.xCoord, y  - c2.yCoord, zd + c2.zCoord };
			v6 = new float[]{ xw + c6.xCoord, yh + c6.yCoord, zd + c6.zCoord };
			v7 = new float[]{ x  - c7.xCoord, yh + c7.yCoord, zd + c7.zCoord };
		}
		else{
	    	if(w == 0) w = 0.01F; if(h == 0) h = 0.01F; if(d == 0) d = 0.01F;
	        float x1 = x + w, y1 = y + h, z1 = z + d;
	        x1 += expansion;  y1 += expansion; z1 += expansion;
	        if(root.mirror){ float xTemp = x1; x1 = x; x = xTemp; }
	        //
			v0 = new float[]{  x,  y,  z };
			v1 = new float[]{ x1,  y,  z };
			v2 = new float[]{ x1, y1,  z };
			v3 = new float[]{  x, y1,  z };
			v4 = new float[]{  x,  y, z1 };
			v5 = new float[]{ x1,  y, z1 };
			v6 = new float[]{ x1, y1, z1 };
			v7 = new float[]{  x, y1, z1 };
		}
    	TexturedVertex[] verts = new TexturedVertex[8];
        TexturedPolygon[] poly = new TexturedPolygon[6];
        TexturedVertex tv0 = new TexturedVertex(v0[0], v0[1], v0[2], 0.0F, 0.0F);
        TexturedVertex tv1 = new TexturedVertex(v1[0], v1[1], v1[2], 0.0F, 8.0F);
        TexturedVertex tv2 = new TexturedVertex(v2[0], v2[1], v2[2], 8.0F, 8.0F);
        TexturedVertex tv3 = new TexturedVertex(v3[0], v3[1], v3[2], 8.0F, 0.0F);
        TexturedVertex tv4 = new TexturedVertex(v4[0], v4[1], v4[2], 0.0F, 0.0F);
        TexturedVertex tv5 = new TexturedVertex(v5[0], v5[1], v5[2], 0.0F, 8.0F);
        TexturedVertex tv6 = new TexturedVertex(v6[0], v6[1], v6[2], 8.0F, 8.0F);
        TexturedVertex tv7 = new TexturedVertex(v7[0], v7[1], v7[2], 8.0F, 0.0F);
        verts[0] = tv0; verts[1] = tv1; verts[2] = tv2; verts[3] = tv3;
        verts[4] = tv4; verts[5] = tv5; verts[6] = tv6; verts[7] = tv7;
        float tx = root.texoffx, ty = root.texoffy;
        if(!anyoff){
            poly[0] = genPolygonWithUV(0, new TexturedVertex[] { tv5, tv1, tv2, tv6 }, tx, ty, d + w, d, d, h);
            poly[1] = genPolygonWithUV(1, new TexturedVertex[] { tv0, tv4, tv7, tv3 }, tx, ty, 0, d, d, h);
            poly[2] = genPolygonWithUV(2, new TexturedVertex[] { tv5, tv4, tv0, tv1 }, tx, ty, d, 0, w, d);
            poly[3] = genPolygonWithUV(3, new TexturedVertex[] { tv2, tv3, tv7, tv6 }, tx, ty, d + w, 0, w, d);
            poly[4] = genPolygonWithUV(4, new TexturedVertex[] { tv1, tv0, tv3, tv2 }, tx, ty, d, d, w, h);
            poly[5] = genPolygonWithUV(5, new TexturedVertex[] { tv4, tv5, tv6, tv7 }, tx, ty, d + w + d, d, w, h);
        }
        else{
        	float yp = invisible[2] && invisible[3] ? 0 : d;
        	float x0 = invisible[1] ? 0 : d;
        	float x1 = invisible[2] ? 0 : w;
        	float x2 = invisible[4] ? 0 : w;
        	float x3 = invisible[0] ? 0 : d;
            if(!invisible[0]) poly[0] = genPolygonWithUV(0, new TexturedVertex[] { tv5, tv1, tv2, tv6 }, tx, ty, x0 + x2, yp, d, h);
            if(!invisible[1]) poly[1] = genPolygonWithUV(1, new TexturedVertex[] { tv0, tv4, tv7, tv3 }, tx, ty, 0, yp, d, h);
            if(!invisible[2]) poly[2] = genPolygonWithUV(2, new TexturedVertex[] { tv5, tv4, tv0, tv1 }, tx, ty, x0, 0, w, d);
            if(!invisible[3]) poly[3] = genPolygonWithUV(3, new TexturedVertex[] { tv2, tv3, tv7, tv6 }, tx, ty, x0 + x1, 0, w, d);
            if(!invisible[4]) poly[4] = genPolygonWithUV(4, new TexturedVertex[] { tv1, tv0, tv3, tv2 }, tx, ty, x0, yp, w, h);
            if(!invisible[5]) poly[5] = genPolygonWithUV(5, new TexturedVertex[] { tv4, tv5, tv6, tv7 }, tx, ty, x0 + x2 + x3, yp, w, h);
        }
        if(root.mirror ^ root.flip){
            for(int l = 0; l < poly.length; l++){
            	poly[l].flipFace();
            }
        }
        if(anyoff){
        	int polis = 0, processed = 0;
        	for(int i = 0; i < poly.length; i++) if(poly[i] != null) polis++;
            TexturedPolygon[] polygons = new TexturedPolygon[polis];
        	for(int i = 0; i < poly.length; i++){
        		if(poly[i] != null){
        			polygons[processed] = poly[i];
        			processed++;
        		}
        	}
        	poly = polygons;
        }
        return root.copyTo(verts, poly);
	}

	private TexturedPolygon genPolygonWithUV(int index, TexturedVertex[] vertices, float tx, float ty, float x, float y, float ex, float ey){
		float[] cuv = index < 0 || uv[index] == null ? null : uv[index];
		if(cuv == null){
			float xs = tx + x;
			float xe = tx + x + ex;
			float ys = ty + y;
			float ye = ty + y + ey;
			vertices[0] = vertices[0].setTexturePosition(xe / root.textureWidth, ys / root.textureHeight);
	    	vertices[1] = vertices[1].setTexturePosition(xs / root.textureWidth, ys / root.textureHeight);
	    	vertices[2] = vertices[2].setTexturePosition(xs / root.textureWidth, ye / root.textureHeight);
	    	vertices[3] = vertices[3].setTexturePosition(xe / root.textureWidth, ye / root.textureHeight);
		}
		else if(cuv.length == 2){
			float xs = tx + cuv[0];
			float xe = tx + cuv[0] + ex;
			float ys = ty + cuv[1];
			float ye = ty + cuv[1] + ey;
			vertices[0] = vertices[0].setTexturePosition(xe / root.textureWidth, ys / root.textureHeight);
	    	vertices[1] = vertices[1].setTexturePosition(xs / root.textureWidth, ys / root.textureHeight);
	    	vertices[2] = vertices[2].setTexturePosition(xs / root.textureWidth, ye / root.textureHeight);
	    	vertices[3] = vertices[3].setTexturePosition(xe / root.textureWidth, ye / root.textureHeight);
		}
		else if(cuv.length == 4){
			float xs = tx + cuv[0];
			float xe = tx + cuv[2];
			float ys = ty + cuv[1];
			float ye = ty + cuv[3];
			vertices[0] = vertices[0].setTexturePosition(xe / root.textureWidth, ys / root.textureHeight);
	    	vertices[1] = vertices[1].setTexturePosition(xs / root.textureWidth, ys / root.textureHeight);
	    	vertices[2] = vertices[2].setTexturePosition(xs / root.textureWidth, ye / root.textureHeight);
	    	vertices[3] = vertices[3].setTexturePosition(xe / root.textureWidth, ye / root.textureHeight);
		}
		else if(cuv.length == 8){
			vertices[0] = vertices[0].setTexturePosition((tx + cuv[0]) / root.textureWidth, (ty + cuv[1]) / root.textureHeight);
	    	vertices[1] = vertices[1].setTexturePosition((tx + cuv[2]) / root.textureWidth, (ty + cuv[3]) / root.textureHeight);
	    	vertices[2] = vertices[2].setTexturePosition((tx + cuv[4]) / root.textureWidth, (ty + cuv[5]) / root.textureHeight);
	    	vertices[3] = vertices[3].setTexturePosition((tx + cuv[6]) / root.textureWidth, (ty + cuv[7]) / root.textureHeight);
		}
		else return genPolygonWithUV(-1, vertices, tx, ty, x, y, ex, ey);
		return new TexturedPolygon(vertices);
	}
	
}
