package net.fexcraft.mod.lib.network.packet;

import io.netty.buffer.ByteBuf;
import net.fexcraft.mod.lib.api.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketKeyInput implements IPacket, IMessage{
	
	public int data;
	public String target;
	
	public PacketKeyInput(){}
	
	public PacketKeyInput(int data, String target){
		this.data = data;
		this.target = target;
	}

	@Override
	public void toBytes(ByteBuf bbuf){
		PacketBuffer buf = new PacketBuffer(bbuf);
		buf.writeInt(data);
		buf.writeString(target);
	}

	@Override
	public void fromBytes(ByteBuf bbuf){
		PacketBuffer buf = new PacketBuffer(bbuf);
		data = buf.readInt();
		target = buf.readString(999);
	}
	
}