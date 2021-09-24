package net.fexcraft.lib.mc.network;

import net.minecraft.network.PacketByteBuf;

public interface PacketListener {
	
	public String getId();
	
	public void process(PacketByteBuf packet, Object[] objs);
	
}