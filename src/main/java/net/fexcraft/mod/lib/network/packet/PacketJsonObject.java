package net.fexcraft.mod.lib.network.packet;

import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import net.fexcraft.mod.lib.api.network.IPacket;
import net.fexcraft.mod.lib.util.json.JsonUtil;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketJsonObject implements IPacket, IMessage{
	
	public JsonObject obj;
	
	public PacketJsonObject(){}
	
	public PacketJsonObject(JsonObject obj){
		this.obj = obj;
	}

	@Override
	public void toBytes(ByteBuf buf){
		ByteBufUtils.writeUTF8String(buf, obj.toString());
	}

	@Override
	public void fromBytes(ByteBuf buf){
		obj = JsonUtil.getObjectFromString(ByteBufUtils.readUTF8String(buf));
	}
}