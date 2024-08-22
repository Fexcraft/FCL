package net.fexcraft.mod.uni.util;

import net.fexcraft.mod.uni.UniChunk;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class UniChunkSerializer implements ICapabilitySerializable<NBTBase>{
	
	private UniChunk instance;
	
	public UniChunkSerializer(Chunk chunk){
		instance = new UniChunk().set(chunk);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		return capability == FCLCapabilities.CHUNK;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		return capability == FCLCapabilities.CHUNK ? FCLCapabilities.CHUNK.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT(){
		return FCLCapabilities.CHUNK.getStorage().writeNBT(FCLCapabilities.CHUNK, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt){
		FCLCapabilities.CHUNK.getStorage().readNBT(FCLCapabilities.CHUNK, instance, null, nbt);
	}

}
