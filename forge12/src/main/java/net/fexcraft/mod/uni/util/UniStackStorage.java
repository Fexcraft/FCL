package net.fexcraft.mod.uni.util;

import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class UniStackStorage implements IStorage<UniStack> {

	@Override
	public NBTBase writeNBT(Capability<UniStack> capability, UniStack instance, EnumFacing side) {
		TagCW com = TagCW.create();
		instance.appended.save(com);
		return com.local();
	}

	@Override
	public void readNBT(Capability<UniStack> capability, UniStack instance, EnumFacing side, NBTBase nbt){
		instance.appended.load(TagCW.wrap(nbt));
	}

}
