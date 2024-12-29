package net.fexcraft.mod.fcl.util;

import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.ui.UIKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public record UISync(UIKey key, V3I pos, JsonMap map) implements CustomPacketPayload {

	public UISync(RegistryFriendlyByteBuf buffer){
		this(UIKey.find(buffer.readUtf(buffer.readInt())),
			new V3I(buffer.readInt(), buffer.readInt(), buffer.readInt()),
			JsonHandler.parse(buffer.readUtf(buffer.readInt()), true).asMap());
	}

	public void encode(RegistryFriendlyByteBuf buffer){
		buffer.writeInt(key.key.length());
		buffer.writeUtf(key.key);
		buffer.writeInt(pos.x);
		buffer.writeInt(pos.y);
		buffer.writeInt(pos.z);
		String str = JsonHandler.toString(UniReg.getMenuJson(key.key), JsonHandler.PrintOption.FLAT);
		buffer.writeInt(str.length());
		buffer.writeUtf(str);
	}

	public static void encode(RegistryFriendlyByteBuf buf, UISync packet){
		packet.encode(buf);
	}

	@Override
	public Type<? extends CustomPacketPayload> type(){
		return FCL.UI_SYNC_TYPE;
	}


}
