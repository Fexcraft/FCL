package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.uni.world.ChunkW;
import net.fexcraft.mod.uni.world.WorldW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ChunkWI implements ChunkW {

	private LevelChunk chunk;

	public ChunkWI(LevelChunk ck){
		chunk = ck;
	}

	@Override
	public boolean isOnClient(){
		return chunk.getLevel().isClientSide;
	}

	@Override
	public int getX(){
		return chunk.getPos().x;
	}

	@Override
	public int getZ(){
		return chunk.getPos().z;
	}

	@Override
	public int x(){
		return chunk.getPos().x;
	}

	@Override
	public int z(){
		return chunk.getPos().z;
	}

	@Override
	public WorldW getWorld(){
		return WrapperHolder.getWorld(chunk.getLevel());
	}

	@Override
	public <E> E local(){
		return (E)chunk;
	}

	@Override
	public Object direct(){
		return chunk;
	}

	@Override
	public void markChanged(){
		chunk.markUnsaved();
	}

}
