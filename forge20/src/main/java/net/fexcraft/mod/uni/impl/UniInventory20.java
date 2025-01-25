package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.inv.UniInventory;
import net.fexcraft.mod.uni.inv.UniStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniInventory20 extends UniInventory implements Container {

	public UniInventory20(List<Object> local){
		super(local);
	}

	public UniInventory20(int size){
		super(NonNullList.withSize(size, ItemStack.EMPTY));
	}

	@Override
	public int getContainerSize(){
		return size();
	}

	@Override
	public boolean isEmpty(){
		return empty();
	}

	@Override
	public ItemStack getItem(int idx){
		return get(idx).local();
	}

	@Override
	public ItemStack removeItem(int idx, int am){
		return get(idx, am).local();
	}

	@Override
	public ItemStack removeItemNoUpdate(int idx){
		return remove(idx).local();
	}

	@Override
	public void setItem(int idx, ItemStack stack){
		set(idx, UniStack.getStack(stack));
	}

	@Override
	public void setChanged(){
		mark();
	}

	@Override
	public boolean stillValid(Player player){
		return true;
	}

	@Override
	public void clearContent(){
		clear();
	}

}
