package net.fexcraft.mod.lib.util.common;

public class Formatter {
	
	public static String format(String string){
		if(string == null){
			return "";
		}
		string = string.replace("&", "\u00a7");
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
	
}