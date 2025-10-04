package net.fexcraft.lib.tmt;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.common.utils.Print;
import net.fexcraft.mod.uni.EnvInfo;

/**
* Tool to parse `ModelRendererTurbo` objects from JSON.
* @Author Ferdinand Calo' (FEX___96)
*/
public class JsonToTMT {
	
	//def
	public static final float def = 0f;
	public static final int idef = 0;
	//common
	public static final String[] format  = new String[]{"format", "form", "f"};//1.1f
	public static final String[] width  = new String[]{"width",  "wid", "w"};
	public static final String[] height = new String[]{"height", "hgt", "h"};
	public static final String[] depth  = new String[]{"depth",  "dep", "d"};
	public static final String[] offx = new String[]{"offset_x", "off_x", "offx", "ox"};
	public static final String[] offy = new String[]{"offset_y", "off_y", "offy", "oy"};
	public static final String[] offz = new String[]{"offset_z", "off_z", "offz", "oz"};
	public static final String[] expansion = new String[]{"expansion", "exp", "e"};
	public static final String[] scale = new String[]{"scale", "s"};
	public static final String[] texturex = new String[]{"texture_x", "texturex", "tex_x", "tx"};
	public static final String[] texturey = new String[]{"texture_y", "texturey", "tex_y", "ty"};
	//
	public static final String[] posx = new String[]{"rotation_point_x", "pos_x", "posx", "px", "x"};
	public static final String[] posy = new String[]{"rotation_point_y", "pos_y", "posy", "py", "y"};
	public static final String[] posz = new String[]{"rotation_point_z", "pos_z", "posz", "pz", "z"};
	public static final String[] rotx = new String[]{"rotation_angle_x", "rotangle_x", "rotanglex", "rot_x", "rx"};
	public static final String[] roty = new String[]{"rotation_angle_y", "rotangle_y", "rotangley", "rot_y", "ry"};
	public static final String[] rotz = new String[]{"rotation_angle_z", "rotangle_z", "rotanglez", "rot_z", "rz"};
	//settings
	public static final String[] oldrot = new String[]{"old_ration", "old_rotation_order", "oro"};
	public static final String[] mirror = new String[]{"mirror", "mir", "m"};
	public static final String[] flip = new String[]{"flip", "fl", "usd"};
	//cyl
	public static final String[] radius = new String[]{"radius", "rad", "r"};
	public static final String[] radius2 = new String[]{"radius2", "rad2", "r2"};
	public static final String[] length = new String[]{"length", "len", "l"};
	public static final String[] segments = new String[]{"segments", "seg", "sg"};
	public static final String[] seglimit = new String[]{"segment_limit", "segments_limit", "seglimit", "seg_limit", "sgl"};
	public static final String[] basescale = new String[]{"base_scale", "basescale", "bs"};
	public static final String[] topscale = new String[]{"top_scale", "topscale", "ts"};
	public static final String[] direction = new String[]{"direction", "dir", "facing"};
	public static final String[] topoffx = new String[]{"top_offset_x", "topoff_x", "topoffx", "tox"};
	public static final String[] topoffy = new String[]{"top_offset_y", "topoff_y", "topoffy", "toy"};
	public static final String[] topoffz = new String[]{"top_offset_z", "topoff_z", "topoffz", "toz"};
	
	public final static ModelRendererTurbo parse(Object base, JsonMap map, int tx, int ty){
		ModelRendererTurbo model = new ModelRendererTurbo(base, get(texturex, map, idef), get(texturey, map, idef), tx, ty);
		//
		float x = get(offx, map, def);
		float y = get(offy, map, def);
		float z = get(offz, map, def);
		int w = get(width, map, idef);
		int h = get(height, map, idef);
		int d = get(depth, map, idef);
		//
		switch(map.get("type").string_value()){
			case "box": case "cube": case "b": {
				model.addBox(x, y, z, w, h, d, get(expansion, map, def));
				break;
			}
			case "shapebox": case "sbox": case "sb": {
				model.addShapeBox(x, y, z, w, h, d, get(scale, map, def),
						get("x0", map, def), get("y0", map, def), get("z0", map, def),
						get("x1", map, def), get("y1", map, def), get("z1", map, def),
						get("x2", map, def), get("y2", map, def), get("z2", map, def),
						get("x3", map, def), get("y3", map, def), get("z3", map, def),
						get("x4", map, def), get("y4", map, def), get("z4", map, def),
						get("x5", map, def), get("y5", map, def), get("z5", map, def),
						get("x6", map, def), get("y6", map, def), get("z6", map, def),
						get("x7", map, def), get("y7", map, def), get("z7", map, def)
					);
				break;
			}
			case "cylinder": case "cyl": case "c": {
				Vec3f offset = null; float tox = get(topoffx, map, 0f), toy = get(topoffy, map, 0f), toz = get(topoffz, map, 0f);
				if(tox != 0f && toy != 0f && toz != 0f) offset = new Vec3f(tox, toy, toz); float rad2 = get(radius, map, 0f);
				if(rad2 == 0f){
					model.addCylinder(x, y, z, get(radius, map, 1f), get(length, map, 1f), get(segments, map, 16), get(basescale, map, 1f), get(topscale, map, 1f), get(direction, map, 4), offset);
				}
				else{
					model.addHollowCylinder(x, y, z, get(radius, map, 1f), rad2, get(length, map, 1f), get(segments, map, 16),
						get(seglimit, map, 16), get(basescale, map, 1f), get(topscale, map, 1f), get(direction, map, 4), offset);
				}
				break;
			}
			case "cone": case "cn": {
				model.addCone(x, y, z, get(radius, map, 1f), get(length, map, 1f), get(segments, map, 12), get(basescale, map, 1f), get(direction, map, 4));
				break;
			}
			case "obj":{
				if(!map.has("location")){
					model.addSphere(x, y, z, 16, 16, 16, 16, 16);
					model.textured = false; //"error model"
				}
				else{
					String str = map.get("location").string_value();
					model.addObj(Static.getResource(str));
				}
			}
		}
		model.mirror = map.getBoolean(mirror[0], map.getBoolean(mirror[1], map.getBoolean(mirror[2], false)));
		model.flip = map.getBoolean(flip[0], map.getBoolean(flip[1], map.getBoolean(flip[2], false)));
		//
		model.rotationAngleX = get(rotx, map, def);
		model.rotationAngleY = get(roty, map, def);
		model.rotationAngleZ = get(rotz, map, def);
		//
		model.boxName = map.getString("name", null);
		model.setRotationPoint(get(posx, map, def), get(posy, map, def), get(posz, map, def));
		return model;
	}

	public final static ModelRendererTurbo[] parse(Object base, JsonArray array, int tx, int ty){
		if(array != null){
			ModelRendererTurbo[] model = new ModelRendererTurbo[array.size()];
			for(int i = 0; i < array.size(); i++){
				model[i] = parse(base, array.get(i).asMap(), tx, ty);
			}
			return model;
		}
		return new ModelRendererTurbo[0];
	}

	public final static ModelRendererTurbo[] parse(Object base, String string, JsonMap map, int tx, int ty){
		if(base == null){
			if(EnvInfo.DEV){
				Static.halt();
			}
			else{
				Print.console("Provided Modelbase is NULL, expect errors!", map);
			}
		}
		if(map.has(string) && map.get(string).isArray()){
			return parse(base, map.get(string).asArray(), tx, ty);
		}
		return new ModelRendererTurbo[0];
	}
	
	public static final float get(String s, JsonMap map, float def){
		return map.getFloat(s, def);
	}
	
	public static final float get(String[] s, JsonMap map, float def){
		for(String str : s){
			if(map.has(str)){
				return map.get(str).float_value();
			}
		}
		return 0;
	}
	
	public static final int get(String[] s, JsonMap map, int def){
		for(String str : s){
			if(map.has(str)){
				return map.get(str).integer_value();
			}
		}
		return 0;
	}
	
}