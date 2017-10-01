package net.fexcraft.mod.lib.api.common;

import net.fexcraft.mod.lib.api.item.KeyItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface LockableObject {
	
	public boolean isLocked();
	
	public boolean unlock(World world, EntityPlayer entity, ItemStack stack, KeyItem item);
	
	public boolean lock(World world, EntityPlayer entity, ItemStack stack, KeyItem item);
	
}