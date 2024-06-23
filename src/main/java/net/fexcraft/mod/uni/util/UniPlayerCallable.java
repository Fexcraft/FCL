package net.fexcraft.mod.uni.util;

import net.fexcraft.mod.uni.UniPlayer;

public class UniPlayerCallable implements java.util.concurrent.Callable<UniPlayer> {

	@Override
	public UniPlayer call() throws Exception {
		return new UniPlayer();
	}

}
