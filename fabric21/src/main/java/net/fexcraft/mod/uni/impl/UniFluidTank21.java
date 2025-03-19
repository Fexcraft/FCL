package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniFluidTank;
import net.fexcraft.mod.uni.tag.TagCW;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniFluidTank21 extends UniFluidTank {

	public UniFluidTank21(int capacity){
		super();
	}

	@Override
	public int capacity(){
		return 0;
	}

	@Override
	public TagCW save(){
		TagCW tag = TagCW.create();

		return tag;
	}

	@Override
	public void load(TagCW com){

	}

	@Override
	public <T> T local(){
		return null;
	}

	@Override
	public int amount(){
		return 0;
	}

	@Override
	public void amount(String type, int am){
		clear();

	}

	@Override
	public void drain(int am, boolean con){

	}

	@Override
	public void fill(int am, boolean con){

	}

	@Override
	public String getFluid(){
		return "null";
	}

	@Override
	public String getFluidFromStack(StackWrapper sw){

		return null;
	}

	@Override
	public Pair<StackWrapper, Boolean> drainFrom(StackWrapper stack, int amount){

		return Pair.of(stack, false);
	}

	@Override
	public void clear(){
		//
	}

}
