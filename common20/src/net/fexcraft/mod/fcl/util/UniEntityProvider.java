package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniEntityProvider implements ICapabilitySerializable<CompoundTag> {

	public static final Capability<UniEntity> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
	private LazyOptional<UniEntity> optional;
	private UniEntity unient;

	public UniEntityProvider(Entity entity){
		unient = new UniEntity().set(entity);
		optional = LazyOptional.of(() -> unient);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction){
		return capability == CAPABILITY ? optional.cast() : LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT(){
		TagCW com = TagCW.create();
		unient.appended.save(com);
		return com.local();
	}

	@Override
	public void deserializeNBT(CompoundTag com){
		if(com == null) return;
		unient.appended.load(TagCW.wrap(com));
	}

}
