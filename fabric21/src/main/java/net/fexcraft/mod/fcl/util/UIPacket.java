package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.fcl.FCL;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public record UIPacket(CompoundTag com) implements CustomPacketPayload {

	public UIPacket(RegistryFriendlyByteBuf buf){
		this(buf.readNbt());
	}

	public void encode(RegistryFriendlyByteBuf buf){
		buf.writeNbt(com);
	}

	public static void encode(RegistryFriendlyByteBuf buf, UIPacket packet){
		packet.encode(buf);
	}

	@Override
	public Type<? extends CustomPacketPayload> type(){
		return FCL.UI_PACKET_TYPE;
	}


}
