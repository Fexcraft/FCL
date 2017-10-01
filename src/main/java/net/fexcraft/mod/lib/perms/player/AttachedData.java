package net.fexcraft.mod.lib.perms.player;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

public interface AttachedData {
	
	public String getId();
	
	public JsonObject save(UUID uuid);
	
	public AttachedData load(UUID uuid, @Nullable JsonObject obj);
	
}