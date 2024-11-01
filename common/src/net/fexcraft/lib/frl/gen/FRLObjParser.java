package net.fexcraft.lib.frl.gen;

import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.frl.Material;
import net.fexcraft.lib.frl.Polygon;
import net.fexcraft.lib.frl.Polyhedron;
import net.fexcraft.lib.frl.Vertex;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * OBJ Model Parser for FRL
 * 
 * @author Ferdinand Calo' (FEX___96)
 * 
 */
public class FRLObjParser {

	private boolean uv = true;
	private boolean normals = true;
	private boolean flip_f;
	private boolean flip_u;
	private boolean flip_v;
	private InputStream stream;
	private String id;

	public FRLObjParser(String uid, InputStream stream){
		this.stream = stream;
		id = uid;
	}
	
	public FRLObjParser uv(boolean bool){
		uv = !bool;
		return this;
	}
	
	public FRLObjParser normals(boolean bool){
		normals = bool;
		return this;
	}

	public FRLObjParser flipFaces(boolean bool){
		flip_f = bool;
		return this;
	}
	
	public FRLObjParser flipUV(boolean f_u, boolean f_v){
		flip_u = f_u;
		flip_v = f_v;
		return this;
	}
	
	public Map<String, ArrayList<Polyhedron>> parse(){
		Map<String, ArrayList<Polyhedron>> polygons = new LinkedHashMap<>();
		ArrayList<Vertex> raw_verts = new ArrayList<>();
		ArrayList<float[]> raw_uvs = new ArrayList<>();
		ArrayList<float[]> raw_normals = new ArrayList<>();
		Map<String, Map<Material, ArrayList<String[]>>> faces = new LinkedHashMap<>();
		Material mat = Material.NONE;
		String s = null, group = null;
		String[] ss, fs;
		float[] uva;
		boolean norm = false, uvb = false;
		int line = 0;
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			while((s = in.readLine()) != null){
				line++;
				s = s.trim();
				if(s.length() == 0) continue;
				if(s.startsWith("#")) continue;
				else if(s.startsWith("o ") || s.startsWith("g ")){
					group = s.substring(s.indexOf(" ") + 1).trim();
					if(!polygons.containsKey(group)) polygons.put(group, new ArrayList<>());
					if(!faces.containsKey(group)) faces.put(group, new LinkedHashMap<>());
					if(!faces.get(group).containsKey(mat)) faces.get(group).put(mat, new ArrayList<>());
					continue;
				}
				else if(s.startsWith("v")){
					ss = s.substring(s.indexOf(" ") + 1).trim().split(" ");
					if(s.startsWith("v ")){
						if(ss.length < 3) continue;
						raw_verts.add(new Vertex(p(ss[0]), p(ss[1]), p(ss[2])));
						continue;
					}
					else if(s.startsWith("vt ")){
						if(!uv || ss.length < 2) continue;
						float u = p(ss[0]), v = p(ss[1]);
						if(flip_u) u = -u + 1;
						if(flip_v) v = -v + 1;
						raw_uvs.add(new float[]{ u, v });
					}
					else if(s.startsWith("vn ")){
						if(!normals || ss.length < 3) continue;
						raw_normals.add(new float[]{ p(ss[0]), p(ss[2]), p(ss[1]) });
					}
					continue;
				}
				else if(s.startsWith("usemtl")){
					mat = Material.get(id + "?" + s.substring(s.indexOf(" ") + 1), true);
					if(!faces.get(group).containsKey(mat)) faces.get(group).put(mat, new ArrayList<>());
				}
				else if(s.startsWith("f ")){
					faces.get(group).get(mat).add(s.substring(s.indexOf(" ") + 1).trim().split(" "));
					continue;
				}
			}
			in.close();
			//
			for(Entry<String, Map<Material, ArrayList<String[]>>> entry : faces.entrySet()){
				for(Entry<Material, ArrayList<String[]>> metry : entry.getValue().entrySet()){
					Polyhedron hedron = new Polyhedron();
					hedron.glObj.material = metry.getKey();
					for(String[] as : metry.getValue()){
						ArrayList<Vertex> verts = new ArrayList<>();
						ArrayList<float[]> norms = new ArrayList<>();
						ArrayList<float[]> uvs = new ArrayList<>();
						for(int i = 0; i < as.length; i++){
							if(as[i].contains("/")){
								if(as[i].contains("//")){
									if(i == 0){
										uvb = false;
										norm = normals;
									}
									fs = as[i].split("//");
									verts.add(raw_verts.get(pi(fs[0])));
									if(norm) norms.add(raw_normals.get(pi(fs[1])));
								}
								else{
									fs = as[i].split("/");
									if(i == 0){
										uvb = uv;
										norm = normals && fs.length > 2;
									}
									verts.add(raw_verts.get(pi(fs[0])));
									if(uvb) uvs.add(raw_uvs.get(pi(fs[1])));
									if(norm) norms.add(raw_normals.get(pi(fs[2])));
								}
							}
							else{
								if(i == 0){
									uvb = false;
									norm = false;
								}
								verts.add(raw_verts.get(pi(as[i])));
							}
						}
						if(uvb){
							for(int i = 0; i < uvs.size(); i++){
								uva = uvs.get(i);
								verts.set(i, new Vertex(verts.get(i)).uv(uva[0], uva[1]));
							}
						}
						Polygon poly = new Polygon(verts);
						if(norm){
							for(int i = 0; i < norms.size(); i++){
								if(i >= poly.vertices.length) break;
								float[] fl = norms.get(i);
								poly.vertices[i].norm(new Vec3f(fl[0], fl[1], fl[2]));
							}
						}
						if(flip_f) poly.flip();
						hedron.polygons.add(poly);
					}
					polygons.get(entry.getKey()).add(hedron);
				}
			}
		}
		catch(Exception e){
			System.out.println("Exception on line " + line + "; " + s + " ; " + group + "; " + e.getMessage());
			e.printStackTrace();
		}
		return polygons;
	}
	
	private float p(String string){
		try{
			return Float.parseFloat(string);
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	private int pi(String string){
		try{
			return Integer.parseInt(string) - 1;
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
}
