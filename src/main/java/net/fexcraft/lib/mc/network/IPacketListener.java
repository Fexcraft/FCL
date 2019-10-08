package net.fexcraft.lib.mc.network;

import net.fabricmc.api.EnvType;
import net.minecraft.entity.player.PlayerEntity;

public interface IPacketListener<PACKET> {
	
	public String getId();
	
	public void process(PACKET packet, PlayerEntity player, EnvType side);
	
}