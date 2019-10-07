package net.fexcraft.lib.mc.registry;

import java.lang.reflect.Field;

import org.apache.commons.lang3.Validate;

import net.minecraft.util.Identifier;

//TODO check if this works correctly
@Deprecated //<- till confirmed working, that is.
public class UCResourceLocation extends Identifier {
	
	public UCResourceLocation(String... resourceName){
		super(resourceName.length < 2 ? new String[]{"§", resourceName[0]} : resourceName);
		Field domain = Identifier.class.getDeclaredFields()[0];
		domain.setAccessible(true);
		Field path = Identifier.class.getDeclaredFields()[1];
		path.setAccessible(true);
		try {
			domain.set(this, resourceName[0].replace("§", ""));
			path.set(this, resourceName[1].indexOf(":") == 0 ? resourceName[1].substring(1) : resourceName[1]);
		}
		catch(IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
		}
        Validate.notNull(this.path);
    }

	public UCResourceLocation(Identifier rs){
		this(rs.getNamespace(), rs.getPath());
	}
	
}