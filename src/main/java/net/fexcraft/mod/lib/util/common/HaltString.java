package net.fexcraft.mod.lib.util.common;

public class HaltString {
	
	public static final HaltString INSTANCE = new HaltString("null");
	private final String string;
	
	public HaltString(String string){
		this.string = string;
	}

	@Override
	public String toString(){
		System.err.println(string);
		Static.halt();
		return null;
	}
	
}