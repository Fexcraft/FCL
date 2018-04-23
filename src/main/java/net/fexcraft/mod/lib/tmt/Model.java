package net.fexcraft.mod.lib.tmt;

import net.fexcraft.mod.lib.util.common.Static;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
* Replaces the old `ModelBase` in this package.
* @Author Ferdinand Calo' (FEX___96)
*/

public abstract class Model<T> extends net.minecraft.client.model.ModelBase {
	
	public static final Model<Object> EMPTY;
	static {
		EMPTY = new Model<Object>(){
			@Override public void render(){}
			@Override public void render(Object type, Entity element){}
			@Override public void translateAll(float x, float y, float z){}
			@Override public void rotateAll(float x, float y, float z){}
		};
	}
	
	/** render whole model */
	public abstract void render();
	
	/** render sub-model array */
	public void render(ModelRendererTurbo[] model){
		for(ModelRendererTurbo sub : model){
			sub.render();
		}
	}
	
	public void render(ModelRendererTurbo[] model, float scale, boolean rotorder){
		for(ModelRendererTurbo sub : model){
			sub.render(scale, rotorder);
		}
	}
	
	/** render whole model based on data and entity */
	public abstract void render(T type, Entity entity);
	
	protected void translate(ModelRendererTurbo[] model, float x, float y, float z){
		for(ModelRendererTurbo mod : model){
			mod.rotationPointX += x;
			mod.rotationPointY += y;
			mod.rotationPointZ += z;
		}
	}
	
	public abstract void translateAll(float x, float y, float z);
	
	protected void rotate(ModelRendererTurbo[] model, float x, float y, float z){
		for(ModelRendererTurbo mod : model){
			mod.rotateAngleX += x;
			mod.rotateAngleY += y;
			mod.rotateAngleZ += z;
		}
	}
	
	public abstract void rotateAll(float x, float y, float z);
	
	/** Legacy Method */
	protected void flip(ModelRendererTurbo[] model){
		this.fixRotations(model);
	}
	
	/** Legacy Method */
	public void flipAll(){
		//To be overriden by extending classes.
	}
	
	/**
	 * Based on @EternalBlueFlame's fix.
	 * @param array ModelRendererTurbo Array
	 */
	public void fixRotations(ModelRendererTurbo[] array){
        for(ModelRendererTurbo model : array){
            if(model.isShape3D){
                model.rotateAngleY = -model.rotateAngleY;
                model.rotateAngleX = -model.rotateAngleX;
                model.rotateAngleZ = -model.rotateAngleZ + Static.rad180;
            }
            else{
                model.rotateAngleZ = -model.rotateAngleZ;
            }
        }
    }
	
	//private static ResourceLocation temploc = new ResourceLocation("fcl:temp_check");
	
	public static final void bindTexture(ResourceLocation rs){
		//if(temploc.equals(rs)){ return; } temploc = rs;
		net.minecraft.client.Minecraft.getMinecraft().renderEngine.bindTexture(rs);
		return;
	}
	
}