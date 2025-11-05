package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniFluidTank;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniFluidTank7 extends UniFluidTank {

	private FluidTank tank;

	public UniFluidTank7(int capacity){
		super();
		tank = new FluidTank(capacity);
	}

	@Override
	public int capacity(){
		return tank.getCapacity();
	}

	@Override
	public TagCW save(){
		TagCW tag = TagCW.create();
		tank.writeToNBT(tag.local());
		return tag;
	}

	@Override
	public void load(TagCW com){
		tank.readFromNBT(com.local());
	}

	@Override
	public <T> T local(){
		return (T)tank;
	}

	@Override
	public int amount(){
		return tank.getFluidAmount();
	}

	@Override
	public void amount(String type, int am){
		clear();
		Fluid fluid = FluidRegistry.getFluid(type);
		if(fluid == null) return;
		tank.fill(new FluidStack(fluid, am), true);
	}

	@Override
	public void drain(int am, boolean con){
		tank.drain(am, con);
	}

	@Override
	public void fill(int am, boolean con){
		FluidStack stack = tank.getFluid().copy();
		stack.amount = am;
		tank.fill(stack, con);
	}

	@Override
	public String getFluid(){
		if(tank.getFluid() == null || tank.getFluid().getFluid() == null) return "null";
		return tank.getFluid().getFluid().getName();
	}

	@Override
	public String getFluidFromStack(StackWrapper sw){
		ItemStack stack = sw.local();
		//TODO
		return null;
	}

	@Override
	public Pair<StackWrapper, Boolean> drainFrom(StackWrapper stack, int amount){
		//TODO
		return Pair.of(stack, false);
	}

	@Override
	public void clear(){
		if(tank.getFluidAmount() > 0) tank.drain(tank.getFluidAmount(), true);
	}

}
