package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.inv.ItemWrapper;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class SWI extends StackWrapper {

	public ItemStack stack;

	protected SWI(ItemWrapper item){
		super(item);
		stack = new ItemStack((ItemLike)item.direct());
	}

	@Override
	public void set(Object obj){
		stack = (ItemStack)obj;
	}

	protected SWI(ItemStack stack){
		super(new IWI(stack.getItem()));
		this.stack = stack;
	}

	public static SWI parse(Object obj){
		if(obj instanceof ItemWrapper) return new SWI((ItemWrapper)obj);
		if(obj instanceof ItemStack) return new SWI((ItemStack)obj);
		if(obj instanceof TagCW) return new SWI(ItemStack.of((CompoundTag)((TagCW)obj).direct()));
		return null;
	}

	public ItemStack local(){
		return stack;
	}

	public Object direct(){
		return stack;
	}

	@Override
	public StackWrapper setTag(TagCW tag){
		stack.setTag(tag.local());
		return this;
	}

	@Override
	public TagCW getTag(){
		return TagCW.wrap(stack.getTag());
	}

	@Override
	public boolean hasTag(){
		return stack.hasTag();
	}

	@Override
	public String getName(){
		return stack.getDisplayName().getString();
	}

	@Override
	public int maxsize(){
		return stack.getMaxStackSize();
	}

	@Override
	public int damage(){
		return stack.getDamageValue();
	}

	@Override
	public void damage(int val){
		stack.setDamageValue(val);
	}

	@Override
	public int count(){
		return stack.getCount();
	}

	@Override
	public void count(int am){
		stack.setCount(am);
	}

	@Override
	public StackWrapper copy(){
		return UniStack.createStack(stack.copy());
	}

	@Override
	public void save(TagCW com){
		stack.save(com.local());
	}

	@Override
	public boolean empty(){
		return stack.isEmpty();
	}

	@Override
	public void createTagIfMissing(){
		if(!stack.hasTag()) stack.setTag(new CompoundTag());
	}

	@Override
	public IDL getIDL(){
		return IDLManager.getIDL(getID());
	}

	@Override
	public String getID(){
		return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof StackWrapper){
			return equals(((StackWrapper)o).direct());
		}
		else if(o instanceof ItemStack){
			return ItemStack.isSameItemSameTags(stack, (ItemStack)o);
		}
		else return super.equals(o);
	}

}