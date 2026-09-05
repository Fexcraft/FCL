package net.fexcraft.lib.frl;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3F;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class Vertex {
	
	public V3F COLOR_BLACK = new V3F(0, 0, 0);
	public V3F COLOR_RED   = new V3F(1, 0, 0);
	public V3F COLOR_BLUE  = new V3F(0, 0, 1);
	public V3F COLOR_GREEN = new V3F(0, 1, 0);
	public V3F COLOR_WHITE = new V3F(1, 1, 1);
	
	public V3F vector;
	public float u, v;
	public V3F norm;
	
	public Vertex(V3F vec){
		this.vector = vec;
	}
	
	public Vertex(float[] vec){
		this.vector = new V3F(vec[0], vec[1], vec[2]);
	}
	
	public Vertex(float x, float y, float z){
		this.vector = new V3F(x, y, z);
	}
	
	public Vertex(V3F vec, float u, float v){
		this(vec);
		this.u = u;
		this.v = v;
	}

	public Vertex(Vertex vertex){
		this(vertex.vector.x, vertex.vector.y, vertex.vector.z);
	}

	public Vertex(double x, double y, double z){
		this.vector = new V3F(x, y, z);
	}

	public Vertex(V3D vec){
		this.vector = new V3F(vec.x, vec.y, vec.z);
	}

	public Vertex(V3D vec, float u, float v){
		this(vec);
		this.u = u;
		this.v = v;
	}

	public void pos(float x, float y, float z){
		vector.x = x;
		vector.y = y;
		vector.z = z;
	}

	public Vertex pos(double x, double y, double z){
		vector.x = (float)x;
		vector.y = (float)y;
		vector.z = (float)z;
		return this;
	}

	public V3F color(){
		return COLOR_WHITE;
	}
	
	public Vertex color(V3F vec){
		return this;
	}

	public Vertex color(float r, float g, float b){
		return this;
	}
	
	public Vertex norm(V3F vec){
		this.norm = vec;
		return this;
	}
	
	public Vertex uv(float x, float y){
		this.u = x;
		this.v = y;
		return this;
	}

	public Vertex nauv(float u, float v){
		return new Vertex(this).uv(u, v);
	}

	public Vertex copy(){
		Vertex vert = null;
		if(this instanceof ColoredVertex){
			vert = new ColoredVertex(vector.copy());
			vert.color(color().copy());
		}
		else{
			vert = new Vertex(vector.copy());
		}
		return vert.uv(u, v).norm(norm == null ? null : norm.copy());
	}

}
