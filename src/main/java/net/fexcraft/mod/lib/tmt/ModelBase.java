package net.fexcraft.mod.lib.tmt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
* @Author Ferdinand (FEX___96)
* Similar to 'FlansMod'-type Models, for a fast convert.
*/
public class ModelBase extends Model<Object> {
	
	public static final ModelBase EMPTY;
	static {
		EMPTY = new ModelBase(){
			@Override public void render(){ return; }
			@Override public void render(ModelRendererTurbo[] model){ return; }
		};
	}
	
	public ModelRendererTurbo base[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo open[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo closed[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r1[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r2[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r3[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r4[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r5[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r6[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r7[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r8[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r9[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r0[] = new ModelRendererTurbo[0];
	   
	public void render(){
		render(base);
		render(open);
		render(closed);
		render(r0);
		render(r1);
		render(r2);
		render(r3);
		render(r4);
		render(r5);
		render(r6);
		render(r7);
		render(r8);
		render(r9);
	}
	
	public void render(ModelRendererTurbo[] model){
		for(ModelRendererTurbo mrt : model){
			mrt.render();
		}
	}
	
	protected void translate(ModelRendererTurbo[] model, float x, float y, float z){
		for(ModelRendererTurbo mod : model){
			mod.rotationPointX += x;
			mod.rotationPointY += y;
			mod.rotationPointZ += z;
		}
	}
	
	public void translateAll(float x, float y, float z){
		translate(base, x, y, z);
		translate(open, x, y, z);
		translate(closed, x, y, z);
		translate(r0, x, y, z);
		translate(r1, x, y, z);
		translate(r2, x, y, z);
		translate(r3, x, y, z);
		translate(r4, x, y, z);
		translate(r5, x, y, z);
		translate(r6, x, y, z);
		translate(r7, x, y, z);
		translate(r8, x, y, z);
		translate(r9, x, y, z);
	}

	@Override
	protected void rotate(ModelRendererTurbo[] model, float x, float y, float z) {
		for(ModelRendererTurbo mod : model){
			mod.rotateAngleX += x;
			mod.rotateAngleY += y;
			mod.rotateAngleZ += z;
		}
	}
	
	//JTMT
	
	protected static final float def = 0f;
	protected static final int idef = 0;
	
	protected ModelRendererTurbo[] parse(String string, JsonObject object, int tx, int ty){
		if(object.has(string)){
			JsonArray array = object.get(string).getAsJsonArray();
			ModelRendererTurbo[] model = new ModelRendererTurbo[array.size()];
			for(int i = 0; i < array.size(); i++){
				JsonObject obj = array.get(i).getAsJsonObject();
				model[i] = new ModelRendererTurbo(this, get("texture_x", obj, idef), get("texture_y", obj, idef), tx, ty);
				//
				float x = get("pos_x", obj, def);
				float y = get("pos_y", obj, def);
				float z = get("pos_z", obj, def);
				int w = get("width", obj, idef);
				int h = get("height", obj, idef);
				int d = get("depth", obj, idef);
				//
				switch(obj.get("type").getAsString()){
					case "box":
						model[i].addBox(x, y, z, w, h, d, get("expansion", obj, def));
						break;
					case "shapebox":
						model[i].addShapeBox(x, y, z, w, h, d, get("scale", obj, def),
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
				model[i].setRotationPoint(get("rotation_point_x", obj, def), get("rotation_point_y", obj, def), get("rotation_point_z", obj, def));
			}
			return model;
		}
		return new ModelRendererTurbo[0];
	}
	
	private static final float get(String s, JsonObject obj, float def){
		if(obj.has(s)){
			return obj.get(s).getAsFloat();
		}
		return def;
	}
	
	private static final int get(String s, JsonObject obj, int def){
		if(obj.has(s)){
			return obj.get(s).getAsInt();
		}
		return def;
	}

	@Override
	public void render(Object type, net.minecraft.entity.Entity element){
		return;
	}
	
	protected final void fixRotation(ModelRendererTurbo[] model, boolean... bools){
		if(bools.length >= 1 && bools[0]){
			for(ModelRendererTurbo mod : model){
				mod.rotateAngleX = -mod.rotateAngleX;
			}
		}
		if(bools.length >= 2 && bools[1]){
			for(ModelRendererTurbo mod : model){
				mod.rotateAngleY = -mod.rotateAngleY;
			}
		}
		if(bools.length >= 3 && bools[2]){
			for(ModelRendererTurbo mod : model){
				mod.rotateAngleZ = -mod.rotateAngleZ;
			}
		}
	}
	
}
