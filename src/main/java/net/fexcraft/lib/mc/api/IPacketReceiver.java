package net.fexcraft.lib.mc.api;

public interface IPacketReceiver<PACKET extends IPacket>{
	
	public default void processServerPacket(PACKET pkt){}
	
	public default void processClientPacket(PACKET pkt){}
	
}