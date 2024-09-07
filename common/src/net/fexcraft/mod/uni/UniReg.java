package net.fexcraft.mod.uni;

import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIKey;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniReg {

	public static String LOADER_VERSION = null;
	public static ConcurrentHashMap<String, Object> INSTANCES = new ConcurrentHashMap<>();
	public static LinkedHashMap<UIKey, Class<? extends UserInterface>> GUI = new LinkedHashMap<>();
	public static LinkedHashMap<UIKey, Class<? extends ContainerInterface>> MENU = new LinkedHashMap<>();
	public static LinkedHashMap<UIKey, String> MENU_JSON_S = new LinkedHashMap<>();
	public static LinkedHashMap<UIKey, String> MENU_JSON_C = new LinkedHashMap<>();

	public static void registerMod(String id, Object inst){
		INSTANCES.put(id, inst);
	}

	public static boolean registerUI(UIKey key, Class<? extends UserInterface> ui){
		if(GUI.containsKey(key)) return false;
		GUI.put(key, ui);
		return true;
	}

	public static boolean registerMenu(UIKey key, String loc, Class<? extends ContainerInterface> con){
		if(MENU.containsKey(key)) return false;
		MENU.put(key, con);
		MENU_JSON_S.put(key, loc);
		MENU_JSON_C.put(key, loc.substring(loc.indexOf("/", loc.indexOf("/") + 1) + 1));
		return true;
	}

	public static Object getInst(UIKey key){
		return INSTANCES.get(key.key.substring(0, key.key.indexOf(":")));
	}

	public static Object getInst(String mid){
		return INSTANCES.get(mid);
	}

}
