package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.inv.UniInventory;
import net.fexcraft.mod.uni.inv.UniStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniInventory7 extends UniInventory implements IInventory {

	public UniInventory7(List<Object> local){
		super(local);
	}

	public UniInventory7(int size){
		super(new ArrayList<>(size));
	}

	@Override
	public int getSizeInventory(){
		return size();
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
	public ItemStack getStackInSlotOnClosing(int index){
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		set(index, UniStack.getStack(stack));
	}

	@Override
	public String getInventoryName(){
		return name();
	}

	@Override
	public boolean hasCustomInventoryName(){
		return true;
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
	public boolean isUseableByPlayer(EntityPlayer player){
		return true;
	}

	@Override
	public void openInventory(){
		open(null);
	}

	@Override
	public void closeInventory(){
		close(null);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack){
		return valid(index, UniStack.getStack(stack));
	}

	@Override
	public void clear(){
		super.clear();
	}

}
