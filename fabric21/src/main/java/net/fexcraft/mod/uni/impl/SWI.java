package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.item.ItemType;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.ItemLike;

public class SWI extends StackWrapper {

	public ItemStack stack;

	/** Use StackWrapper.wrap() instead of direct. */
	@Deprecated
	public SWI(ItemWrapper item){
		super(item);
		stack = new ItemStack((ItemLike)item.direct());
		appended.init(appendables);
	}

	@Override
	public void set(Object obj){
		stack = (ItemStack)obj;
	}

	/** Use StackWrapper.wrap() instead of direct. */
	@Deprecated
	public SWI(ItemStack stack){
		super(new IWI(stack.getItem()));
		this.stack = stack;
		appended.init(appendables);
	}

	public ItemStack local(){
		return stack;
	}

	public Object direct(){
		return stack;
	}

	@Override
	public StackWrapper setTag(TagCW tag){
		//TODO stack.setTag(tag.local());
		return this;
	}

	@Override
	public TagCW getTag(){
		return TagCW.create();//TODO TagCW.wrap(stack.getTag());
	}

	@Override
	public boolean hasTag(){
		return true;//TODO stack.hasTag();
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
		return wrap(stack.copy());
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
		//TODO if(!stack.hasTag()) stack.setTag(new CompoundTag());
	}

	@Override
	public boolean isItemOf(ItemType type){
		switch(type){
			case LEAD: return stack.getItem() instanceof LeadItem;
			case FOOD: return false;//TODO stack.getItem().isEdible();
		}
		return false;
	}

	@Override
	public <C> C getContent(Object contenttype){
		return null;
	}

	@Override
	public IDL getIDL(){
		return IDLManager.getIDL(getID());
	}

	@Override
	public String getID(){
		return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
	}

}