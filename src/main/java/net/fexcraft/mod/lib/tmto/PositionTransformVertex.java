package net.fexcraft.mod.lib.tmto;

import java.util.ArrayList;

import net.fexcraft.mod.lib.util.math.Vec3f;

public class PositionTransformVertex extends TexturedVertex {
	
	public Vec3f neutralVector;
	public ArrayList<TransformGroup> transformGroups = new ArrayList<TransformGroup>();

	public PositionTransformVertex(float x, float y, float z, float u, float v){
		this(new Vec3f(x, y, z), u, v);
	}
	
	public PositionTransformVertex(TexturedVertex vertex, float u, float v){
		super(vertex, u, v);
		if(vertex instanceof PositionTransformVertex){
			neutralVector = ((PositionTransformVertex)vertex).neutralVector;
		}
		else{
			neutralVector = new Vec3f(vertex.vector.xCoord, vertex.vector.yCoord, vertex.vector.zCoord);
		}
	}
	
	public PositionTransformVertex(TexturedVertex vertex){
		this(vertex, vertex.texX, vertex.texY);
	}
	
	public PositionTransformVertex(Vec3f vector, float u, float v){
		super(vector, u, v);
		neutralVector = new Vec3f(vector.xCoord, vector.yCoord, vector.zCoord);
	}
	
	public void setTransformation(){
		if(transformGroups.size() == 0){
			vector = neutralVector;
			return;
		}
		float weight = 0f;
		for(TransformGroup group : transformGroups){
			weight += group.getWeight();
		}
		vector = new Vec3f(0, 0, 0);
		for(int i = 0; i < transformGroups.size(); i++){
			TransformGroup group = transformGroups.get(i);
			float cWeight = group.getWeight() / weight;
			Vec3f vector = group.doTransformation(this);
			vector.addVector(cWeight * vector.xCoord, cWeight * vector.yCoord, cWeight * vector.zCoord);
		}
	}
	
	public void addGroup(TransformGroup group){
		transformGroups.add(group);
	}
	
	public void removeGroup(TransformGroup group){
		transformGroups.remove(group);
	}
	
}
