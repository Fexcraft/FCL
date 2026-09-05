package net.fexcraft.lib.common.math;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class TexturedVertex {
	
	public V3F vector;
	public float textureX, textureY;

	public TexturedVertex(V3F vec, float x, float y){
		vector = vec; textureX = x; textureY = y;
	}

	public TexturedVertex(float x, float y, float z, float u, float v){
		this(new V3F(x, y, z), u, v);
	}

	public TexturedVertex(TexturedVertex texver, float x, float y){
		vector = new V3F(texver.vector); textureX = x; textureY = y;
	}

	public TexturedVertex(TexturedVertex other){
		vector = new V3F(other.vector);
		textureX = other.textureX; textureY = other.textureY;
	}

    public TexturedVertex(V3D vec, float u, float v){
		vector = new V3F(vec.x, vec.y, vec.z);
		textureX = u;
		textureY = v;
    }

    public TexturedVertex setTexturePosition(float x, float y){
		return new TexturedVertex(this, x, y);
	}
	
	public TexturedVertex setTexturePosition(double x, double y){
		return new TexturedVertex(this, (float)x, (float)y);
	}
	
}