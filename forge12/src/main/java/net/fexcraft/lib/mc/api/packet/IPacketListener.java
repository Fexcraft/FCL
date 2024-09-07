package net.fexcraft.lib.mc.api.packet;

public interface IPacketListener <PACKET extends IPacket> {
	
	public String getId();
	
	public void process(PACKET packet, Object[] objs);
	
}