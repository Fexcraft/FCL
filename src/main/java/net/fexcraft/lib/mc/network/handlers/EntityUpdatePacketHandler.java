package net.fexcraft.lib.mc.network.handlers;

import java.util.HashSet;

import net.fexcraft.lib.mc.api.IPacketListener;
import net.fexcraft.lib.mc.api.IPacketReceiver;
import net.fexcraft.lib.mc.network.packet.PacketEntityUpdate;
import net.fexcraft.lib.mc.utils.Static;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EntityUpdatePacketHandler {
	
	public static class Client implements IMessageHandler<PacketEntityUpdate, IMessage> {
		
		private static HashSet<IPacketListener<PacketEntityUpdate>> set = new HashSet<IPacketListener<PacketEntityUpdate>>();
		
		@Override
		public IMessage onMessage(final PacketEntityUpdate packet, MessageContext ctx) {
			IThreadListener ls = Minecraft.getMinecraft();
			ls.addScheduledTask(new Runnable(){
				@SuppressWarnings("unchecked")
				@Override
				public void run(){
					Entity entity = null;
					for(Entity ent : Minecraft.getMinecraft().world.loadedEntityList){
						if(ent.getEntityId() == packet.id){ entity = ent; }
					}
					if(entity != null && entity instanceof IPacketReceiver){
						((IPacketReceiver<PacketEntityUpdate>)entity).processClientPacket(packet);
					}
					if(packet.nbt.hasKey("target_listener")){
						for(IPacketListener<PacketEntityUpdate> pktl : set){
							if(pktl.getId().equals(packet.nbt.getString("target_listener"))){
								pktl.process(packet, null);
							}
						}
					}
				}
				
			});
			return null;
		}

		public static void addListener(IPacketListener<PacketEntityUpdate> listener) {
			set.add(listener);
		}
	}
	
	public static class Server implements IMessageHandler<PacketEntityUpdate, IMessage>{
		
		private static HashSet<IPacketListener<PacketEntityUpdate>> set = new HashSet<IPacketListener<PacketEntityUpdate>>();
		
		@Override
		public IMessage onMessage(final PacketEntityUpdate packet, MessageContext ctx) {
			IThreadListener ls = Static.getServer();
			ls.addScheduledTask(new Runnable(){
				@SuppressWarnings("unchecked")
				@Override
				public void run(){
					Entity entity = Static.getServer().worlds[packet.dim].getEntityByID(packet.id);
					if(entity != null && entity instanceof IPacketReceiver){
						((IPacketReceiver<PacketEntityUpdate>)entity).processServerPacket(packet);
					}
					if(packet.nbt.hasKey("target_listener")){
						for(IPacketListener<PacketEntityUpdate> pktl : set){
							if(pktl.getId().equals(packet.nbt.getString("target_listener"))){
								pktl.process(packet, null);
							}
						}
					}
				}
				
			});
			return null;
		}

		public static void addListener(IPacketListener<PacketEntityUpdate> listener) {
			set.add(listener);
		}
	}
	
}