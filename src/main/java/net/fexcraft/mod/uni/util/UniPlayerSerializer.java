package net.fexcraft.mod.uni.util;

import net.fexcraft.lib.mc.capabilities.FCLCapabilities;
import net.fexcraft.mod.uni.UniPlayer;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class UniPlayerSerializer implements ICapabilitySerializable<NBTBase>{
	
	private UniPlayer instance;
	
	public UniPlayerSerializer(Entity entity){
		(instance = FCLCapabilities.PLAYER.getDefaultInstance()).set(WrapperHolder.getEntity(entity));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		return capability == FCLCapabilities.PLAYER;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		return capability == FCLCapabilities.PLAYER ? FCLCapabilities.PLAYER.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT(){
		return FCLCapabilities.PLAYER.getStorage().writeNBT(FCLCapabilities.PLAYER, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt){
		FCLCapabilities.PLAYER.getStorage().readNBT(FCLCapabilities.PLAYER, instance, null, nbt);
	}

}
