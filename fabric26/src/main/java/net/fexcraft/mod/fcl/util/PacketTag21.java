package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.packet.PacketTag;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class PacketTag21 extends PacketTag implements CustomPacketPayload {

	public PacketTag21(){}

	public PacketTag21(String key, TagCW tag){
		fill(key, tag);
	}

	public static PacketTag21 decode(RegistryFriendlyByteBuf buf){
		String key = buf.readUtf(buf.readInt());
		TagCW com = TagCW.wrap(buf.readNbt());
		return (PacketTag21)new PacketTag21().fill(key, com);
	}

	public void encode(RegistryFriendlyByteBuf buf){
		buf.writeInt(lis.length());
		buf.writeUtf(lis);
		buf.writeNbt(com.local());
	}

	public static void encode(RegistryFriendlyByteBuf buf, PacketTag21 packet){
		packet.encode(buf);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){
		return FCL.TAG_PACKET_TYPE;
	}

}
