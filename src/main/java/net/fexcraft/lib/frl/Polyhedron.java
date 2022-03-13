package net.fexcraft.lib.frl;

import static net.fexcraft.lib.frl.Renderer.RENDERER;

import java.util.ArrayList;

import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.common.math.TexturedPolygon;
import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.tmt.ModelRendererTurbo;

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
	public float texU, texV;
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
		RENDERER.render(this);
	}
	
	public Polyhedron<GLO> importMRT(ModelRendererTurbo turbo, float scale){
		this.name = turbo.boxName;
		for(TexturedPolygon tp : turbo.getFaces()){
			Vertex[] verts = new Vertex[tp.getVertices().length];
			for(int i = 0; i < verts.length; i++){
				verts[i] = new ColoredVertex(new Vec3f(tp.getVertices()[i].vector), tp.getVertices()[i].textureX, tp.getVertices()[i].textureY);
		        Vec3f vec0 = new Vec3f(tp.getVertices()[1].vector.sub(tp.getVertices()[0].vector));
		        Vec3f vec1 = new Vec3f(tp.getVertices()[1].vector.sub(tp.getVertices()[2].vector));
		        Vec3f vec2 = vec1.cross(vec0).normalize();
				verts[i].norm(vec2);
				//verts[i].color(1, i == 2 || i == 3 ? 1 : 0, 0);
			}
			polygons.add(new Polygon(verts));//.colored(true));
		}
		posX = turbo.rotationPointX;
		posY = turbo.rotationPointY;
		posZ = turbo.rotationPointZ;
		rotX = turbo.rotationAngleX;
		rotY = turbo.rotationAngleY;
		rotZ = turbo.rotationAngleZ;
		texU = turbo.texoffx;
		texV = turbo.texoffy;
		if(scale != 0f && scale != 1f) rescale(scale);
		return this;
	}
	
	public void clear(){
		polygons.clear();
		Renderer.RENDERER.delete(this);
	}

}
