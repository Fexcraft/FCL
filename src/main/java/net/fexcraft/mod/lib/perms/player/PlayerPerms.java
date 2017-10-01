package net.fexcraft.mod.lib.perms.player;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fexcraft.mod.lib.perms.PermManager;
import net.fexcraft.mod.lib.perms.PermissionBool;
import net.fexcraft.mod.lib.perms.PermissionNode;
import net.fexcraft.mod.lib.perms.PermissionNumber;
import net.fexcraft.mod.lib.perms.PermissionString;
import net.fexcraft.mod.lib.perms.Rank;
import net.fexcraft.mod.lib.perms.PermissionNode.Type;
import net.fexcraft.mod.lib.util.common.Print;
import net.fexcraft.mod.lib.util.json.JsonUtil;

public class PlayerPerms implements IPlayerPerms {
	
	private TreeMap<String, PermissionNode> permissions = new TreeMap<String, PermissionNode>();
	private HashMap<Class, AttachedData> data = new HashMap<Class, AttachedData>();
	private static final HashSet<Class> set = new HashSet<Class>();
	private Rank rank;
	
	public PlayerPerms(){
		//
	}

	@Override
	public void load(UUID uuid){
		JsonElement elm = JsonUtil.read(new File(PermManager.userDir, "/" + uuid.toString() + ".perm"), false);
		if(elm == null){
			setRank(PermManager.getRank("default"));
			Print.log("No perm data for " + uuid.toString() + " found! Setting to default rank and saving.");
			for(Class clazz : set){
				try{
					this.data.put(clazz, ((AttachedData)clazz.getConstructor(PlayerPerms.class).newInstance(this)).load(uuid, null));
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			save(uuid);
		}
		else{
			JsonObject obj = elm.getAsJsonObject();
			setRank(PermManager.getRank(obj.get("Rank").getAsString()));
			try{
				JsonElement data = obj.get("AttachedData").getAsJsonObject();
				if(data != null){
					for(Class clazz : set){
						try{
							AttachedData iad = (AttachedData)clazz.getConstructor(PlayerPerms.class).newInstance(this);
							JsonElement jsn = data.getAsJsonObject().get(iad.getId());
							iad.load(uuid, jsn == null ? null : jsn.getAsJsonObject());
							this.data.put(clazz, iad);
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(obj.has("Permissions")){
				JsonObject perms = obj.get("Permissions").getAsJsonObject();
				for(Entry<String, JsonElement> entry : perms.entrySet()){
					PermissionNode perm = PermManager.loadPermission(entry);
					if(perm != null){
						permissions.put(entry.getKey(), perm);
					}
				}
			}
			save(uuid);
		}
	}
	
	@Override
	public void save(UUID uuid){
		JsonUtil.write(new File(PermManager.userDir, "/" + uuid.toString() + ".perm"), toJson(uuid));
	}
	
	public JsonObject toJson(UUID uuid){
		JsonObject obj = new JsonObject();
		obj.addProperty("Rank", rank == null ? "default" : rank.getId());
		JsonObject data = new JsonObject();
		this.data.values().forEach((adata) -> {
			data.add(adata.getId(), adata.save(uuid));
		});
		obj.add("AttachedData", data);
		JsonObject array = new JsonObject();
		for(PermissionNode node : permissions.values()){
			array.add(node.getId(), node.toJsonElement());
		}
		obj.add("Permissions", array);
		Print.debug(obj);
		return obj;
	}

	@Override
	public void copy(PlayerPerms ipd){
		PlayerPerms data = (PlayerPerms)ipd;
		this.permissions = data.permissions;
		this.rank = data.rank;
		this.data = data.data;
		
		/*for(AttachedData entry : this.data.values()){
			entry.copy(ipd);
		}*/
	}

	public Rank getRank(){
		return rank;
	}

	public void setRank(Rank rank){
		this.rank = rank;
	}
	
	@Override
	public String toString(){
		return rank.toJson().toString();
	}

	@Override
	public PlayerPerms getInstance(){
		return this;
	}

	public boolean hasPermission(String string){
		if(!PermManager.isEnabled()){
			return true;
		}
		PermissionNode perm = permissions.get(string);
		if(perm == null){
			return rank.hasPermission(string);
		}
		else return perm.getBooleanValue();
	}
	
	public PermissionNode getPermissionNode(String s){
		return permissions.get(s);
	}
	
	public String getPermissionString(String s){
		return permissions.get(s).getStringValue();
	}
	
	public Boolean getPermissionBoolean(String s){
		return permissions.get(s).getBooleanValue();
	}
	
	public Number getPermissionNumber(String s){
		return permissions.get(s).getNumberValue();
	}
	
	public TreeMap<String, PermissionNode> getPermissions(){
		return permissions;
	}
	
	public final boolean add(String id, Type type, Object default_value){
		PermissionNode node = PermManager.getPermission(id);
		if(node == null){
			return false;
		}
		else{
			switch(type){
				case BOOLEAN:
					permissions.put(id, new PermissionBool(id, type, default_value));
					return true;
				case NUMBER:
					permissions.put(id, new PermissionNumber(id, type, default_value));
					return true;
				case STRING:
					permissions.put(id, new PermissionString(id, type, default_value));
					return true;
				default:
					return false;
			}
		}
	}
	
	public static void addAdditionalData(Class type){
		set.add(type);
	}
	
	public <T extends AttachedData> T getAdditionalData(Class<T> clazz){
		return (T)data.get(clazz);
	}
	
}