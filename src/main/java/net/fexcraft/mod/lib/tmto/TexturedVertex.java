package net.fexcraft.mod.lib.tmto;

import net.fexcraft.mod.lib.util.math.Vec3f;

public class TexturedVertex {
	
	public Vec3f vector;
	public float texX, texY;

	public TexturedVertex(Vec3f vec, float x, float y){
		vector = vec; texX = x; texY = y;
	}

	public TexturedVertex(float x, float y, float z, float u, float v){
		this(new Vec3f(x, y, z), u, v);
	}

	public TexturedVertex(TexturedVertex texver, float x, float y){
		vector = texver.vector; texX = x; texY = y;
	}

	public TexturedVertex setTexturePosition(float x, float y){
		texX = x; texY = y; return this;
	}
	
}
