package net.fexcraft.lib.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.annotation.Nullable;

import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.TexturedPolygon;
import net.fexcraft.lib.common.math.TexturedVertex;
import net.fexcraft.lib.common.math.Vec3f;

/**
 * @author Ferdinand Calo' (FEX___96)
 * 
 *         Utils to make OBJ import more fine-tuned.
 *
 */
public class WavefrontObjUtil {

	public static String[] getGroups(File objfile){
		try{
			return getGroups(new FileInputStream(objfile));
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			return new String[0];
		}
	}

	public static String[] getGroups(InputStream stream){
		ArrayList<String> arr = new ArrayList<String>();
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			String s;
			while((s = in.readLine()) != null){
				s = s.trim();
				if(s.length() == 0) continue;
				if(s.startsWith("g ")){
					arr.add(s.replace("g ", "").split(" ")[0]);
					continue;
				}
			}
			in.close();
		}
		catch(Throwable e){
			e.printStackTrace();
			Static.stop();
		}
		return arr.toArray(new String[0]);
	}

	public static String[] getObjects(InputStream stream){
		ArrayList<String> arr = new ArrayList<String>();
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			String s;
			while((s = in.readLine()) != null){
				s = s.trim();
				if(s.length() == 0) continue;
				if(s.startsWith("o ")){
					arr.add(s.replace("o ", "").split(" ")[0]);
					continue;
				}
			}
			in.close();
		}
		catch(Throwable e){
			e.printStackTrace();
			Static.stop();
		}
		return arr.toArray(new String[0]);
	}

	public static String[][] findValues(InputStream stream, Integer limit, String key){
		return findValues(stream, limit, new String[]{ key }, " ");
	}

	public static String[][] findValues(InputStream stream, Integer limit, String[] keys){
		return findValues(stream, limit, keys, " ");
	}

	public static String[][] findValues(InputStream stream, Integer limit, String[] keys, String divident){
		return findValues(stream, limit, keys, " ", null);
	}

	public static String[][] findValues(InputStream stream, Integer limit, String[] keys, String divident, String endat){
		if(keys == null || keys.length == 0) return new String[0][0];
		if(divident == null) divident = " ";
		ArrayList<String[]> arr = new ArrayList<String[]>();
		Integer counter = limit == null || limit <= 0 ? null : limit;
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			String s;
			while((s = in.readLine()) != null){
				if(counter != null && counter >= limit) break;
				s = s.trim();
				if(s.length() == 0) continue;
				if(endat != null && s.startsWith(endat)) break;
				for(String str : keys){
					if(s.startsWith(str + " ")){
						arr.add(s.replace(str + " ", "").split(divident));
						if(counter != null) counter++;
						continue;
					}
				}
				continue;
			}
			in.close();
		}
		catch(Throwable e){
			e.printStackTrace();
			Static.stop();
		}
		return arr.toArray(new String[0][]);
	}

	/** (Strongly) Based on the old OBJ parsing method in TMT/MRT. */
	public static Object[][] getVerticesAndPolygons(InputStream stream, @Nullable String group, boolean flipaxes, boolean objmode){
		TexturedVertex[] vertices = null;
		TexturedPolygon[] polygons = null;
		Boolean passing = null;
		String grouprefix = objmode ? "o " : "g ";
		ArrayList<TexturedVertex> verts0 = new ArrayList<TexturedVertex>();
		ArrayList<TexturedVertex> verts1 = new ArrayList<TexturedVertex>();
		ArrayList<TexturedPolygon> faces = new ArrayList<TexturedPolygon>();
		ArrayList<float[]> uvs = new ArrayList<float[]>(), normals = new ArrayList<float[]>();
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			String s;
			while((s = in.readLine()) != null){
				if(s.contains("#")) s = s.substring(0, s.indexOf("#"));
				s = s.trim();
				if(s.equals("")) continue;
				float[] v;
				if(s.startsWith("v ")){
					s = s.substring(s.indexOf(" ") + 1).trim();
					v = new float[3];
					for(int i = 0; i < 3; i++){
						int ind = s.indexOf(" ");
						if(ind > -1) v[i] = Float.parseFloat(s.substring(0, ind));
						else v[i] = Float.parseFloat(s.substring(0));
						s = s.substring(s.indexOf(" ") + 1).trim();
					}
					/*float flt = v[2];
					v[2] = -v[1];
					v[1] = flt;*/
					if(!flipaxes){
						verts0.add(new TexturedVertex(v[0], v[1], v[2], 0, 0));
					}
					else{
						verts0.add(new TexturedVertex(v[0], -v[2], v[1], 0, 0));
					}
					continue;
				}
				if(s.startsWith("vt ")){
					s = s.substring(s.indexOf(" ") + 1).trim();
					v = new float[2];
					for(int i = 0; i < 2; i++){
						int ind = s.indexOf(" ");
						if(ind > -1) v[i] = Float.parseFloat(s.substring(0, ind));
						else v[i] = Float.parseFloat(s.substring(0));
						s = s.substring(s.indexOf(" ") + 1).trim();
					}
					uvs.add(new float[]{ v[0] < 0 ? -v[0] : v[0], v[1] < 0 ? -v[1] : v[1] });
					continue;
				}
				if(s.startsWith("vn ")){
					s = s.substring(s.indexOf(" ") + 1).trim();
					v = new float[3];
					for(int i = 0; i < 3; i++){
						int ind = s.indexOf(" ");
						if(ind > -1) v[i] = Float.parseFloat(s.substring(0, ind));
						else v[i] = Float.parseFloat(s.substring(0));
						s = s.substring(s.indexOf(" ") + 1).trim();
					}
					float flt = v[2];
					v[2] = v[1];
					v[1] = flt;
					normals.add(new float[]{ v[0], v[1], v[2] });
					continue;
				}
				//
				if(s.startsWith(grouprefix) && group != null){
					passing = s.replace(grouprefix, "").split(" ")[0].equals(group);
				}
				if(passing != null && !passing){
					continue; // if(Static.dev()) Print.console("skipping: " + s);
				}
				//
				if(s.startsWith("f ")){
					s = s.substring(s.indexOf(" ") + 1).trim();
					ArrayList<TexturedVertex> vc = new ArrayList<TexturedVertex>();
					String s1;
					int finalPhase = 0;
					float[] normal = new float[]{ 0F, 0F, 0F };
					ArrayList<Vec3f> iNormal = new ArrayList<Vec3f>();
					do{
						int vInt, ind = s.indexOf(" ");
						float[] curUV, curNormals;
						s1 = s;
						if(ind > -1) s1 = s.substring(0, ind);
						if(s1.indexOf("/") > -1){
							String[] f = s1.split("/");
							vInt = Integer.parseInt(f[0]) - 1;
							if(f[1].equals("")) f[1] = f[0];
							int vtInt = Integer.parseInt(f[1]) - 1;
							if(uvs.size() > vtInt) curUV = uvs.get(vtInt);
							else curUV = new float[]{ 0, 0 };
							int vnInt = 0;
							if(f.length == 3){
								if(f[2].equals("")){
									f[2] = f[0];
								}
								vnInt = Integer.parseInt(f[2]) - 1;
							}
							else vnInt = Integer.parseInt(f[0]) - 1;
							if(normals.size() > vnInt){
								curNormals = normals.get(vnInt);
							}
							else curNormals = new float[]{ 0, 0, 0 };
						}
						else{
							vInt = Integer.parseInt(s1) - 1;
							if(uvs.size() > vInt) curUV = uvs.get(vInt);
							else curUV = new float[]{ 0, 0 };
							if(normals.size() > vInt){
								curNormals = normals.get(vInt);
							}
							else curNormals = new float[]{ 0, 0, 0 };
						}
						iNormal.add(new Vec3f(curNormals[0], curNormals[1], curNormals[2]));
						normal[0] += curNormals[0];
						normal[1] += curNormals[1];
						normal[2] += curNormals[2];
						if(vInt < verts0.size()){
							vc.add(verts0.get(vInt).setTexturePosition(curUV[0], curUV[1]));
						}
						if(ind > -1) s = s.substring(s.indexOf(" ") + 1).trim();
						else finalPhase++;
					}
					while(finalPhase < 1);
					float d = (float)Math.sqrt(normal[0] * normal[0] + normal[1] * normal[1] + normal[2] * normal[2]);
					normal[0] /= d;
					normal[1] /= d;
					normal[2] /= d;
					TexturedVertex[] vToArr = new TexturedVertex[vc.size()];
					for(int i = 0; i < vc.size(); i++){
						vToArr[i] = vc.get(i);
					}
					TexturedPolygon poly = new TexturedPolygon(vToArr);
					poly.setNormals(normal[0], normal[1], normal[2]);
					poly.setNormals(iNormal);
					faces.add(poly);
					verts1.addAll(vc);
					continue;
				}
			}
			vertices = new TexturedVertex[verts1.size()];
			for(int i = 0; i < verts1.size(); i++){
				vertices[i] = verts1.get(i);
			}
			polygons = new TexturedPolygon[faces.size()];
			for(int i = 0; i < faces.size(); i++){
				polygons[i] = faces.get(i);
				polygons[i].clearNormals();
			}
			in.close(); // Static.stop();
		}
		catch(Throwable e){
			e.printStackTrace();
			Static.stop();
		}
		return new Object[][]{ vertices, polygons };
	}

}
