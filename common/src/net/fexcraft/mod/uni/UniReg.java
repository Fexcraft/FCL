package net.fexcraft.mod.uni;

import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIKey;
import net.fexcraft.mod.uni.ui.UserInterface;
import net.fexcraft.mod.uni.world.WrapperHolder;

import java.io.IOException;
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
	public static LinkedHashMap<UIKey, IDL> MENU_LOC = new LinkedHashMap<>();
	public static LinkedHashMap<UIKey, JsonMap> MENU_JSON = new LinkedHashMap<>();

	public static void registerMod(String id, Object inst){
		INSTANCES.put(id, inst);
	}

	public static boolean registerUI(UIKey key, Class<? extends UserInterface> ui){
		if(GUI.containsKey(key)) return false;
		GUI.put(key, ui);
		return true;
	}

	public static boolean registerMenu(UIKey key, String loc, Class<? extends ContainerInterface> con){
		return registerMenu(key, IDLManager.getIDLCached(loc + ".json"), con);
	}

	public static boolean registerMenu(UIKey key, IDL idl, Class<? extends ContainerInterface> con){
		if(MENU.containsKey(key)) return false;
		MENU.put(key, con);
		MENU_LOC.put(key, idl);
		return true;
	}

	public static Object getInst(UIKey key){
		return INSTANCES.get(key.key.substring(0, key.key.indexOf(":")));
	}

	public static Object getInst(String mid){
		return INSTANCES.get(mid);
	}

	public static JsonMap getMenuJson(String ui){
		try{
			UIKey key = UIKey.find(ui);
			if(MENU_JSON.containsKey(key)) return MENU_JSON.get(key);
			JsonMap map = JsonHandler.parse(WrapperHolder.getDataResource(MENU_LOC.get(key)));
			if(map.not_empty()) MENU_JSON.put(key, map);
			return map;
		}
		catch(IOException e){
			e.printStackTrace();
			return new JsonMap();
		}
	}

}
