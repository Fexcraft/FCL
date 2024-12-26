package net.fexcraft.mod.uni;

import net.fexcraft.app.json.*;
import net.fexcraft.app.json.JsonHandler.PrintOption;
import net.fexcraft.lib.common.math.RGB;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * FVTM4 based Config Base.
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class ConfigBase {

	public static final LinkedHashMap<String, ConfigBase> CONFIGS = new LinkedHashMap<>();
	protected final File file;
	protected LinkedHashMap<String, ArrayList<ConfigEntry>> categories = new LinkedHashMap<>();
	protected ArrayList<ConfigEntry> entries = new ArrayList<>();
	protected ArrayList<Runnable> listeners = new ArrayList<>();
	protected boolean changes = false;
	protected String name;

	public ConfigBase(File fl){
		this(fl, fl.getName());
	}

	public ConfigBase(File fl, String nome){
		file = fl;
		name = nome;
		CONFIGS.put(name, this);
		if(!file.exists()){
			file.getParentFile().mkdirs();
			JsonMap map = new JsonMap();
			map.add("format", 1);
			fillInfo(map);
			JsonHandler.print(file, map, PrintOption.SPACED);
		}
		fillEntries();
		reload();
	}

	protected abstract void fillInfo(JsonMap map);

	protected abstract void fillEntries();

	protected abstract void onReload(JsonMap map);

	public void reload(){
		changes = false;
		JsonMap map = JsonHandler.parse(file);
		createCategories();
		onReload(map);
		for(ConfigEntry entry : entries) entry.consumer.accept(entry, map);
		if(changes) JsonHandler.print(file, map, PrintOption.SPACED);
		for(Runnable run : listeners) run.run();
	}

	private void createCategories(){
		categories.clear();
		for(ConfigEntry entry : entries){
			if(!categories.containsKey(entry.cat)) categories.put(entry.cat, new ArrayList<>());
			categories.get(entry.cat).add(entry);
		}
	}

	public void addListener(Runnable run){
		listeners.add(run);
	}

	public Map<String, ArrayList<ConfigEntry>> getCategories(){
		return categories;
	}

	public ArrayList<ConfigEntry> getEntries(){
		return entries;
	}

	public String name(){
		return name;
	}

	public int getCategoryIndex(String cat){
		int idx = 0;
		for(String key : categories.keySet()){
			if(key.equals(cat)) return idx;
			idx++;
		}
		return -1;
	}

	public String getCategoryByIndex(int idx){
		int cat = 0;
		for(String key : categories.keySet()){
			if(cat == idx) return key;
			cat++;
		}
		return null;
	}

	public ArrayList<ConfigEntry> getCategoryEntries(int idx){
		int cat = 0;
		for(ArrayList<ConfigEntry> list : categories.values()){
			if(cat == idx) return list;
			cat++;
		}
		return null;
	}

	public ConfigEntry getEntry(String cat, String key){
		ArrayList<ConfigEntry> entries = categories.get(cat);
		if(entries != null){
			for(ConfigEntry entry : entries){
				if(entry.key.equals(key)) return entry;
			}
		}
		return null;
	}

	public static class ConfigEntry {

		private ConfigBase base;
		public final String key;
		public final String cat;
		private String info;
		private JsonValue defval;
		private JsonValue value;
		private BiConsumer<ConfigEntry, JsonMap> consumer;
		private float min;
		private float max;
		private Boolean req_level;
		private Boolean req_start;

		public ConfigEntry(ConfigBase base, String cat, String key, JsonValue def){
			this.cat = cat;
			this.key = key;
			this.defval = def;
			this.base = base;
		}

		public ConfigEntry(ConfigBase base, String cat, String key, boolean def){
			this(base, cat, key, new JsonValue(def));
		}

		public ConfigEntry(ConfigBase base, String cat, String key, String def){
			this(base, cat, key, new JsonValue(def));
		}

		public ConfigEntry(ConfigBase base, String cat, String key, float def){
			this(base, cat, key, new JsonValue(def));
		}

		public ConfigEntry(ConfigBase base, String cat, String key, int def){
			this(base, cat, key, new JsonValue(def));
		}

		public ConfigEntry info(String str){
			info = str;
			return this;
		}

		public ConfigEntry rang(float mn, float mx){
			min = mn;
			max = mx;
			return this;
		}

		public ConfigEntry cons(BiConsumer<ConfigEntry, JsonMap> cons){
			consumer = cons;
			return this;
		}

		public ConfigEntry req(boolean re_level, boolean re_start){
			req_level = re_level;
			req_start = re_start;
			return this;
		}

		public String getString(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			value = map.getMap(key).get("value");
			return value.string_value();
		}

		public boolean getBoolean(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			value = map.getMap(key).get("value");
			return value.bool();
		}

		public int getInteger(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			int i = map.getMap(key).get("value").integer_value();
			i = i > max ? (int)max : i < min ? (int)min : i;
			value = new JsonValue(i);
			return i;
		}

		public float getFloat(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			float f = map.getMap(key).get("value").float_value();
			f = f > max ? max : f < min ? min : f;
			value = new JsonValue(f);
			return f;
		}

		public FJson getJson(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			return value = map.getMap(key).get("value");
		}

		private JsonMap checkCat(JsonMap map){
			if(!map.has(cat)) map.addMap(cat);
			return map.getMap(cat);
		}

		private void fill(JsonMap map){
			JsonMap con = new JsonMap();
			if(info != null) con.add("info", info);
			if(min != 0 || max != 0) con.add("range", RGB.df.format(min) + " ~ " + RGB.df.format(max));
			con.add("value", defval);
			map.add(key, con);
			base.changes = true;
		}

		public String info(){
			return info;
		}

		public float min(){
			return min;
		}

		public float max(){
			return max;
		}

		public JsonValue initial(){
			return defval;
		}

		public JsonValue value(){
			return value == null ? defval : value;
		}

		public Boolean reqLevelRestart(){
			return req_level;
		}

		public Boolean reqGameRestart(){
			return req_start;
		}

	}


}
