package net.fexcraft.mod.uni;

import net.fexcraft.mod.uni.world.ChunkW;
import net.fexcraft.mod.uni.world.EntityW;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * @author Ferdinand Calo'
 */
public class UniChunk {

	public static Function<Object, UniChunk> GETTER = null;
	public static Function<Object, ChunkW> CHUNK_GETTER = null;
	private static ArrayList<Appendable<UniChunk>> appendables = new ArrayList<>();
	public final Appended<UniChunk> appended = new Appended(this);
	public ChunkW chunk;

	public UniChunk(){}

	public UniChunk set(Object ent){
		chunk = CHUNK_GETTER.apply(ent);
		appended.init(appendables);
		return this;
	}

	public static UniChunk get(Object chunk){
		return GETTER.apply(chunk);
	}

	public <A> A getApp(Class<A> clazz){
		return appended.get(clazz);
	}

	public <A> A getApp(String id){
		return appended.get(id);
	}

	public static ChunkW getChunk(Object chunk){
		return GETTER.apply(chunk).chunk;
	}

	public static void register(Appendable<UniChunk> app){
		appendables.add(app);
	}

}
