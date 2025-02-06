package net.fexcraft.mod.uni.inv;

import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.fexcraft.mod.uni.inv.StackWrapper.EMPTY;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class UniFluidTank {

	public static Class<? extends UniFluidTank> IMPL = null;
	protected ArrayList<UniFluidValidator> validators = new ArrayList<>();
	protected String name = "Universal Tank";

	public UniFluidTank(){}

	public static UniFluidTank create(int capacity){
		try{
			return IMPL.getConstructor(int.class).newInstance(capacity);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public UniFluidTank name(String str){
		name = str;
		return this;
	}

	public void addValidator(UniFluidValidator val){
		validators.add(val);
	}

	public String name(){
		return name;
	}

	public abstract int capacity();

	public abstract TagCW save();

	public abstract void load(TagCW com);

	public abstract <T> T local();

	public boolean isValid(StackWrapper stack){
		boolean result = true;
		for(UniFluidValidator validator : validators){
			if(!validator.isValid(stack)) result = false;
		}
		return result;
	}

	public abstract int amount();

	public abstract void amount(String type, int am);

	public abstract void drain(int am, boolean con);

	/** For tanks that are NOT empty. */
	public abstract void fill(int am, boolean con);

	public abstract String getFluid();

	public abstract String getFluidFromStack(StackWrapper stack);

	public abstract Pair<StackWrapper, Boolean> drainFrom(StackWrapper stack, int amount);

	public abstract void clear();

	public static interface UniFluidValidator {

		public boolean isValid(StackWrapper stack);

	}

	@Override
	public String toString(){
		return "UniFluidTank[" + name + "]";
	}

}
