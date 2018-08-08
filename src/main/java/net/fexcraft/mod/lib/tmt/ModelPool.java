package net.fexcraft.mod.lib.tmt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class ModelPool {
	
	public static ModelPoolEntry addLocation(ResourceLocation location, Class<?> modelClass){
		ModelPoolEntry entry = null;
		if(modelMap.containsKey(location.toString())){
			entry = modelMap.get(location.toString());
			return entry;
		}
		try{
			entry = (ModelPoolEntry)modelClass.newInstance();
		}
		catch(Exception e){
			System.out.println("A new " + entry.getClass().getName() + " could not be initialized.");
			System.out.println(e.getMessage());
			return null;
		}
		IResource resource;
		try{
			resource = Minecraft.getMinecraft().getResourceManager().getResource(location);
			if(resource == null){ return null; }
		}
		catch(IOException e){
			System.out.println("The model with the name " + location + " does not exist.");
			e.printStackTrace();
			return null;
		}
		entry.name = location.toString();
		entry.getModel(resource);
		modelMap.put(location.toString(), entry);
		return entry;
	}
	
    private static Map<String, ModelPoolEntry> modelMap = new HashMap<String, ModelPoolEntry>();
    public static final Class<ModelPoolObjEntry> OBJ = ModelPoolObjEntry.class;
    
}
