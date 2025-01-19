package net.fexcraft.mod.uni.item;

import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.tag.TagCW;

import java.util.ArrayList;
import java.util.List;

import static net.fexcraft.mod.uni.item.StackWrapper.EMPTY;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class UniInventory {

	public static Class<? extends UniInventory> IMPL = null;
	protected ArrayList<StackWrapper> stacks;
	protected List<Object> local;
	protected boolean drop_on_close;
	protected int stacksize = 64;
	protected String name = "Universal Inventory";

	public UniInventory(List<Object> local){
		this.local = local;
		stacks = new ArrayList<>();
		for(int i = 0; i < local.size(); i++){
			stacks.add(i, StackWrapper.wrap(local.get(i)));
		}
	}

	public static UniInventory create(List<Object> stacks){
		try{
			return IMPL.getConstructor(List.class).newInstance(stacks);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static UniInventory create(int size){
		try{
			return IMPL.getConstructor(int.class).newInstance(size);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public UniInventory stacksize(int set){
		stacksize = set;
		return this;
	}

	public UniInventory drop(boolean bool){
		drop_on_close = bool;
		return this;
	}

	public UniInventory name(String str){
		name = str;
		return this;
	}

	public <INV> INV cast(){
		return (INV)this;
	}

	public boolean empty(){
		boolean empty = true;
		for(StackWrapper stack : stacks){
			if(!stack.empty()){
				empty = false;
				break;
			}
		}
		return empty;
	}

	public int size(){
		return stacks.size();
	}

	public StackWrapper get(int idx){
		if(idx < 0 || idx >= stacks.size()) return EMPTY;
		return stacks.get(idx);
	}

	public void set(int idx, StackWrapper stack){
		if(idx < 0 || idx >= stacks.size()) return;
		local.set(idx, stack.local());
		stacks.set(idx, stack);
	}

	public void set(int idx, TagCW com){
		set(idx, StackWrapper.wrap(com));
	}

	public StackWrapper get(int idx, int am){
		if(idx < 0 || idx >= stacks.size()) return EMPTY;
		StackWrapper stack = stacks.get(idx);
		if(stack.empty()) return stack;
		if(am >= stack.count()){
			set(idx, EMPTY);
			return stack;
		}
		else{
			StackWrapper copy = stack.copy();
			copy.count(am);
			stack.decr(am);
			return copy;
		}
	}

	public StackWrapper remove(int idx){
		StackWrapper stack = get(idx);
		set(idx, EMPTY);
		return stack;
	}

	public boolean empty(int idx){
		return get(idx).empty();
	}

	public void mark(){
		//
	}

	public void open(UniEntity ent){
		//
	}

	public void close(UniEntity ent){
		if(!drop_on_close) return;
		for(StackWrapper stack : stacks){
			if(stack.empty()) continue;
			ent.entity.addStack(stack);
		}
	}

	public boolean valid(int idx, StackWrapper stack){
		return true;
	}

	public void clear(){
		local.clear();
		for(int i = 0; i < stacks.size(); i++) stacks.set(i, EMPTY);
	}

	public String name(){
		return name;
	}

	@Override
	public String toString(){
		return "UniInventory[" + name + "]";
	}

}
