package net.fexcraft.lib.mc.registry;

import java.lang.reflect.Field;

import org.apache.commons.lang3.Validate;

import net.minecraft.util.ResourceLocation;

public class UCResourceLocation extends ResourceLocation {
	
	public UCResourceLocation(String... resourceName){
		super(0, resourceName.length < 2 ? new String[]{"§", resourceName[0]} : resourceName);
		Field domain = ResourceLocation.class.getDeclaredFields()[0];
		domain.setAccessible(true);
		Field path = ResourceLocation.class.getDeclaredFields()[1];
		path.setAccessible(true);
		try {
			domain.set(this, resourceName[0].replace("§", ""));
			path.set(this, resourceName[1].indexOf(":") == 0 ? resourceName[1].substring(1) : resourceName[1]);
		}
		catch(IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
		}
        Validate.notNull(this.resourcePath);
    }

	public UCResourceLocation(ResourceLocation rs){
		this(rs.getResourceDomain(), rs.getResourcePath());
	}
	
}