package net.fexcraft.mod.lib.perms.player;

import java.util.UUID;

public interface IPlayerPerms {

	public void copy(PlayerPerms data);

	public void save(UUID uuid);

	public void load(UUID uuid);

	public PlayerPerms getInstance();
	
}