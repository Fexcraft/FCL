package net.fexcraft.mod.uni;

import net.fexcraft.mod.uni.world.EntityW;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * @author Ferdinand Calo'
 */
public class UniEntity {

	public static Function<Object, UniEntity> GETTER = null;
	public static Function<Object, EntityW> ENTITY_GETTER = null;
	private static ArrayList<Appendable<UniEntity>> appendables = new ArrayList<>();
	public final Appended<UniEntity> appended = new Appended(this);
	public EntityW entity;

	public UniEntity(){}

	public UniEntity set(Object ent){
		entity = ENTITY_GETTER.apply(ent);
		appended.init(appendables);
		return this;
	}

	public static UniEntity get(Object playerent){
		return GETTER.apply(playerent);
	}

	public <A> A getApp(Class<A> clazz){
		return appended.get(clazz);
	}

	public <A> A getApp(String id){
		return appended.get(id);
	}

	public static EntityW getEntity(Object playerent){
		return GETTER.apply(playerent).entity;
	}

	public static void register(Appendable<UniEntity> app){
		appendables.add(app);
	}

}
