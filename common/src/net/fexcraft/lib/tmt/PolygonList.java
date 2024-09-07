package net.fexcraft.lib.tmt;

import net.fexcraft.lib.common.Static;

import java.util.ArrayList;

/**
 * Based on the FVTM "TurboList" as well as the FMT "TurboList" class.
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class PolygonList extends ArrayList<ModelRendererTurbo> {

	public static final PolygonList EMPTY = new PolygonList("fcl:empty");
	public String name;
	public boolean visible = true;
	public float scale = Static.sixteenth;

	public PolygonList(String name){
		super();
		this.name = name;
	}

	public PolygonList(String name, ModelRendererTurbo... mrts){
		this(name);
		for(ModelRendererTurbo mrt : mrts){
			this.add(mrt);
		}
	}

	public void render(){
		if(!visible) return;
		for(ModelRendererTurbo turbo : this){
			turbo.render(scale);
		}
	}

	public void renderPlain(){
		for(ModelRendererTurbo turbo : this){
			turbo.render(scale);
		}
	}

	public void translate(float x, float y, float z){
		for(ModelRendererTurbo mrt : this){
			mrt.rotationPointX += x;
			mrt.rotationPointY += y;
			mrt.rotationPointZ += z;
		}
	}

	public void scale(float flt){
		this.scale = flt;
	}

	public void rotate(float x, float y, float z){
		rotate(x, y, z, false);
	}

	public void rotate(float x, float y, float z, boolean apply){
		if(apply){
			for(ModelRendererTurbo mrt : this){
				mrt.rotationAngleX = x;
				mrt.rotationAngleY = y;
				mrt.rotationAngleZ = z;
			}
		}
		else{
			for(ModelRendererTurbo mrt : this){
				mrt.rotationAngleX += x;
				mrt.rotationAngleY += y;
				mrt.rotationAngleZ += z;
			}
		}
	}

	public void rotateAxis(float value, int axis, boolean apply){
		switch(axis){
			case 0:{
				if(apply){
					for(ModelRendererTurbo mrt : this)
						mrt.rotationAngleX = value;
				}
				else{
					for(ModelRendererTurbo mrt : this)
						mrt.rotationAngleX += value;
				}
				return;
			}
			case 1:{
				if(apply){
					for(ModelRendererTurbo mrt : this)
						mrt.rotationAngleY = value;
				}
				else{
					for(ModelRendererTurbo mrt : this)
						mrt.rotationAngleY += value;
				}
				return;
			}
			case 2:{
				if(apply){
					for(ModelRendererTurbo mrt : this)
						mrt.rotationAngleZ = value;
				}
				else{
					for(ModelRendererTurbo mrt : this)
						mrt.rotationAngleZ += value;
				}
				return;
			}
			default:
				return;
		}
	}

}
