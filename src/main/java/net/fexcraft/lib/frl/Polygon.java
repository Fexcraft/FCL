package net.fexcraft.lib.frl;

import java.util.Collection;

import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.common.math.Vec3f;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class Polygon {

	public Vertex[] vertices;
	public boolean colored;
	public boolean lines, textured;
	
	public Polygon(int edges){
		vertices = new Vertex[edges];
		for(int i = 0; i < edges; i++) vertices[i] = new Vertex(new Vec3f());
	}
	
	public Polygon(Vertex[] verts){
		this.vertices = verts;
	}
	
	public Polygon(Collection<Vertex> verts){
		this.vertices = verts.toArray(new Vertex[0]);
	}
	
	public Polygon color(RGB color){
		if(colored = color != null){
			Vec3f col = new Vec3f(color.toFloatArray(), 0);
			for(int i = 0; i < vertices.length; i++){
				if(vertices[i] instanceof ColoredVertex == false){
					vertices[i] = new ColoredVertex(vertices[i]);
				}
				vertices[i].color(col);
			}
		}
		return this;
	}
	
	public Polygon lines(boolean bool){
		lines = bool;
		return this;
	}
	
	public Polygon textured(boolean bool){
		textured = bool;
		return this;
	}

	public void rescale(float scale){
		for(Vertex vert : vertices) vert.vector = vert.vector.scale(scale);
	}
	
}
