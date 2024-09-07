package net.fexcraft.mod.uni.util;

import net.fexcraft.mod.uni.UniChunk;

public class UniChunkCallable implements java.util.concurrent.Callable<UniChunk> {

	@Override
	public UniChunk call() throws Exception {
		return new UniChunk();
	}

}
