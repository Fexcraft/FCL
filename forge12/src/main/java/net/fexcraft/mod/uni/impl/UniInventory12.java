package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniInventory;
import net.fexcraft.mod.uni.inv.UniStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniInventory12 extends UniInventory implements IInventory, IItemHandler {

	private ItemStackHandler handler;

	public UniInventory12(List<Object> local){
		super(local);
	}

	public UniInventory12(int size){
		super(NonNullList.withSize(size, ItemStack.EMPTY));
		handler = new ItemStackHandler();
	}

	@Override
	public int getSizeInventory(){
		return size();
	}

	@Override
	public boolean isEmpty(){
		return empty();
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
		return over ? ItemHandlerHelper.copyStackWithSize(stack,stack.getCount() - rem) : ItemStack.EMPTY;
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

	@Override
	public ItemStack decrStackSize(int index, int count){
		return get(index, count).local();
	}

	@Override
	public ItemStack removeStackFromSlot(int index){
		return remove(index).local();
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		set(index, UniStack.getStack(stack));
	}

	@Override
	public int getInventoryStackLimit(){
		return stacksize;
	}

	@Override
	public void markDirty(){
		mark();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player){
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player){
		open(UniEntity.get(player));
	}

	@Override
	public void closeInventory(EntityPlayer player){
		close(UniEntity.get(player));
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack){
		return valid(index, UniStack.getStack(stack));
	}

	@Override
	public int getField(int id){
		return 0;
	}

	@Override
	public void setField(int id, int value){

	}

	@Override
	public int getFieldCount(){
		return 0;
	}

	@Override
	public void clear(){
		super.clear();
	}

	@Override
	public String getName(){
		return name();
	}

	@Override
	public boolean hasCustomName(){
		return true;
	}

	@Override
	public ITextComponent getDisplayName(){
		return new TextComponentString(name());
	}

}
