package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.packet.PacketFile;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class FilePacket extends PacketFile implements CustomPacketPayload {

	public FilePacket(RegistryFriendlyByteBuf buf){
		decode(buf);
	}

	public static void encode(RegistryFriendlyByteBuf buf, FilePacket packet){
		packet.encode(buf);
	}

	@Override
	public Type<? extends CustomPacketPayload> type(){
		return FCL.IMG_PACKET_TYPE;
	}

}
