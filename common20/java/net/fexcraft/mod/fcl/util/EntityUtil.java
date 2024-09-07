package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.uni.world.EntityW;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class EntityUtil {

	public static Class<? extends EntityWI> IMPL = EntityWI.class;
	public static EntityWI.UIOpen UI_OPENER = null;

	public static <E extends EntityW> E get(Entity entity){
		try{
			return (E)EntityUtil.IMPL.getConstructor(Entity.class).newInstance(entity);
		}
		catch(Exception e){
			return null;
		}
	}



}
