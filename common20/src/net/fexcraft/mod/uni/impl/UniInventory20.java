package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniInventory;
import net.fexcraft.mod.uni.inv.UniStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniInventory20 extends UniInventory implements Container, IItemHandler {

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

	@Override
	public int getSlots(){
		return size();
	}

	@Override
	public ItemStack getStackInSlot(int index){
		return get(index).local();
	}

	@Override
	public ItemStack insertItem(int idx, @Nonnull ItemStack stack, boolean sim){
		if(stack.isEmpty()) return ItemStack.EMPTY;
		if(!valid(idx, UniStack.getStack(stack))) return stack;
		int rem = stacksize;
		StackWrapper local = get(idx);
		if(!local.empty()){
			if(!ItemHandlerHelper.canItemStacksStack(stack, local.local())) return stack;
			rem -= local.count();
		}
		if(rem <= 0) return stack;
		boolean over = stack.getCount() > rem;
		if(!sim){
			if(local.empty()){
				set(idx, UniStack.getStack(over ? ItemHandlerHelper.copyStackWithSize(stack, rem) : stack));
			}
			else local.incr(over ? rem : stack.getCount());
		}
		return over ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - rem) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack extractItem(int idx, int am, boolean sim){
		if(am == 0) return ItemStack.EMPTY;
		StackWrapper stack = get(idx);
		if(stack.empty()) return ItemStack.EMPTY;
		int rem = am < stack.maxsize() ? am : stack.maxsize();
		if(stack.count() < rem){
			if(!sim) set(idx, StackWrapper.EMPTY);
			return stack.local();
		}
		else{
			if(!sim) set(idx, UniStack.getStack(ItemHandlerHelper.copyStackWithSize(stack.local(), stack.count() - rem)));
			return ItemHandlerHelper.copyStackWithSize(stack.local(), stack.count() - rem);
		}
	}

	@Override
	public int getSlotLimit(int slot){
		return stacksize;
	}

	@Override
	public boolean isItemValid(int idx, ItemStack stack){
		return valid(idx, UniStack.getStack(stack));
	}

}
