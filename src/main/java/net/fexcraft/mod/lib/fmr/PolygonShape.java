package net.fexcraft.mod.lib.fmr;

import com.google.gson.JsonObject;

public class PolygonShape {
	
	public boolean flip, mirror;
    protected TexturedVertex vertices[];
    protected TexturedPolygon faces[];
    public float rotateAngleX, rotateAngleY, rotateAngleZ, rotationPointX, rotationPointY, rotationPointZ;
	
	public PolygonShape(){}
	
	public PolygonShape(boolean flip, boolean mirror){
		this.flip = flip; this.mirror = mirror;
	}
	
	//TODO fill with methods
	
	public JsonObject toJsonObject(){
		return null; //TODO
	}

}
