package net.fexcraft.lib.frl;

import org.lwjgl.opengl.GL11;

public enum RotationOrder {
	
	XYZ(new int[]{ 0, 1, 2 }){
		@Override
		public void rotate(Polyhedron<?> mrt){
	        if(mrt.rotX != 0.0F) GL11.glRotatef(mrt.rotX, 1.0F, 0.0F, 0.0F);
			if(mrt.rotY != 0.0F) GL11.glRotatef(mrt.rotY, 0.0F, 1.0F, 0.0F);
	        if(mrt.rotZ != 0.0F) GL11.glRotatef(mrt.rotZ, 0.0F, 0.0F, 1.0F);
		}
	},
	XZY(new int[]{ 0, 2, 1 }){
		@Override
		public void rotate(Polyhedron<?> mrt){
	        if(mrt.rotX != 0.0F) GL11.glRotatef(mrt.rotX, 1.0F, 0.0F, 0.0F);
	        if(mrt.rotZ != 0.0F) GL11.glRotatef(mrt.rotZ, 0.0F, 0.0F, 1.0F);
			if(mrt.rotY != 0.0F) GL11.glRotatef(mrt.rotY, 0.0F, 1.0F, 0.0F);
		}
	},
	YXZ(new int[]{ 1, 0, 2 }){
		@Override
		public void rotate(Polyhedron<?> mrt){
			if(mrt.rotY != 0.0F) GL11.glRotatef(mrt.rotY, 0.0F, 1.0F, 0.0F);
	        if(mrt.rotX != 0.0F) GL11.glRotatef(mrt.rotX, 1.0F, 0.0F, 0.0F);
	        if(mrt.rotZ != 0.0F) GL11.glRotatef(mrt.rotZ, 0.0F, 0.0F, 1.0F);
		}
	},
	YZX(new int[]{ 1, 2, 0 }){
		@Override
		public void rotate(Polyhedron<?> mrt){
			if(mrt.rotY != 0.0F) GL11.glRotatef(mrt.rotY, 0.0F, 1.0F, 0.0F);
	        if(mrt.rotZ != 0.0F) GL11.glRotatef(mrt.rotZ, 0.0F, 0.0F, 1.0F);
	        if(mrt.rotX != 0.0F) GL11.glRotatef(mrt.rotX, 1.0F, 0.0F, 0.0F);
		}
	},
	ZXY(new int[]{ 2, 0, 1 }){
		@Override
		public void rotate(Polyhedron<?> mrt){
	        if(mrt.rotZ != 0.0F) GL11.glRotatef(mrt.rotZ, 0.0F, 0.0F, 1.0F);
	        if(mrt.rotX != 0.0F) GL11.glRotatef(mrt.rotX, 1.0F, 0.0F, 0.0F);
			if(mrt.rotY != 0.0F) GL11.glRotatef(mrt.rotY, 0.0F, 1.0F, 0.0F);
		}
	},
	ZYX(new int[]{ 2, 1, 0 }){
		@Override
		public void rotate(Polyhedron<?> mrt){
	        if(mrt.rotZ != 0.0F) GL11.glRotatef(mrt.rotZ, 0.0F, 0.0F, 1.0F);
			if(mrt.rotY != 0.0F) GL11.glRotatef(mrt.rotY, 0.0F, 1.0F, 0.0F);
	        if(mrt.rotX != 0.0F) GL11.glRotatef(mrt.rotX, 1.0F, 0.0F, 0.0F);
		}
	},
	;

	public final int[] axid;

	RotationOrder(int[] axes){
		axid = axes;
	}

	public abstract void rotate(Polyhedron<?> mrt);

}
