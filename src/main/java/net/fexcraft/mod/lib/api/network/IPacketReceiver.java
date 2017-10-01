package net.fexcraft.mod.lib.api.network;

public interface IPacketReceiver<PACKET extends IPacket>{
	
	public default void processServerPacket(PACKET pkt){}
	
	public default void processClientPacket(PACKET pkt){}
	
}