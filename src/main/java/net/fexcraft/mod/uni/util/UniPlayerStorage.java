package net.fexcraft.mod.uni.util;

import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class UniPlayerStorage implements IStorage<UniEntity> {

	@Override
	public NBTBase writeNBT(Capability<UniEntity> capability, UniEntity instance, EnumFacing side) {
		TagCW com = TagCW.create();
		instance.save(com);
		return com.local();
	}

	@Override
	public void readNBT(Capability<UniEntity> capability, UniEntity instance, EnumFacing side, NBTBase nbt){
		instance.load(TagCW.wrap(nbt));
	}

}
