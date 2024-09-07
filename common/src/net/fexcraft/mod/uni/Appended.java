package net.fexcraft.mod.uni;

import net.fexcraft.mod.uni.tag.TagCW;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Appended<T> {

	private HashMap<Class<?>, Appendable<T>> appended = new HashMap<>();
	public final T type;

	public Appended(T ty){
		type = ty;
	}

	public void init(ArrayList<Appendable<T>> appendables){
		Appendable<T> npp = null;
		for(Appendable<T> app : appendables){
			npp = app.create(type);
			if(npp == null) continue;
			appended.put(npp.getClass(), npp);
		}
	}

	public <A> A get(String id){
		for(Appendable<T> val : appended.values()){
			if(val.id().equals(id)) return (A)val;
		}
		return null;
	}

	public <A> A get(Class<A> clazz){
		return (A)appended.get(clazz);
	}

	public void save(TagCW com){
		for(Appendable<T> val : appended.values()){
			TagCW tag = TagCW.create();
			val.save(type, tag);
			if(!tag.empty()){
				com.set(val.id(), tag);
			}
		}
	}

	public void load(TagCW com){
		for(Appendable<T> val : appended.values()){
			if(com.has(val.id())){
				val.load(type, com.getCompound(val.id()));
			}
		}
	}

}
