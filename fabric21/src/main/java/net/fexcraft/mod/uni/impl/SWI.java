package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.inv.ItemWrapper;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

public class SWI extends StackWrapper {

	public ItemStack stack;

	/** Use StackWrapper.wrap() instead of direct. */
	@Deprecated
	public SWI(ItemWrapper item){
		super(item);
		stack = new ItemStack((ItemLike)item.direct());
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
	}

	public static SWI parse(Object obj){
		if(obj instanceof ItemWrapper) return new SWI((ItemWrapper)obj);
		if(obj instanceof ItemStack) return new SWI((ItemStack)obj);
		if(obj instanceof TagCW){
			Optional<ItemStack> opt = ItemStack.parse(FCL.SERVER.get().registryAccess(), (CompoundTag)((TagCW)obj).direct());
			return new SWI(opt.isPresent() ? opt.get() : ItemStack.EMPTY);
		}
		return null;
	}

	public ItemStack local(){
		return stack;
	}

	public Object direct(){
		return stack;
	}

	@Override
	public StackWrapper updateTag(TagCW tag){
		if(!tag.has("fcl")) tag.set("fcl", (byte)0);
		stack.set(FCL.FCLTAG, CustomData.of(tag.local()));
		return this;
	}

	@Override
	public TagCW directTag(){
		if(!stack.has(FCL.FCLTAG)){
			CompoundTag tag = new CompoundTag();
			tag.putByte("fcl", (byte)0);
			stack.set(FCL.FCLTAG, CustomData.of(tag));
		}
		return TagCW.wrap(stack.get(FCL.FCLTAG).getUnsafe());
	}

	@Override
	public TagCW copyTag(){
		if(!stack.has(FCL.FCLTAG)){
			CompoundTag tag = new CompoundTag();
			tag.putByte("fcl", (byte)0);
			stack.set(FCL.FCLTAG, CustomData.of(tag));
		}
		return TagCW.wrap(stack.get(FCL.FCLTAG).copyTag());
	}

	@Override
	public boolean hasTag(){
		return stack.has(FCL.FCLTAG);
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
	public IDL getIDL(){
		return IDLManager.getIDLCached(getID());
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
			return ItemStack.isSameItemSameComponents(stack, (ItemStack)o);
		}
		else return super.equals(o);
	}

}