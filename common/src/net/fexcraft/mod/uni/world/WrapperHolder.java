package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.tag.TagCW;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class WrapperHolder {

	public static final ConcurrentHashMap<Object, WorldW> WORLDS = new ConcurrentHashMap<>();
	public static Function<Object, WorldW> LEVEL_PROVIDER;
	public static WrapperHolder INSTANCE;

	public static WorldW getWorld(Object obj){
		if(obj == null) return null;
		if(!WORLDS.containsKey(obj)){
			WORLDS.put(obj, LEVEL_PROVIDER.apply(obj));
		}
		return WORLDS.get(obj);
	}

	public static <W extends WorldW> W getClientWorld(){
		return INSTANCE.getClientWorld0();
	}

	public static boolean isSinglePlayer(){
		return false;
	}

	protected abstract boolean isSinglePlayer0();

	public abstract <W extends WorldW> W getClientWorld0();

	public static File getWorldFolder(WorldW world){
		return INSTANCE.getWorldFolder0(world);
	}

	public abstract File getWorldFolder0(WorldW world);

	public static V3I getPos(Object obj){
		return INSTANCE.getPos0(obj);
	}

	public abstract V3I getPos0(Object o);

	public static V3I getPos(long l){
		return INSTANCE.getPos0(l);
	}

	public abstract V3I getPos0(long l);

	public static CubeSide getSide(Object o){
		return INSTANCE.getSide0(o);
	}

	public abstract CubeSide getSide0(Object o);

	public static <S> S getLocalSide(CubeSide side){
		return INSTANCE.getLocalSide0(side);
	}

	public abstract <S> S getLocalSide0(CubeSide side);

	public static List<UUID> getOnlinePlayerIDs(){
		return INSTANCE.getOnlinePlayerIDs0();
	}

	public abstract List<UUID> getOnlinePlayerIDs0();

	public static UUID getUUIDFor(String string){
		return INSTANCE.getUUIDFor0(string);
	}

	public abstract UUID getUUIDFor0(String string);

	public static String getNameFor(UUID uuid){
		return INSTANCE.getNameFor0(uuid);
	}

	public abstract String getNameFor0(UUID uuid);

	public static void schedule(Runnable run){
		INSTANCE.schedule0(run);
	}

	public abstract void schedule0(Runnable run);

	public abstract void reset();
	
	public static TagCW read(File file){
		return INSTANCE.read0(file);
	}
	
	protected abstract TagCW read0(File file);

	public static void write(TagCW compound, File file){
		INSTANCE.write0(compound, file);
	}

	protected abstract void write0(TagCW compound, File file);

	public static V3I mutPos(Object pos){
		return INSTANCE.mutPos0(pos);
	}

	protected abstract V3I mutPos0(Object pos);

	public static V3I mutPos(int x, int y, int z){
		return INSTANCE.mutPos0(x, y, z);
	}

	protected abstract V3I mutPos0(int x, int y, int z);

	public static InputStream getDataResource(IDL loc) throws IOException {
		return INSTANCE.getDataResource0(loc);
	}

	protected abstract InputStream getDataResource0(IDL loc) throws IOException;

}
