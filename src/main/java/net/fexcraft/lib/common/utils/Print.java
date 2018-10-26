package net.fexcraft.lib.common.utils;

import net.fexcraft.lib.common.Static;

public class Print {

	public static final void debug(String url){
		if(!Static.devmode) return; log(url);
	}

	public static final void log(String url){
		System.out.println(url);
	}
	
	public static final void log(Object... objects){
		log(true, objects);
	}
	
	public static final void log(boolean newlines, Object... objects){
		StringBuffer buff = new StringBuffer();
		buff.append("[ " + (newlines ? "\n" : ""));
		for(int i = 0; i < objects.length; i++){
			buff.append(objects[i] == null ? "%=NULL=%" : objects[i].toString());
			if(i < objects.length - 1) buff.append(newlines ? ",\n" : ", ");
		}
		buff.append(" ]");
		System.out.println(buff.toString());
	}

	public static boolean bool(boolean bool, String string){
		log(string); return bool;
	}
	
}