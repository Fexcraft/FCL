package net.fexcraft.mod.uni;

import net.fexcraft.app.json.*;
import net.fexcraft.app.json.JsonHandler.PrintOption;
import net.fexcraft.lib.common.math.RGB;

import java.io.File;
import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * FVTM4 based Config Base.
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class ConfigBase {

	protected final File file;
	protected ArrayList<ConfigEntry> entries = new ArrayList<>();
	protected ArrayList<Runnable> listeners = new ArrayList<>();
	protected boolean changes = false;

	public ConfigBase(File fl){
		file = fl;
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
		onReload(map);
		for(ConfigEntry entry : entries) entry.consumer.accept(entry, map);
		if(changes) JsonHandler.print(file, map, PrintOption.SPACED);
		for(Runnable run : listeners) run.run();
	}

	public void addListener(Runnable run){
		listeners.add(run);
	}

	public static class ConfigEntry {

		private ConfigBase base;
		private String key;
		private String cat;
		private String info;
		private JsonValue defval;
		private BiConsumer<ConfigEntry, JsonMap> consumer;
		private float min;
		private float max;

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

		public String getString(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			return map.getMap(key).get("value").string_value();
		}

		public boolean getBoolean(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			return map.getMap(key).get("value").bool();
		}

		public int getInteger(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			int i = map.getMap(key).get("value").integer_value();
			return i > max ? (int)max : i < min ? (int)min : i;
		}

		public float getFloat(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			float f = map.getMap(key).get("value").float_value();
			return f > max ? max : f < min ? min : f;
		}

		public FJson getJson(JsonMap map){
			map = checkCat(map);
			if(!map.has(key)) fill(map);
			return map.getMap(key).get("value");
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

	}


}
