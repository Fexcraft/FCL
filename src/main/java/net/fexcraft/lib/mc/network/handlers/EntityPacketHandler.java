package net.fexcraft.lib.mc.network.handlers;

import java.util.HashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fexcraft.lib.mc.network.IPacketListener;
import net.fexcraft.lib.mc.network.IPacketReceiver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

public class EntityPacketHandler implements PacketConsumer {
	
	private static HashMap<String, IPacketListener<CompoundTag>> cl_set = new HashMap<>(), sr_set = new HashMap<>();
	
	@SuppressWarnings("unchecked") @Override
	public void accept(PacketContext context, PacketByteBuf buffer) {
		context.getTaskQueue().execute(() -> {
			Entity entity = context.getPlayer().world.getEntityById(buffer.readInt());
			CompoundTag compound = buffer.readCompoundTag();
			if(entity != null && entity instanceof IPacketReceiver){
				if(context.getPacketEnvironment() == EnvType.CLIENT){
					((IPacketReceiver<CompoundTag>)entity).processClientPacket(compound);
				}
				else{
					((IPacketReceiver<CompoundTag>)entity).processServerPacket(compound);
				}
			}
			if(compound.containsKey("target_listener")){
				IPacketListener<CompoundTag> list = (context.getPacketEnvironment() == EnvType.CLIENT ? cl_set : sr_set).get(compound.getString("target_listener"));
				if(list != null){ list.process(compound, context.getPlayer(), context.getPacketEnvironment()); }
			}
		});
	}

	public static void addListener(boolean server, IPacketListener<CompoundTag> listener) {
		(server ? sr_set : cl_set).put(listener.getId(), listener);
	}
	
}