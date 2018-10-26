package net.fexcraft.mod.lib.util.common;

public class Formatter {
	
	public static final String PARAGRAPH_SIGN = "\u00a7";
	
	public static String format(String string){
		if(string == null){ return ""; }
		string = string.replace("&", PARAGRAPH_SIGN);
		return string;
	}
	
	public static String fromInt(int i){
		return format(fromInt((byte)i));
	}

	public static String fromInt(byte i){
		if(i < 0){ i = 0; }
		if(i < 10){
			return "&" + i;
		}
		switch(i){
			case 10: return "&a";
			case 11: return "&b";
			case 12: return "&c";
			case 13: return "&d";
			case 14: return "&e";
			case 15: return "&f";
			default: return "&2";
		}
	}
	
	public static String clear(String string){
		return string.replaceAll("&d", string).replaceAll(PARAGRAPH_SIGN + "d", string);
	}

	public static net.minecraft.util.text.ITextComponent newTextComponentString(String string){
		return new net.minecraft.util.text.TextComponentString(format(string));
	}

	public static String format(String string, Object... objs){
		return format(String.format(string, objs));
	}
	
}