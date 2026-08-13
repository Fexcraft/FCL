package net.fexcraft.mod.uni;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.WrapperHolder;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniPerm {

	protected static ConcurrentHashMap<String, Integer> PERMS = new ConcurrentHashMap<>();
	public static UniPerm INSTANCE = new UniPerm();

	public static void register(String perm){
		register(perm, 4);
	}

	public static void register(String perm, int lvl){
		if(PERMS.containsKey(perm)) return;
		PERMS.put(perm, lvl);
		INSTANCE.register0(perm, lvl);
	}

	public void register0(String perm, int lvl){}

	public static boolean has(EntityW ent, String perm){
		return INSTANCE.has0(ent, perm);
	}

	public boolean has0(EntityW ent, String perm){
		int lvl = PERMS.getOrDefault(perm, 4);
		return lvl == 0 || WrapperHolder.isSinglePlayer() || WrapperHolder.isOp(ent, lvl);
	}

	public static boolean can_interact(EntityW ent, V3I pos){
		return INSTANCE.can_interact0(ent, pos);
	}

	public boolean can_interact0(EntityW ent, V3I pos){
		return true;
	}

	public static boolean can_break(EntityW ent, V3I pos){
		return INSTANCE.can_break0(ent, pos);
	}

	public boolean can_break0(EntityW ent, V3I pos){
		return true;
	}

	public static boolean can_place(EntityW ent, V3I pos){
		return INSTANCE.can_place0(ent, pos);
	}

	public boolean can_place0(EntityW ent, V3I pos){
		return true;
	}

}
