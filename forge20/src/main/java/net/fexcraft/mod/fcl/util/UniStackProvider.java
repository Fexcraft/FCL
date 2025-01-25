package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniStackProvider implements ICapabilitySerializable<CompoundTag> {

	public static final Capability<UniStack> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
	private LazyOptional<UniStack> optional;
	private UniStack unistack;

	public UniStackProvider(ItemStack stack){
		unistack = new UniStack().set(stack);
		optional = LazyOptional.of(() -> unistack);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction){
		return capability == CAPABILITY ? optional.cast() : LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT(){
		TagCW com = TagCW.create();
		unistack.appended.save(com);
		return com.local();
	}

	@Override
	public void deserializeNBT(CompoundTag com){
		if(com == null) return;
		unistack.appended.load(TagCW.wrap(com));
	}

}
