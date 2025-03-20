package net.fexcraft.lib.common.utils;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CallbackContainer {

	private ConcurrentLinkedQueue<Runnable> callbacks = new ConcurrentLinkedQueue<>();
	private boolean completed = false;

	public void add(Runnable run){
		if(completed) run.run();
		else callbacks.add(run);
	}

	public void complete(){
		while(!callbacks.isEmpty()){
			callbacks.poll().run();
		}
		completed = true;
	}

}
