package net.fexcraft.mod.lib.perms;

import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonElement;
import net.fexcraft.mod.lib.FCL;
import net.fexcraft.mod.lib.perms.PermissionNode.Type;
import net.fexcraft.mod.lib.perms.player.PlayerHandler;
import net.fexcraft.mod.lib.perms.player.PlayerPerms;
import net.fexcraft.mod.lib.util.common.Print;
import net.fexcraft.mod.lib.util.json.JsonUtil;
import net.minecraft.entity.player.EntityPlayer;

public class PermManager {
	
	private static TreeMap<String, PermissionNode> permissionnodes = new TreeMap<String, PermissionNode>();
	private static TreeMap<String, Rank> ranks = new TreeMap<String, Rank>();
	private static TreeSet<PermissionNode> def = new TreeSet<PermissionNode>();
	private static final ArrayList<String> mods = new ArrayList<String>();
	private static boolean enabled;
	public static File rankDir, userDir;
	
	public static final void initialize(){
		add(Permissions.GENERAL_BLOCK_BREAK, Type.BOOLEAN, true, true);
		add(Permissions.GENERAL_BLOCK_PLACE, Type.BOOLEAN, true, true);
		add(Permissions.FCL_PERMISSION_EDIT, Type.BOOLEAN, false, true);
		
		File parent = new File(FCL.getInstance().getConfigDirectory().getParentFile(), "/fcl-perms");
		rankDir = new File(parent, "/ranks/");
		if(!rankDir.exists()){
			rankDir.mkdirs();
		}
		File defRank = new File(rankDir, "/default.rank");
		if(!defRank.exists()){
			JsonUtil.write(defRank, new Rank().toJson());
		}
		loadRanks();
		userDir = new File(parent, "/users/");
		if(!userDir.exists()){
			userDir.mkdirs();
		}
		
		PlayerHandler.initialize();
		
		//DEBUG
		/*for(Rank rank : ranks.values()){
			Print.debug(rank.toJson());
		}
		Static.stop();*/
	}
	
	public static void saveRank(Rank rank){
		JsonUtil.write(new File(rankDir, "/" + rank.getId() + ".rank"), rank.toJson());
		Print.debug("Saved Rank '" + rank.getName() + "'.");
	}
	
	private static void loadRanks(){
		for(File file : rankDir.listFiles()){
			try{
				Rank rank = new Rank(JsonUtil.get(file));
				ranks.put(rank.getId(), rank);
				if(rank.def){
					rank.control(def);
					JsonUtil.write(new File(rankDir, "/default.rank"), rank.toJson());
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			Print.debug(file.toPath());
		}
	}

	public static final void setEnabled(String modid){
		mods.add(modid);
		enabled = true;
	}
	
	public static final void add(String id, Type type, Object default_value, boolean addToDef){
		switch(type){
			case BOOLEAN:
				permissionnodes.put(id, new PermissionBool(id, type, default_value));
				if(addToDef){
					def.add(new PermissionBool(id, type, default_value));
				}
				break;
			case NUMBER:
				permissionnodes.put(id, new PermissionNumber(id, type, default_value));
				if(addToDef){
					def.add(new PermissionNumber(id, type, default_value));
				}
				break;
			case STRING:
				permissionnodes.put(id, new PermissionString(id, type, default_value));
				if(addToDef){
					def.add(new PermissionString(id, type, default_value));
				}
				break;
			default:
				break;
		}
	}
	
	public static final void add(Rank rank){
		ranks.put(rank.getId(), rank);
	}
	
	public TreeMap<String, PermissionNode> getAllPermissionNodes(){
		return permissionnodes;
	}
	
	public TreeMap<String, Rank> getAllRanks(){
		return ranks;
	}
	
	public static final Rank getRank(String id){
		return ranks.get(id);
	}

	public static final PermissionNode getPermission(String id){
		return permissionnodes.get(id);
	}

	public static PermissionNode loadPermission(Entry<String, JsonElement> entry){
		PermissionNode node = getPermission(entry.getKey());
		if(node != null){
			switch(node.type){
				case BOOLEAN:
					return new PermissionBool(node.id, entry.getValue());
				case NUMBER:
					return new PermissionNumber(node.id, entry.getValue());
				case STRING:
					return new PermissionString(node.id, entry.getValue());
				default:
					return null;
			}
		}
		else return null;
	}

	public static PermissionNode copy(String string){
		PermissionNode node = getPermission(string);
		if(node != null){
			switch(node.type){
				case BOOLEAN:
					return new PermissionBool(node.id, node.toJsonElement());
				case NUMBER:
					return new PermissionNumber(node.id, node.toJsonElement());
				case STRING:
					return new PermissionString(node.id, node.toJsonElement());
				default:
					return null;
			}
		}
		else return null;
	}

	public static boolean isEnabled(){
		return enabled;
	}
	
	public static PlayerPerms getPlayerPerms(EntityPlayer player){
		return PlayerHandler.getPerms(player);
	}
	
}