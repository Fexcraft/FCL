package net.fexcraft.mod.lib.tmto;

import net.fexcraft.mod.lib.util.math.Vec3f;

public abstract class TransformGroup {
	
	public abstract float getWeight();
	public abstract Vec3f doTransformation(PositionTransformVertex vertex);
	
}
