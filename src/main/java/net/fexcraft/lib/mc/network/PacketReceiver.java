package net.fexcraft.lib.mc.network;

import net.minecraft.network.PacketByteBuf;

public interface PacketReceiver {
	
	public default void processServerPacket(PacketByteBuf packet){}
	
	public default void processClientPacket(PacketByteBuf packet){}
	
}