package net.fexcraft.mod.uni.world;

import net.minecraft.world.chunk.Chunk;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ChunkWI implements ChunkW {

	protected Chunk chunk;
	protected WorldW world;

	public ChunkWI(Chunk ck){
		chunk = ck;
		world = WrapperHolder.getWorld(ck.getWorld());
	}

	@Override
	public boolean isOnClient(){
		return chunk.getWorld().isRemote;
	}

	@Override
	public int getX(){
		return chunk.x;
	}

	@Override
	public int getZ(){
		return chunk.z;
	}

	@Override
	public int x(){
		return chunk.x;
	}

	@Override
	public int z(){
		return chunk.z;
	}

	@Override
	public WorldW getWorld(){
		return world;
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
		chunk.markDirty();
	}

}
