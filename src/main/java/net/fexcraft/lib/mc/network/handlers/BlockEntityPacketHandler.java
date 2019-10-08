package net.fexcraft.lib.mc.network.handlers;

import java.util.HashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fexcraft.lib.mc.network.IPacketListener;
import net.fexcraft.lib.mc.network.IPacketReceiver;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class BlockEntityPacketHandler implements PacketConsumer {
	
	private static HashMap<String, IPacketListener<CompoundTag>> cl_set = new HashMap<>(), sr_set = new HashMap<>();
	
	@SuppressWarnings("unchecked") @Override
	public void accept(PacketContext context, PacketByteBuf buffer){
		context.getTaskQueue().execute(() -> {
			BlockEntity block = context.getPlayer().world.getBlockEntity(BlockPos.fromLong(buffer.readLong()));
			CompoundTag compound = buffer.readCompoundTag();
			if(block != null && block instanceof IPacketReceiver){
				if(context.getPacketEnvironment() == EnvType.CLIENT){
					((IPacketReceiver<CompoundTag>)block).processClientPacket(compound);
				}
				else{
					((IPacketReceiver<CompoundTag>)block).processServerPacket(compound);
				}
			}
			if(compound.containsKey("target_listener")){
				IPacketListener<CompoundTag> list = (context.getPacketEnvironment() == EnvType.CLIENT ? cl_set : sr_set).get(compound.getString("target_listener"));
				if(list != null){ list.process(compound, context.getPlayer(), context.getPacketEnvironment()); }
			}
			//
		});
	}

	public static void addListener(boolean server, IPacketListener<CompoundTag> listener) {
		(server ? sr_set : cl_set).put(listener.getId(), listener);
	}
	
}