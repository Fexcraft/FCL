package net.fexcraft.lib.mc.utils;

import java.io.File;

import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonObject;

public class JsonConfig {
	
	private File file;
	private JsonMap config;

	public JsonConfig(File file){
		this.file = file;
	}
	
	public JsonConfig load(){
		config = JsonHandler.parse(file);
		return this;
	}
	
	public JsonConfig save(){
		JsonHandler.print(file, config, false, true);
		return this;
	}
	
	public JsonMap map(){
		return config;
	}
	
	public JsonObject<?> get(String key, Object def){
		return get(key, def, null);
	}
	
	public JsonObject<?> get(String key, Object def, String comment){
		if(config.has(key)) return config.get(key);
		if(comment != null) config.add("#" + key, comment);
		config.add(key, def);
		return config.get(key);
	}
	
	public <V> V getValue(String key, Object def){
		return (V)getValue(key, def, null);
	}
	
	public <V> V getValue(String key, V def, String comment){
		if(config.has(key)) return config.get(key).value();
		if(comment != null) config.add("#" + key, comment);
		config.add(key, def);
		return config.get(key).value();
	}

}
