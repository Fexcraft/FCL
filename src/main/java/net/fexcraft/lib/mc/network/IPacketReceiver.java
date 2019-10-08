package net.fexcraft.lib.mc.network;

public interface IPacketReceiver<PACKET> {
	
	public default void processServerPacket(PACKET pkt){}
	
	public default void processClientPacket(PACKET pkt){}
	
}