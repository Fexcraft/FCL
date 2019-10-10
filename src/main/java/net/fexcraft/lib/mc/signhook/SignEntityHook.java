package net.fexcraft.lib.mc.signhook;

public interface SignEntityHook {
	
	public SignInteractionHook getSignInteractionHook();
	
	public void setSignInteractionHook(SignInteractionHook hook);
	
	public void sendSIHUpdate();

}
