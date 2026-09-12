package net.fexcraft.lib.frl;

import static net.fexcraft.lib.frl.Renderer.RENDERER;

import java.util.ArrayList;

import net.fexcraft.lib.common.math.*;
import net.fexcraft.lib.frl.gen.Generator;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class Polyhedron {
	
	public ArrayList<Polygon> polygons = new ArrayList<>();
	public ArrayList<Polyhedron> sub;
	public float rotX, rotY, rotZ;
	public float posX, posY, posZ;
	public float texU, texV;
	public boolean recompile, visible = true;
	public RotationOrder rotOrder = RotationOrder.YXZ;//YZX;
	public String name;
	public GLO glObj = GLO.SUPPLIER.get();
	public Integer glId;
	
	public Polyhedron(){}
	
	public Polyhedron(String name){
		this.name = name;
	}
	
	public Polyhedron rescale(float scale){
		for(Polygon gon : polygons) gon.rescale(scale);
		posX *= scale;
		posY *= scale;
		posZ *= scale;
		return this;
	}
	
	public Polyhedron color(RGB color){
		for(Polygon gon : polygons) gon.color(color);
		return this;
	}

	public void render(){
		RENDERER.render(this);
	}
	
	public void clear(){
		polygons.clear();
	}

	public void delete(){
		RENDERER.delete(this);
	}
	
	public <GL extends GLO> Polyhedron glObj(GL newobj){
		this.glObj = newobj;
		return this;
	}

	public <GL extends GLO> GL glObj(){
		return (GL)glObj;
	}

	public <GL extends GLO> GL glObj(Class<GL> clazz){
		return (GL)glObj;
	}

	public Polyhedron pos(float x, float y, float z){
		posX = x;
		posY = y;
		posZ = z;
		return this;
	}

	public Polyhedron rot(float x, float y, float z){
		rotX = x;
		rotY = y;
		rotZ = z;
		return this;
	}

	public Polyhedron pos(double x, double y, double z){
		posX = (float)x;
		posY = (float)y;
		posZ = (float)z;
		return this;
	}

	public Polyhedron rot(double x, double y, double z){
		rotX = (float)x;
		rotY = (float)y;
		rotZ = (float)z;
		return this;
	}

	public Polyhedron pos(V3I vec){
		posX = vec.x;
		posY = vec.y;
		posZ = vec.z;
		return this;
	}

	public Polyhedron pos(V3D vec){
		posX = (float)vec.x;
		posY = (float)vec.y;
		posZ = (float)vec.z;
		return this;
	}

	public Generator newGen(){
		return new Generator(this);
	}

	public Polyhedron copy(boolean full){
		Polyhedron hed = new Polyhedron();
		hed.glObj.copy(glObj, full);
		hed.visible = visible;
		hed.pos(posX, posY, posZ);
		hed.rot(rotX, rotY, rotZ);
		hed.rotOrder = rotOrder;
		hed.texU = texU;
		hed.texV = texV;
		hed.name = name;
		if(full){
			for(Polygon poly : polygons){
				hed.polygons.add(poly.copy(full));
			}
		}
		else hed.polygons.addAll(polygons);
		return hed;
	}

	public Polyhedron genNorm(){
		for(Polygon polygon : polygons){
			polygon.genNorm();
		}
		return this;
	}

}
