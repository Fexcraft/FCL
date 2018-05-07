package net.fexcraft.mod.lib.network.packet;

import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import net.fexcraft.mod.lib.api.network.IPacket;
import net.fexcraft.mod.lib.util.json.JsonUtil;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketJsonObject implements IPacket, IMessage{
	
	public JsonObject obj;
	
	public PacketJsonObject(){}
	
	public PacketJsonObject(JsonObject obj){
		this.obj = obj;
	}

	@Override
	public void toBytes(ByteBuf buf){
		byte[] bytes = obj.toString().getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        return;
	}

	@Override
	public void fromBytes(ByteBuf buf){
		int length = buf.readInt();
		String str = buf.toString(buf.readerIndex(), length, StandardCharsets.UTF_8);
        buf.readerIndex(buf.readerIndex() + length);
		obj = JsonUtil.getObjectFromString(str);
		return;
	}
}