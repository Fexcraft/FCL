package net.fexcraft.lib.common.utils;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Formatter {
	
	public static final String PARAGRAPH_SIGN = "\u00a7";
	
	public static String format(String string){
		if(string == null) return "";
		return string.replaceAll("&([a-f0-9k-or])", "\u00a7$1");
	}

	public static String format(String string, Object... objs){
		return format(String.format(string, objs));
	}

	public static String clear(String string){
		if(string == null) return "";
		return string.replaceAll("&([a-f0-9k-or])", "$1");
	}

	
}