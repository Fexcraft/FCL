package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.item.UniInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniInventory12 extends UniInventory implements IInventory {

	public UniInventory12(List<Object> local){
		super(local);
	}

	public UniInventory12(int size){
		super(NonNullList.withSize(size, ItemStack.EMPTY));
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
	public ItemStack getStackInSlot(int index){
		return get(index).local();
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
		set(index, StackWrapper.wrap(stack));
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
		return valid(index, StackWrapper.wrap(stack));
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
