package net.fexcraft.mod.uni.util;

import net.fexcraft.mod.uni.UniEntity;

public class UniPlayerCallable implements java.util.concurrent.Callable<UniEntity> {

	@Override
	public UniEntity call() throws Exception {
		return new UniEntity();
	}

}
