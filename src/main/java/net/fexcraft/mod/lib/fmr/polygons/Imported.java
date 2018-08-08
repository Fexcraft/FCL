package net.fexcraft.mod.lib.fmr.polygons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import net.fexcraft.mod.lib.fmr.PolygonShape;
import net.fexcraft.mod.lib.fmr.Shape;
import net.fexcraft.mod.lib.fmr.TexturedPolygon;
import net.fexcraft.mod.lib.fmr.TexturedVertex;
import net.fexcraft.mod.lib.tmt.ModelRendererTurbo;
import net.fexcraft.mod.lib.util.common.Static;
import net.fexcraft.mod.lib.util.math.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

/**
 * @author Ferdinand Calo' (FEX___96)
**/
public class Imported extends PolygonShape {

	public Imported(Shape type){
		super(type);
		if(!type.isExternal()){
			Static.exception(new Exception(String.format("Invalid usage of ImportedPolygon! Wrong PolygonType: %s", type.name())), true);
		}
	}

	public Imported(Shape type, boolean flip, boolean mirror){
		super(type, flip, mirror);
		if(!type.isExternal()){
			Static.exception(new Exception(String.format("Invalid usage of ImportedPolygon! Wrong PolygonType: %s", type.name())), true);
		}
	}
	
	public Imported importTMT(ModelRendererTurbo turbo){
    	faces = turbo.getFaces(); vertices = turbo.getVertices();
        rotationPointX = turbo.rotationPointX; rotationPointY = turbo.rotationPointY; rotationPointZ = turbo.rotationPointZ;
        rotateAngleX = turbo.rotateAngleX; rotateAngleY = turbo.rotateAngleY; rotateAngleZ = turbo.rotateAngleZ;
        return this;
	}
	

	public Imported importOBJ(String string_loc){
		return importOBJ(new ResourceLocation(string_loc));
	}
	
	/** Based on the TMT one. **/
	public Imported importOBJ(ResourceLocation loc){
		IResource resource = null;
		try{
			resource = Minecraft.getMinecraft().getResourceManager().getResource(loc);
			if(resource == null){
				throw new IOException(String.format("OBJ Model with Locatiion %s not found!", loc.toString()));
			}
		}
		catch(IOException e){
			e.printStackTrace(); Static.halt();
		}
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8")); String s;
			ArrayList<TexturedPolygon> face = new ArrayList<TexturedPolygon>();
			ArrayList<TexturedVertex> verts = new ArrayList<TexturedVertex>();
			ArrayList<float[]> uvs = new ArrayList<float[]>(), normals = new ArrayList<float[]>();
			while((s = in.readLine()) != null){
				if(s.contains("#")) s = s.substring(0, s.indexOf("#"));
				s = s.trim();
				if(s.equals("")){ continue; }
				if(s.startsWith("v ")){
					s = s.substring(s.indexOf(" ") + 1).trim();
					float[] v = new float[3];
					for(int i = 0; i < 3; i++){
						int ind = s.indexOf(" ");
						v[i] = Float.parseFloat(ind > -1 ? s.substring(0, ind) : s.substring(0));
						s = s.substring(s.indexOf(" ") + 1).trim();
					}
					float flt = v[2];
					v[2] = -v[1];
					v[1] = flt;
					verts.add(new TexturedVertex(v[0], v[1], v[2], 0, 0));
					continue;
				}
				if(s.startsWith("vt ")){
					s = s.substring(s.indexOf(" ") + 1).trim();
					float[] v = new float[2];
					for(int i = 0; i < 2; i++){
						int ind = s.indexOf(" ");
						v[i] = Float.parseFloat(ind > -1 ? s.substring(0, ind) : s.substring(0));
						s = s.substring(s.indexOf(" ") + 1).trim();
					}
					uvs.add(new float[] {v[0], 1F - v[1]});
					continue;
				}
				if(s.startsWith("vn ")){
					s = s.substring(s.indexOf(" ") + 1).trim();
					float[] v = new float[3];
					for(int i = 0; i < 3; i++){
						int ind = s.indexOf(" ");
						v[i] = Float.parseFloat(ind > -1 ? s.substring(0, ind) : s.substring(0));
						s = s.substring(s.indexOf(" ") + 1).trim();
					}
					float flt = v[2];
					v[2] = v[1];
					v[1] = flt;
					normals.add(new float[] {v[0], v[1], v[2]});
					continue;					
				}
				if(s.startsWith("f ")){
					s = s.substring(s.indexOf(" ") + 1).trim();
					ArrayList<TexturedVertex> v = new ArrayList<TexturedVertex>();
					String s1;
					int finalPhase = 0;
					float[] normal = new float[] {0F, 0F, 0F};
					ArrayList<Vec3f> iNormal = new ArrayList<Vec3f>();
					do{
						int vInt;
						float[] curUV;
						float[] curNormals;
						int ind = s.indexOf(" ");
						s1 = s;
						if(ind > -1) s1 = s.substring(0, ind);
						if(s1.indexOf("/") > -1){
							String[] f = s1.split("/");
							vInt = Integer.parseInt(f[0]) - 1;
							if(f[1].equals("")) f[1] = f[0];
							int vtInt = Integer.parseInt(f[1]) - 1;
							if(uvs.size() > vtInt){ curUV = uvs.get(vtInt); }
							else{ curUV = new float[] {0, 0}; }
							int vnInt = 0;
							if(f.length == 3){
								if(f[2].equals("")) f[2] = f[0];
								vnInt = Integer.parseInt(f[2]) - 1;
							}
							else{ vnInt = Integer.parseInt(f[0]) - 1; }
							if(normals.size() > vnInt){ curNormals = normals.get(vnInt); }
							else{ curNormals = new float[] {0, 0, 0}; }
						}
						else{
							vInt = Integer.parseInt(s1) - 1;
							if(uvs.size() > vInt){ curUV = uvs.get(vInt); }
							else{ curUV = new float[] {0, 0}; }
							if(normals.size() > vInt){ curNormals = normals.get(vInt); }
							else{ curNormals = new float[] {0, 0, 0}; }
						}
						iNormal.add(new Vec3f(curNormals[0], curNormals[1], curNormals[2]));
						normal[0]+= curNormals[0];
						normal[1]+= curNormals[1];
						normal[2]+= curNormals[2];
						if(vInt < verts.size()) v.add(verts.get(vInt).setTexturePosition(curUV[0], curUV[1]));
						if(ind > -1){ s = s.substring(s.indexOf(" ") + 1).trim(); }
						else{ finalPhase++; }
					}
					while(finalPhase < 1);
					float d = MathHelper.sqrt(normal[0] * normal[0] + normal[1] * normal[1] + normal[2] * normal[2]);
					normal[0]/= d;
					normal[1]/= d;
					normal[2]/= d;
					TexturedVertex[] vToArr = new TexturedVertex[v.size()];
					for(int i = 0; i < v.size(); i++){ vToArr[i] = v.get(i); }
					TexturedPolygon poly = new TexturedPolygon(vToArr);
					poly.setNormals(normal[0], normal[1], normal[2]);
					poly.setNormals(iNormal);
					face.add(poly); //texture.addPoly(poly);
					continue;					
				}
			}
			vertices = new TexturedVertex[verts.size()];
			for(int i = 0; i < verts.size(); i++){
				vertices[i] = verts.get(i);
			}
			faces = new TexturedPolygon[face.size()];
			for(int i = 0; i < face.size(); i++){
				faces[i] = face.get(i);
				faces[i].clearNormals();
			}
			in.close();
	    	if(flip){ for(int l = 0; l < faces.length; l++){ faces[l].flipFace(); } }
		}
		catch(Throwable e){
			e.printStackTrace();
			Static.stop();
		}
		return this;
	}

	@Override
	protected void populateJsonObject(JsonObject obj){
		//TODO
	}

	@Override
	protected PolygonShape compileShape(){
		compiled = true; return this;//No need to do more.
	}

}
