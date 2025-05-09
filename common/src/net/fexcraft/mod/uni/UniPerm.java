package net.fexcraft.mod.uni;

import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.WrapperHolder;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class UniPerm {

	protected static ConcurrentHashMap<String, Integer> PERMS = new ConcurrentHashMap<>();
	public static UniPerm INSTANCE = new DefUniPerm();

	public static void register(String perm){
		register(perm, 4);
	}

	public static void register(String perm, int lvl){
		if(PERMS.containsKey(perm)) return;
		PERMS.put(perm, lvl);
		INSTANCE.register0(perm, lvl);
	}

	protected abstract void register0(String perm, int lvl);

	public static boolean has(EntityW ent, String perm){
		return INSTANCE.has0(ent, perm);
	}

	protected abstract boolean has0(EntityW ent, String perm);

	public static class DefUniPerm extends UniPerm {

		@Override
		protected void register0(String perm, int lvl){
			//
		}

		@Override
		protected boolean has0(EntityW ent, String perm){
			int lvl = PERMS.getOrDefault(perm, 4);
			return lvl == 0 || WrapperHolder.isOp(ent, lvl);
		}

	}

}
