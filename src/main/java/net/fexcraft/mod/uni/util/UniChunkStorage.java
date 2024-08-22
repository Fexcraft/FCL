package net.fexcraft.mod.uni.util;

import net.fexcraft.mod.uni.UniChunk;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class UniChunkStorage implements IStorage<UniChunk> {

	@Override
	public NBTBase writeNBT(Capability<UniChunk> capability, UniChunk instance, EnumFacing side) {
		TagCW com = TagCW.create();
		instance.appended.save(com);
		return com.local();
	}

	@Override
	public void readNBT(Capability<UniChunk> capability, UniChunk instance, EnumFacing side, NBTBase nbt){
		instance.appended.load(TagCW.wrap(nbt));
	}

}
