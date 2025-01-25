package net.fexcraft.mod.uni.util;

import net.fexcraft.mod.uni.inv.UniStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class UniStackSerializer implements ICapabilitySerializable<NBTBase>{

	private UniStack instance;

	public UniStackSerializer(ItemStack stack){
		instance = new UniStack().set(stack);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		return capability == FCLCapabilities.STACK;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		return capability == FCLCapabilities.STACK ? FCLCapabilities.STACK.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT(){
		return FCLCapabilities.STACK.getStorage().writeNBT(FCLCapabilities.STACK, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt){
		FCLCapabilities.STACK.getStorage().readNBT(FCLCapabilities.STACK, instance, null, nbt);
	}

}
