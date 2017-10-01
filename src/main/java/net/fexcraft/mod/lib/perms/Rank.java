package net.fexcraft.mod.lib.perms;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fexcraft.mod.lib.perms.PermissionNode.Type;

public class Rank {
	
	public boolean def = false;
	private TreeMap<String, PermissionNode> permissions = new TreeMap<String, PermissionNode>();
	public Rank parent;
	//
	private String id;
	private String name;
	/** You can also extend this class and override the save/load methods to implement your own data.*/
	private String prefix;
	
	/**
	 * Default constructor for Default Rank Instance, never use this! It's only for pre-init of the default rank;
	 */
	public Rank(){
		id = "default";
		name = "Default";
		setPrefix("def-rank");
		def = true;
	}
	
	public Rank(JsonObject obj){
		if(obj.has("Parent")){
			parent = PermManager.getRank(obj.get("Parent").getAsString());
		}
		if(obj.has("ID")){
			id = obj.get("ID").getAsString();
			if(id.equals("default")){
				def = true;
			}
		}
		if(obj.has("Name")){
			name = obj.get("Name").getAsString();
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
		if(obj.has("Prefix")){
			prefix = obj.get("Prefix").getAsString();
		}
	}
	
	public JsonObject toJson(){
		JsonObject obj = new JsonObject();
		if(parent != null){
			obj.addProperty("Parent", parent.id);
		}
		obj.addProperty("ID", id);
		obj.addProperty("Name", name == null ? id : name);
		JsonObject array = new JsonObject();
		for(PermissionNode node : permissions.values()){
			array.add(node.id, node.toJsonElement());
		}
		obj.add("Permissions", array);
		if(prefix != null){
			obj.addProperty("Prefix", prefix);
		}
		return obj;
	}
	
	/**
	 * Checks if the Rank contains this node and if it equals.
	 * @param node
	 * @return
	 */
	public boolean hasPermissionNode(PermissionNode node){
		PermissionNode perm = permissions.get(node.getId());
		return perm == null ? (parent == null ? false : parent.hasPermissionNode(node)) : perm.equals(node);
	}
	
	/**
	 * General check if the Rank has this permission.
	 * @param s The requested permission to be checked.
	 * @return True if there is no mod using the permissions system registered.<br>Else returns the boolean value of the requested Node or null if not found.
	 */
	public boolean hasPermission(String s){
		if(!PermManager.isEnabled()){
			return true;
		}
		PermissionNode perm = permissions.get(s);
		if(perm == null){
			if(parent == null){
				PermissionNode node = PermManager.getPermission(s);
				return node == null ? false : node.getBooleanValue();
			}
			else{
				return parent.hasPermission(s);
			}
		}
		else return perm.getBooleanValue();
	}
	
	/**
	 * Get's the current instance of the requested PermissionNode.
	 * @param s
	 * @return
	 */
	public PermissionNode getPermissionNode(String s){
		return permissions.get(s);
	}
	
	/**
	 * Get's the current instance of the requested PermissionNode as String.
	 * @param s
	 * @return
	 */
	public String getPermissionString(String s){
		return permissions.get(s).getStringValue();
	}
	
	/**
	 * Get's the current instance of the requested PermissionNode as Boolean.
	 * @param s
	 * @return
	 */
	public Boolean getPermissionBoolean(String s){
		return permissions.get(s).getBooleanValue();
	}
	
	/**
	 * Get's the current instance of the requested PermissionNode as Number.
	 * @param s
	 * @return
	 */
	public Number getPermissionNumber(String s){
		return permissions.get(s).getNumberValue();
	}
	
	/**
	 * Get's a list of all permission associated with this rank.
	 * @return
	 */
	public TreeMap<String, PermissionNode> getPermissions(){
		return permissions;
	}
	
	/**
	 * @return The Rank's Name.
	 */
	public String getName(){
		return name == null ? getId() : name;
	}

	public String getId(){
		return id;
	}
	
	@Override
	public String toString(){
		return this.toJson().toString();
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

	public void control(TreeSet<PermissionNode> set){
		if(!def){
			return;
		}
		for(PermissionNode node : set){
			if(!permissions.containsKey(node.getId())){
				permissions.put(node.id, node);
			}
		}
	}

	public String getPrefix(){
		return prefix;
	}

	public void setPrefix(String prefix){
		this.prefix = prefix;
	}
	
}