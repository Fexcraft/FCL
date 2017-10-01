package net.fexcraft.mod.lib.network.packet;

import io.netty.buffer.ByteBuf;
import net.fexcraft.mod.lib.api.network.IPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class Packet implements IPacket, IMessage{
	
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