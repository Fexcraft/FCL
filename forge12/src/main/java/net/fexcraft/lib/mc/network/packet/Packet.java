package net.fexcraft.lib.mc.network.packet;

import io.netty.buffer.ByteBuf;
import net.fexcraft.lib.mc.api.packet.IPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class Packet implements IPacket, IMessage {
	
	public int data;
	
	public Packet(){}
	
	public Packet(int data){
		this.data = data;
	}

	@Override
	public void toBytes(ByteBuf buf){
		buf.writeInt(data);
	}

	@Override
	public void fromBytes(ByteBuf buf){
		data = buf.readInt();
	}
	
}