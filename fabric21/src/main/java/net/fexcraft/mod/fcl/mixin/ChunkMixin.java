package net.fexcraft.mod.fcl.mixin;

import net.fexcraft.mod.fcl.mixint.CWProvider;
import net.fexcraft.mod.fcl.mixint.EWProvider;
import net.fexcraft.mod.uni.UniChunk;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mixin(LevelChunk.class)
public class ChunkMixin implements CWProvider {

	@Unique
	public UniChunk wrapper;

	@Override
	public UniChunk fcl_wrapper(){
		if(wrapper == null) wrapper = new UniChunk().set(this);
		return wrapper;
	}

}