package net.fexcraft.lib.tmt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.fexcraft.lib.mc.render.FCLBlockModel;

/**
* Similar to 'FlansMod'-type Models, for a fast convert.
* @Author Ferdinand Calo' (FEX___96)
*/
public class GenericModelBase extends ModelBase implements FCLBlockModel {
	
	public ModelRendererTurbo base[] = new ModelRendererTurbo[0], open[] = new ModelRendererTurbo[0], closed[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r0[] = new ModelRendererTurbo[0], r1[] = new ModelRendererTurbo[0], r2[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r3[] = new ModelRendererTurbo[0], r4[] = new ModelRendererTurbo[0], r5[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r6[] = new ModelRendererTurbo[0], r7[] = new ModelRendererTurbo[0], r8[] = new ModelRendererTurbo[0];
	public ModelRendererTurbo r9[] = new ModelRendererTurbo[0];
	   
	public void render(){
		render(base);
		render(open); render(closed); render(r0);
		render(r1); render(r2); render(r3);
		render(r4); render(r5); render(r6);
		render(r7); render(r8); render(r9);
	}
	
	@Override
	public void translate(float x, float y, float z){
		translate(base, x, y, z);
		translate(open, x, y, z); translate(closed, x, y, z); translate(r0, x, y, z);
		translate(r1, x, y, z); translate(r2, x, y, z); translate(r3, x, y, z);
		translate(r4, x, y, z); translate(r5, x, y, z); translate(r6, x, y, z);
		translate(r7, x, y, z); translate(r8, x, y, z); translate(r9, x, y, z);
	}

	@Override
	public void rotate(float x, float y, float z){
		rotate(base, x, y, z);
		rotate(open, x, y, z); rotate(closed, x, y, z); rotate(r0, x, y, z);
		rotate(r1, x, y, z); rotate(r2, x, y, z); rotate(r3, x, y, z);
		rotate(r4, x, y, z); rotate(r5, x, y, z); rotate(r6, x, y, z);
		rotate(r7, x, y, z); rotate(r8, x, y, z); rotate(r9, x, y, z);
	}

	@Override
	public Collection<ModelRendererTurbo> getPolygons(Map<String, String> args){
		ArrayList<ModelRendererTurbo> mrts = new ArrayList<>();
		addAll(mrts, base);
		addAll(mrts, open);
		addAll(mrts, closed);
		addAll(mrts, r0);
		addAll(mrts, r1);
		addAll(mrts, r2);
		addAll(mrts, r3);
		addAll(mrts, r4);
		addAll(mrts, r5);
		addAll(mrts, r6);
		addAll(mrts, r7);
		addAll(mrts, r8);
		addAll(mrts, r9);
		return mrts;
	}

	private void addAll(ArrayList<ModelRendererTurbo> mrts, ModelRendererTurbo[] array){
		for(ModelRendererTurbo model : array) mrts.add(model);
	}
	
}
