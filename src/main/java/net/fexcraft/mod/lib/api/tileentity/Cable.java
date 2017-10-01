package net.fexcraft.mod.lib.api.tileentity;

import net.minecraft.util.EnumFacing;

public interface Cable {
	
	public int fpu_get(EnumFacing ef);
	
	public void fpu_set(EnumFacing ef, int e);
	
	public void fpu_add(EnumFacing ef, int e);
	
	public void fpu_subtract(EnumFacing ef, int e);
	
	public int fpu_max(EnumFacing ef);
	
	public int fpu_min(EnumFacing ef);
	
	/** FPU transferred per tick */
	public int fpu_transfer_speed(EnumFacing ef);
	
	public int fpu_min_transfer_speed(EnumFacing ef);
	
	public EnumFacing fpu_input();
	
	public boolean fpu_isInput(EnumFacing ef);
	
	public EnumFacing fpu_output();
	
	public boolean fpu_isOutput(EnumFacing ef);
	
	public void fpu_setInput(EnumFacing ef, int i);
	
	public void fpu_setOutput(EnumFacing ef, int i);
	
	public void detectAndSendChanges(EnumFacing ef);
	
	public int fpu_resistivity(EnumFacing ef);
	
	
}