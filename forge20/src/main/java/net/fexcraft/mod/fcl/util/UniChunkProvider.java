package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.uni.UniChunk;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniChunkProvider implements ICapabilitySerializable<CompoundTag> {

	public static final Capability<UniChunk> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
	private LazyOptional<UniChunk> optional;
	private UniChunk unick;

	public UniChunkProvider(LevelChunk chunk){
		unick = new UniChunk().set(chunk);
		optional = LazyOptional.of(() -> unick);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction){
		return capability == CAPABILITY ? optional.cast() : LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT(){
		TagCW com = TagCW.create();
		unick.appended.save(com);
		return com.local();
	}

	@Override
	public void deserializeNBT(CompoundTag com){
		unick.appended.load(TagCW.wrap(com));
	}

}
