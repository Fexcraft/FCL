package net.fexcraft.mod.lib.api.network;

public interface IPacketListener <PACKET extends IPacket> {
	
	public String getId();
	
	public void process(PACKET packet, Object[] objs);
	
}