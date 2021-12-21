package net.fexcraft.lib.frl;

import java.util.ArrayList;

import net.fexcraft.lib.common.math.RGB;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class Polyhedron<GLO> {
	
	public ArrayList<Polygon> polygons = new ArrayList<>();
	public ArrayList<Polyhedron<?>> sub;
	public float rotX, rotY, rotZ;
	public float posX, posY, posZ;
	public boolean recompile;
	public RotationOrder rotOrder = RotationOrder.YZX;
	public String name;
	public GLO glObj;
	public Integer glId;
	
	public Polyhedron(){}
	
	public Polyhedron(String name){
		this.name = name;
	}
	
	public Polyhedron<GLO> rescale(float scale){
		for(Polygon gon : polygons) gon.rescale(scale);
		posX *= scale;
		posY *= scale;
		posZ *= scale;
		return this;
	}
	
	public Polyhedron<GLO> color(RGB color){
		for(Polygon gon : polygons) gon.color(color);
		return this;
	}

	public void render(){
		Renderer.render(this);
	}

}
