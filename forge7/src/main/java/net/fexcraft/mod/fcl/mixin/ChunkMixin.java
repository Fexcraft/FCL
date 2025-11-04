package net.fexcraft.mod.fcl.mixin;

import net.fexcraft.mod.fcl.mixint.CWProvider;
import net.fexcraft.mod.uni.UniChunk;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mixin(Chunk.class)
public class ChunkMixin implements CWProvider {

	@Unique
	public UniChunk wrapper;

	@Override
	public UniChunk fcl_wrapper(){
		if(wrapper == null) wrapper = new UniChunk().set(this);
		return wrapper;
	}

}