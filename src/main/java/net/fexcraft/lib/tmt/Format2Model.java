package net.fexcraft.lib.tmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fexcraft.lib.common.json.JsonToTMT;
import net.fexcraft.lib.common.json.JsonUtil;
import net.fexcraft.lib.mc.utils.Static;
import net.minecraft.util.ResourceLocation;

/**
 * Basic multi-purpose model base class that can be initialized via "JTMT" format 2 JSONs.<br>
 * This format is based on the one in FVTM, but simpler and with less features.
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class Format2Model {

	private ArrayList<PolygonList> groups = new ArrayList<>();
	private ArrayList<String> creators = new ArrayList<>();
	public int textureX = 256, textureY = 256;

	public Format2Model(){}

	public Format2Model(JsonObject obj){
		this();
		if(obj == null){
			Static.exception(new Exception("MODEL JSON OBJ IS NULL, LOADING ABORTED"), false);
			return;
		}
		creators = JsonUtil.jsonArrayToStringArray(obj.get("creators").getAsJsonArray());
		textureX = obj.get("texture_size_x").getAsInt();
		textureY = obj.get("texture_size_y").getAsInt();
		try{
			if(JsonUtil.getIfExists(obj, "format", 2).intValue() == 1){
				JsonObject modelobj = obj.get("model").getAsJsonObject();
				for(Entry<String, JsonElement> entry : modelobj.entrySet()){
					groups.add(new PolygonList(entry.getKey(), JsonToTMT.parse(null, entry.getValue().getAsJsonArray(), textureX, textureY)));
				}
			}
			else{
				JsonObject modelobj = obj.get("groups").getAsJsonObject();
				for(Entry<String, JsonElement> entry : modelobj.entrySet()){
					groups.add(new PolygonList(entry.getKey(), JsonToTMT.parse(null, entry.getValue().getAsJsonObject().get("polygons").getAsJsonArray(), textureX, textureY)));
				}
			}
		}
		catch(Throwable thr){
			thr.printStackTrace();
			net.fexcraft.lib.mc.utils.Static.stop();
		}
	}

	public void render(){
		for(PolygonList list : groups){
			list.render();
		}
	}

	public PolygonList get(String name){
		for(PolygonList group : groups){
			if(group.name.equals(name)){ return group; }
		}
		return null;
	}

	public boolean addToCreators(String str){
		return creators.add(str);
	}

	public List<String> getCreators(){
		return ImmutableList.copyOf(creators);
	}

	public void translate(float x, float y, float z){
		groups.forEach(group -> group.translate(x, y, z));
	}

	public void rotate(float x, float y, float z, boolean apply){
		groups.forEach(group -> group.rotate(x, y, z, apply));
	}

	public void fixRotations(){
		groups.forEach(group -> fixRotations(group));
	}

	public void bindTexture(ResourceLocation texture){
		ModelBase.bindTexture(texture);
	}

	public static void fixRotations(PolygonList group){
		for(ModelRendererTurbo model : group){
			if(model.isShape3D){
				model.rotationAngleY = -model.rotationAngleY;
				model.rotationAngleX = -model.rotationAngleX;
				model.rotationAngleZ = -model.rotationAngleZ + 180f;
			}
			else{
				model.rotationAngleZ = -model.rotationAngleZ;
			}
		}
	}

	public void add(String key, ModelRendererTurbo[] mrts){
		this.groups.add(new PolygonList(key, mrts));
	}

	public PolygonList get(String string, boolean allownull){
		PolygonList list = get(string);
		return list == null ? allownull ? list : PolygonList.EMPTY : list;
	}

}
