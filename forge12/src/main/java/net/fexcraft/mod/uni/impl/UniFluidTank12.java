package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniFluidTank;
import net.fexcraft.mod.uni.inv.UniInventory;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniFluidTank12 extends UniFluidTank {

	private FluidTank tank;

	public UniFluidTank12(int capacity){
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
		if(!stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) return null;
		IFluidHandlerItem item = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if(item.getTankProperties().length > 0 && item.getTankProperties()[0].getContents() != null){
			return item.getTankProperties()[0].getContents().getFluid().getName();
		}
		return null;
	}

	@Override
	public Pair<StackWrapper, Boolean> drainFrom(StackWrapper stack, int amount){
		if(stack.empty() || !((ItemStack)stack.direct()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) return Pair.of(stack, false);
		IFluidHandlerItem item = ((ItemStack)stack.direct()).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if(item.getTankProperties().length > 0 && item.getTankProperties()[0].getContents() != null && item.getTankProperties()[0].getContents().amount > 0){
			FluidActionResult result = FluidUtil.tryEmptyContainer(stack.local(), tank, amount, null, true);
			return Pair.of(result.getResult() == null ? StackWrapper.EMPTY : UniStack.getStack(result.getResult()), result.success);
		}
		return Pair.of(stack, false);
	}

	@Override
	public void clear(){
		if(tank.getFluidAmount() > 0) tank.drain(tank.getFluidAmount(), true);
	}

}
