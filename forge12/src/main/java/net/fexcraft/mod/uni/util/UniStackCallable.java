package net.fexcraft.mod.uni.util;

import net.fexcraft.mod.uni.item.UniStack;

public class UniStackCallable implements java.util.concurrent.Callable<UniStack> {

	@Override
	public UniStack call() throws Exception {
		return new UniStack();
	}

}
