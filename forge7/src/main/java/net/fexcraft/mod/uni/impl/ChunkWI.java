package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.world.ChunkW;
import net.fexcraft.mod.uni.world.WorldW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.world.chunk.Chunk;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ChunkWI implements ChunkW {

	protected Chunk chunk;
	protected WorldW world;

	public ChunkWI(Chunk ck){
		chunk = ck;
		world = WrapperHolder.getWorld(ck.worldObj);
	}

	@Override
	public boolean isOnClient(){
		return chunk.worldObj.isRemote;
	}

	@Override
	public int x(){
		return chunk.xPosition;
	}

	@Override
	public int z(){
		return chunk.zPosition;
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
		chunk.isModified = true;
	}

}
