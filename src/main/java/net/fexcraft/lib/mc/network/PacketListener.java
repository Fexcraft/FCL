package net.fexcraft.lib.mc.network;

public interface PacketListener<PKT> {
	
	public String getId();
	
	public void process(PKT packet, Object... objs);
	
}