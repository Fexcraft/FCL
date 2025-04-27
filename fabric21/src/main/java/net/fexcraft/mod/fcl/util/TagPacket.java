package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public record TagPacket(String key, TagCW com) implements CustomPacketPayload {

	public static TagPacket decode(RegistryFriendlyByteBuf buf){
		TagCW com = TagCW.wrap(buf.readNbt());
		String key = buf.readUtf(buf.readInt());
		return new TagPacket(key, com);
	}

	public void encode(RegistryFriendlyByteBuf buf){
		buf.writeNbt(com.local());
		buf.writeInt(key.length());
		buf.writeUtf(key);
	}

	public static void encode(RegistryFriendlyByteBuf buf, TagPacket packet){
		packet.encode(buf);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){
		return FCL.TAG_PACKET_TYPE;
	}

}
