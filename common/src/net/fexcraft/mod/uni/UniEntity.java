package net.fexcraft.mod.uni;

import net.fexcraft.mod.uni.world.EntityW;

import java.util.ArrayList;
import java.util.function.Consumer;
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

	public static <A> A getApp(Object ent, Class<A> clazz){
		UniEntity ue = get(ent);
		return ue == null ? null : ue.getApp(clazz);
	}

	public static <A> A getApp(Object ent, String id){
		UniEntity ue = get(ent);
		return ue == null ? null : ue.getApp(id);
	}

	public <A> boolean runIfPresent(Class<A> clazz, Consumer<A> cons){
		A app = getApp(clazz);
		if(app != null){
			cons.accept(app);
			return true;
		}
		return false;
	}

	public <A> boolean runIfPresent(String id, Consumer<A> cons){
		A app = getApp(id);
		if(app != null){
			cons.accept(app);
			return true;
		}
		return false;
	}

	public static EntityW getEntity(Object entity){
		return GETTER.apply(entity).entity;
	}

	public static EntityW getEntityN(Object entity){
		if(entity == null) return null;
		UniEntity ent = GETTER.apply(entity);
		return ent == null ? null : ent.entity;
	}

	public static void register(Appendable<UniEntity> app){
		appendables.add(app);
	}

	public void copy(UniEntity old){
		appended.copy(old.appended);
	}

}
