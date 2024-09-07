package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.RGB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UISlot extends UIElement {

	public static LinkedHashMap<String, Function<Object[], Object>> GETTERS = new LinkedHashMap<>();
	public boolean playerinv;
	public String type;
	public int index;
	public int repeat_x;
	public int repeat_y;

	public UISlot(UserInterface ui, JsonMap map) throws Exception {
		super(ui, map);
		playerinv = map.getBoolean("player", true);
		type = map.getString("type", "default");
		index = map.getInteger("index", 0);
		if(map.has("repeat")){
			JsonArray array = map.getArray("repeat");
			repeat_x = array.get(0).integer_value();
			repeat_y = array.size() > 1 ? array.get(1).integer_value() : 1;
			if(repeat_x < 1) repeat_x = 1;
			if(repeat_y < 1) repeat_y = 1;
		}
		else repeat_x = repeat_y = 1;
	}

	public static Object get(String type, Object[] objs){
		return GETTERS.get(type).apply(objs);
	}

}
