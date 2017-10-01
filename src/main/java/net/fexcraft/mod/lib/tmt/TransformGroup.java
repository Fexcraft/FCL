package net.fexcraft.mod.lib.tmt;

import net.fexcraft.mod.lib.util.math.Vec3f;

public abstract class TransformGroup {
	
	public abstract double getWeight();
	public abstract Vec3f doTransformation(PositionTransformVertex vertex);
	
}
