package net.fexcraft.lib.mc.network.handlers;

import java.util.HashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fexcraft.lib.mc.network.IPacketListener;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

public class CompoundTagPacketHandler implements PacketConsumer {
	
	private static HashMap<String, IPacketListener<CompoundTag>> sls = new HashMap<>(), cls = new HashMap<>();
	
	@Override
	public void accept(PacketContext context, PacketByteBuf buffer) {
		context.getTaskQueue().execute(() -> {
			CompoundTag compound = buffer.readCompoundTag();
			if(!compound.containsKey("target_listener")){
				Print.log("Received NBT Packet, but had no target listener, ignoring!");
				Print.log("[NBT] " + compound.toString());
				return;
			}
			IPacketListener<CompoundTag> listener = (context.getPacketEnvironment() == EnvType.CLIENT ? cls : sls).get(compound.getString("target_listener"));
			if(listener != null){
				listener.process(compound, context.getPlayer(), context.getPacketEnvironment());
			}
		});
	}

	public static void addListener(boolean server, IPacketListener<CompoundTag> listener) {
		(server ? sls : cls).put(listener.getId(), listener);
	}
	
}