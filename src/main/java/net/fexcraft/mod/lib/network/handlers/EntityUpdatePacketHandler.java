package net.fexcraft.mod.lib.network.handlers;

import java.util.HashSet;

import net.fexcraft.mod.lib.api.network.IPacketListener;
import net.fexcraft.mod.lib.api.network.IPacketReceiver;
import net.fexcraft.mod.lib.network.packet.PacketEntityUpdate;
import net.fexcraft.mod.lib.util.common.Static;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EntityUpdatePacketHandler {
	
	public static class Client implements IMessageHandler<PacketEntityUpdate, IMessage> {
		
		private static HashSet<IPacketListener> set = new HashSet<IPacketListener>();
		
		@Override
		public IMessage onMessage(final PacketEntityUpdate packet, MessageContext ctx) {
			IThreadListener ls = Minecraft.getMinecraft();
			ls.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					Entity entity = Minecraft.getMinecraft().world.getEntityByID(packet.id);
					if(entity != null && entity instanceof IPacketReceiver){
						((IPacketReceiver)entity).processClientPacket(packet);
					}
					if(packet.nbt.hasKey("target_listener")){
						for(IPacketListener pktl : set){
							if(pktl.getId().equals(packet.nbt.getString("target_listener"))){
								pktl.process(packet, null);
							}
						}
					}
				}
				
			});
			return null;
		}

		public static void addListener(IPacketListener listener) {
			set.add(listener);
		}
	}
	
	public static class Server implements IMessageHandler<PacketEntityUpdate, IMessage>{
		
		private static HashSet<IPacketListener> set = new HashSet<IPacketListener>();
		
		@Override
		public IMessage onMessage(final PacketEntityUpdate packet, MessageContext ctx) {
			IThreadListener ls = Static.getServer();
			ls.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					Entity entity = Static.getServer().worlds[packet.dim].getEntityByID(packet.id);
					if(entity != null && entity instanceof IPacketReceiver){
						((IPacketReceiver)entity).processServerPacket(packet);
					}
					if(packet.nbt.hasKey("target_listener")){
						for(IPacketListener pktl : set){
							if(pktl.getId().equals(packet.nbt.getString("target_listener"))){
								pktl.process(packet, null);
							}
						}
					}
				}
				
			});
			return null;
		}

		public static void addListener(IPacketListener listener) {
			set.add(listener);
		}
	}
	
}