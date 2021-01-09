package net.fexcraft.lib.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.TexturedPolygon;
import net.fexcraft.lib.common.math.TexturedVertex;
import net.fexcraft.lib.common.math.Vec3f;

/**
 * New OBJ Model Parser
 * 
 * Note that as of now, it stops reading comments after the first object definition,
 * the comment reading is mainly intended when there is data stored, like for FVTM.
 * 
 * @author Ferdinand Calo' (FEX___96)
 * 
 */
public class ObjParser {
	
	private boolean comments, uv = true, normals = true;
	private boolean objmode, flip, invert, model = true;
	private InputStream stream;
	
	public ObjParser(InputStream stream){
		this.stream = stream;
	}
	
	public ObjParser readComments(boolean bool){
		comments = bool;
		return this;
	}
	
	public ObjParser readModel(boolean bool){
		model = bool;
		return this;
	}
	
	public ObjParser skipUV(boolean bool){
		uv = !bool;
		return this;
	}
	
	public ObjParser noNormals(boolean bool){
		normals = !bool;
		return this;
	}
	
	public ObjParser flipAxes(boolean bool){
		flip = bool;
		return this;
	}
	
	public ObjParser objectMode(boolean bool){
		objmode = bool;
		return this;
	}
	
	public ObjParser groupMode(boolean bool){
		objmode = !bool;
		return this;
	}
	
	public ObjParser invertNormals(boolean bool){
		invert = !bool;
		return this;
	}
	
	public ObjModel parse(){
		ObjModel model = new ObjModel();
		ArrayList<TexturedVertex> raw_verts = new ArrayList<>();
		ArrayList<float[]> raw_uvs = new ArrayList<>();
		ArrayList<float[]> raw_normals = new ArrayList<>();
		Map<String, ArrayList<String[]>> faces = new LinkedHashMap<>();
		String s = null, group = null;
		String[] ss, fs;
		float[] uva = null;
		boolean norm = false, uvb = false;
		String gprefix = objmode ? "o " : "g ";
		int line = 0;
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			while((s = in.readLine()) != null){
				line++;
				s = s.trim();
				if(s.length() == 0) continue;
				if(s.startsWith("#")){
					if(comments) model.comments.add(s);
					continue;
				}
				else if(s.startsWith(gprefix)){
					if(!this.model) break;
					comments = false;
					group = s.substring(s.indexOf(" ") + 1).trim();
					model.polygons.put(group, new ArrayList<>());
					faces.put(group, new ArrayList<>());
					continue;
				}
				else if(!this.model) continue;
				else if(s.startsWith("v")){
					ss = s.substring(s.indexOf(" ") + 1).trim().split(" ");
					if(s.startsWith("v ")){
						if(ss.length < 3) continue;
						if(!flip) raw_verts.add(new TexturedVertex(p(ss[0]), p(ss[1]), p(ss[2]), 0, 0));
						else raw_verts.add(new TexturedVertex(p(ss[0]), -p(ss[2]), p(ss[1]), 0, 0));
						continue;
					}
					else if(s.startsWith("vt ")){
						if(!uv || ss.length < 2) continue;
						float u = p(ss[0]), v = p(ss[1]);
						raw_uvs.add(new float[]{ u < 0 ? -u : u, v < 0 ? -v : v });
					}
					else if(s.startsWith("vn ")){
						if(!normals || ss.length < 3) continue;
						raw_normals.add(new float[]{ p(ss[0]), p(ss[2]), p(ss[1]) });
					}
					continue;
				}
				else if(s.startsWith("f ")){
					faces.get(group).add(s.substring(s.indexOf(" ") + 1).trim().split(" "));
					continue;
				}
			}
			in.close();
			//
			for(Entry<String, ArrayList<String[]>> entry : faces.entrySet()){
				for(String[] as : entry.getValue()){
					ArrayList<TexturedVertex> verts = new ArrayList<>();
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
							verts.set(i, new TexturedVertex(verts.get(i), uva[0], uva[1]));
						}
					}
					TexturedPolygon poly = new TexturedPolygon(verts);
					if(norm){
						ArrayList<Vec3f> normals = new ArrayList<>();
						for(float[] fl : norms){
							normals.add(new Vec3f(fl[0], fl[1], fl[2]));
						}
						poly.setNormals(normals);
					}
					if(invert) poly.setInvert(invert);
					model.polygons.get(entry.getKey()).add(poly);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			Print.console("Exception on line " + line + "; " + s + " ; " + group);
			Static.stop();
		}
		return model;
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

	public static String getCommentValue(ObjModel model, String... prefixes){
		List<String> list = getCommentValues(model, prefixes, 1);
		return list.isEmpty() ? null : list.get(0);
	}
	
	public static String[] getCommentValue(ObjModel model, String[] prefixes, String divider){
		List<String[]> list = getCommentValues(model, prefixes, divider, 1);
		return list.isEmpty() ? null : list.get(0);
	}
	
	public static List<String> getCommentValues(ObjModel model, String[] prefixes, Integer limit){
		ArrayList<String> list = new ArrayList<>();
		if(model.comments == null || model.comments.isEmpty() || prefixes == null || prefixes.length == 0) return list;
		for(String comment : model.comments){
			for(String prefix : prefixes){
				try{
					if(!comment.startsWith(prefix)) continue;
					list.add(comment.substring(comment.indexOf(prefix) + prefix.length()).trim());
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	public static List<String[]> getCommentValues(ObjModel model, String[] prefixes, String divider, Integer limit){
		List<String> results = getCommentValues(model, prefixes, 1);
		ArrayList<String[]> list = new ArrayList<>();
		if(divider == null) divider = " ";
		if(results.isEmpty()) return list;
		for(String str : results) list.add(str.split(divider));
		return list;
	}
	
	
	public static class ObjModel {
		
		public final ArrayList<String> comments = new ArrayList<>();
		public final Map<String, ArrayList<TexturedPolygon>> polygons = new LinkedHashMap<>();
		
	}
	
}
