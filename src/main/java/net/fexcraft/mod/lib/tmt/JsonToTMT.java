package net.fexcraft.mod.lib.tmt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.fexcraft.mod.lib.util.common.Print;
import net.fexcraft.mod.lib.util.common.Static;
import net.fexcraft.mod.lib.util.json.JsonUtil;

/**
* Tool to parse `ModelRendererTurbo` objects from JSON.
* @Author Ferdinand Calo' (FEX___96)
*/
public class JsonToTMT {
	
	//def
	protected static final float def = 0f;
	protected static final int idef = 0;
	//common
	protected static final String[] format  = new String[]{"format", "form", "f"};
	protected static final String[] width  = new String[]{"width",  "wid", "w"};
	protected static final String[] height = new String[]{"height", "hgt", "h"};
	protected static final String[] depth  = new String[]{"depth",  "dep", "d"};
	protected static final String[] posx = new String[]{"pos_x", "posx", "x"};
	protected static final String[] posy = new String[]{"pos_y", "posy", "y"};
	protected static final String[] posz = new String[]{"pos_z", "posz", "z"};
	protected static final String[] expansion = new String[]{"expansion", "exp", "e"};
	protected static final String[] scale = new String[]{"scale", "s"};
	protected static final String[] texturex = new String[]{"texture_x", "texturex", "tex_x", "tx"};
	protected static final String[] texturey = new String[]{"texture_y", "texturey", "tex_y", "ty"};
	//
	protected static final String[] rotpointx = new String[]{"rotation_point_x", "rotpoint_x", "rotpointx", "rot_x", "rx"};
	protected static final String[] rotpointy = new String[]{"rotation_point_y", "rotpoint_y", "rotpointy", "rot_y", "ry"};
	protected static final String[] rotpointz = new String[]{"rotation_point_z", "rotpoint_z", "rotpointz", "rot_z", "rz"};
	//settings
	protected static final String[] oldrot = new String[]{"old_ration", "old_rotation_order", "oro"};
	protected static final String[] mirror = new String[]{"mirror", "mir", "m"};
	protected static final String[] flip = new String[]{"flip", "fl", "usd"};
	//cyl
	protected static final String[] radius = new String[]{"radius", "rad", "r"};
	protected static final String[] length = new String[]{"length", "len", "l"};
	protected static final String[] segments = new String[]{"segments", "seg", "sg"};
	protected static final String[] basescale = new String[]{"base_scale", "basescale", "bs"};
	protected static final String[] topscale = new String[]{"top_scale", "topscale", "ts"};
	protected static final String[] direction = new String[]{"direction", "dir", "facing"};
	
	public final static ModelRendererTurbo parse(net.minecraft.client.model.ModelBase base, JsonObject obj, int tx, int ty){
		ModelRendererTurbo model = new ModelRendererTurbo(base, get(texturex, obj, idef), get(texturey, obj, idef), tx, ty);
		//
		float x = get(posx, obj, def);
		float y = get(posy, obj, def);
		float z = get(posz, obj, def);
		int w = get(width, obj, idef);
		int h = get(height, obj, idef);
		int d = get(depth, obj, idef);
		//
		switch(obj.get("type").getAsString()){
			case "box": case "cube": case "b": {
				model.addBox(x, y, z, w, h, d, get(expansion, obj, def));
				break;
			}
			case "shapebox": case "sb": {
				model.addShapeBox(x, y, z, w, h, d, get(scale, obj, def),
						get("x0", obj, def), get("y0", obj, def), get("z0", obj, def),
						get("x1", obj, def), get("y1", obj, def), get("z1", obj, def),
						get("x2", obj, def), get("y2", obj, def), get("z2", obj, def),
						get("x3", obj, def), get("y3", obj, def), get("z3", obj, def),
						get("x4", obj, def), get("y4", obj, def), get("z4", obj, def),
						get("x5", obj, def), get("y5", obj, def), get("z5", obj, def),
						get("x6", obj, def), get("y6", obj, def), get("z6", obj, def),
						get("x7", obj, def), get("y7", obj, def), get("z7", obj, def)
					);
				break;
			}
			case "cylinder": case "cyl": {
				model.addCylinder(x, y, z, get(radius, obj, 1f), get(length, obj, 1f), get(segments, obj, 16), get(basescale, obj, 1f), get(topscale, obj, 1f), get(direction, obj, 4));
				break;
			}
		}
		//
		model.rotorder = JsonUtil.getIfExists(obj, oldrot, model.rotorder);
		model.mirror = JsonUtil.getIfExists(obj, mirror, false);
		model.flip = JsonUtil.getIfExists(obj, flip, false);
		//
		model.setRotationPoint(get(rotpointx, obj, def), get(rotpointy, obj, def), get(rotpointz, obj, def));
		return model;
	}

	public final static ModelRendererTurbo[] parse(net.minecraft.client.model.ModelBase base, JsonArray array, int tx, int ty){
		if(array != null){
			ModelRendererTurbo[] model = new ModelRendererTurbo[array.size()];
			for(int i = 0; i < array.size(); i++){
				model[i] = parse(base, array.get(i).getAsJsonObject(), tx, ty);
			}
			return model;
		}
		return new ModelRendererTurbo[0];
	}

	public final static ModelRendererTurbo[] parse(net.minecraft.client.model.ModelBase base, String string, JsonObject object, int tx, int ty){
		if(base == null){
			if(Static.dev()){
				Static.halt();
			}
			else{
				Print.log("Provided Modelbase is NULL, expect errors!", object);
			}
		}
		if(object.has(string) && object.get(string).isJsonArray()){
			return parse(base, object.get(string).getAsJsonArray(), tx, ty);
		}
		return new ModelRendererTurbo[0];
	}
	
	private static final float get(String s, JsonObject obj, float def){
		if(obj.has(s)){
			return obj.get(s).getAsFloat();
		}
		return def;
	}
	
	private static final float get(String[] s, JsonObject obj, float def){
		for(String str : s){
			if(obj.has(str)){
				return obj.get(str).getAsFloat();
			}
		}
		return 0;
	}
	
	private static final int get(String[] s, JsonObject obj, int def){
		for(String str : s){
			if(obj.has(str)){
				return obj.get(str).getAsInt();
			}
		}
		return 0;
	}
	
}