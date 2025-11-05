package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.inv.ItemWrapper;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class SWI extends StackWrapper {

	public ItemStack stack;

	public SWI(ItemWrapper item){
		super(item);
		stack = new ItemStack((Item)item.direct());
	}

	public SWI(ItemStack is){
		super(ItemWrapper.get(Item.itemRegistry.getNameForObject(is.getItem())));
		stack = is;
	}

	public static SWI parse(Object obj){
		if(obj instanceof ItemWrapper) return new SWI((ItemWrapper)obj);
		if(obj instanceof ItemStack) return new SWI((ItemStack)obj);
		if(obj instanceof TagCW) return new SWI(ItemStack.loadItemStackFromNBT((NBTTagCompound)((TagCW)obj).direct()));
		return null;
	}

	@Override
	public void set(Object obj){
		stack = (ItemStack)obj;
	}

	@Override
	public ItemStack local(){
		return stack;
	}

	@Override
	public Object direct(){
		return stack;
	}

	@Override
	public StackWrapper updateTag(TagCW tag){
		stack.setTagCompound(tag.local());
		return this;
	}

	@Override
	public TagCW directTag(){
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		return TagCW.wrap(stack.getTagCompound());
	}

	@Override
	public TagCW copyTag(){
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		return TagCW.wrap(stack.getTagCompound().copy());
	}

	@Override
	public boolean hasTag(){
		return stack.hasTagCompound();
	}

	@Override
	public String getName(){
		return stack.getDisplayName();
	}

	@Override
	public int maxsize(){
		return stack.getMaxStackSize();
	}

	@Override
	public int damage(){
		return stack.getItemDamage();
	}

	@Override
	public void damage(int val){
		stack.setItemDamage(val);
	}

	@Override
	public int count(){
		return stack.stackSize;
	}

	@Override
	public void count(int am){
		stack.stackSize = am;
	}

	@Override
	public StackWrapper copy(){
		return UniStack.createStack(stack.copy());
	}

	@Override
	public void save(TagCW com){
		stack.writeToNBT(com.local());
	}

	@Override
	public boolean empty(){
		return stack.stackSize < 1;
	}

	@Override
	public IDL getIDL(){
		return IDLManager.getIDL(Item.itemRegistry.getNameForObject(stack.getItem()));
	}

	@Override
	public String getID(){
		return Item.itemRegistry.getNameForObject(stack.getItem());
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof StackWrapper){
			return equals(((StackWrapper)o).direct());
		}
		else if(o instanceof ItemStack){
			return stack.isItemEqual((ItemStack)o);
		}
		else return super.equals(o);
	}

}
