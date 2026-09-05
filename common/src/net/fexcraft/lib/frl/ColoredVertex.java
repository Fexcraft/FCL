package net.fexcraft.lib.frl;

import net.fexcraft.lib.common.math.V3F;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class ColoredVertex extends Vertex {
	
	protected V3F color = new V3F(1, 1, 1);
	
	public ColoredVertex(V3F vec){
		super(vec);
	}
	
	public ColoredVertex(V3F vec, float u, float v){
		super(vec, u, v);
	}
	
	public ColoredVertex(Vertex vertex){
		super(vertex.vector, vertex.u, vertex.v);
		norm(vertex.norm);
	}

	@Override
	public V3F color(){
		return color;
	}
	
	@Override
	public Vertex color(V3F vec){
		this.color = vec;
		return this;
	}

	@Override
	public Vertex color(float r, float g, float b){
		return color(new V3F(r, g, b));
	}

}
