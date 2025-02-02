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
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

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
	public String getFluidFromStack(StackWrapper sw){
		ItemStack stack = sw.local();
		if(!stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) return null;
		IFluidHandlerItem item = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if(item.getTankProperties().length > 0 && item.getTankProperties()[0].getContents() != null){
			return item.getTankProperties()[0].getContents().getFluid().getBlock().getRegistryName().toString();
		}
		return null;
	}

}
