package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniFluidTank;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniFluidTank20 extends UniFluidTank {

	private FluidTank tank;

	public UniFluidTank20(int capacity){
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
		Fluid fluid = BuiltInRegistries.FLUID.get(new ResourceLocation(type));
		if(fluid == null) return;
		tank.fill(new FluidStack(fluid, am), EXECUTE);
	}

	@Override
	public void drain(int am, boolean con){
		tank.drain(am, con ? EXECUTE : SIMULATE);
	}

	@Override
	public void fill(int am, boolean con){
		FluidStack stack = tank.getFluid().copy();
		stack.setAmount(am);
		tank.fill(stack, con ? EXECUTE : SIMULATE);
	}

	@Override
	public String getFluid(){
		if(tank.getFluid().getFluid() == null) return "null";
		return getFluidTypeId(tank.getFluid().getFluid().getFluidType());
	}

	public String getFluidTypeId(FluidType type){
		return ForgeRegistries.FLUID_TYPES.get().getKey(type).getPath();
	}

	@Override
	public String getFluidFromStack(StackWrapper sw){
		ItemStack stack = sw.local();
		if(!stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) return null;
		IFluidHandlerItem item = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().get();
		if(item.getTanks() > 0 && item.getFluidInTank(0).getFluid() != null){
			return getFluidTypeId(item.getFluidInTank(0).getFluid().getFluidType());
		}
		return null;
	}

	@Override
	public Pair<StackWrapper, Boolean> drainFrom(StackWrapper stack, int amount){
		if(stack.empty() || !((ItemStack)stack.direct()).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) return Pair.of(stack, false);
		IFluidHandlerItem item = ((ItemStack)stack.direct()).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().get();
		if(item.getTanks() > 0 && item.getFluidInTank(0).getFluid() != null && item.getFluidInTank(0).getAmount() > 0){
			FluidActionResult result = FluidUtil.tryEmptyContainer(stack.local(), tank, amount, null, true);
			return Pair.of(result.getResult() == null ? StackWrapper.EMPTY : UniStack.getStack(result.getResult()), result.success);
		}
		return Pair.of(stack, false);
	}

	@Override
	public void clear(){
		if(tank.getFluidAmount() > 0) tank.drain(tank.getFluidAmount(), EXECUTE);
	}

}
