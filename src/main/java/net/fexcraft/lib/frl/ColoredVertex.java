package net.fexcraft.lib.frl;

import net.fexcraft.lib.common.math.Vec3f;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class ColoredVertex extends Vertex {
	
	protected Vec3f color;
	
	public ColoredVertex(Vec3f vec){
		super(vec);
	}
	
	public ColoredVertex(Vec3f vec, float u, float v){
		super(vec, u, v);
	}
	
	public ColoredVertex(Vertex vertex){
		super(vertex.vector, vertex.u, vertex.v);
		norm(vertex.norm);
	}

	@Override
	public Vec3f color(){
		return color;
	}
	
	@Override
	public Vertex color(Vec3f vec){
		this.color = vec;
		return this;
	}

}