package net.fexcraft.lib.mc.network.handlers;

import java.util.HashSet;

import net.fexcraft.lib.mc.api.IPacketListener;
import net.fexcraft.lib.mc.api.IPacketReceiver;
import net.fexcraft.lib.mc.network.packet.PacketTileEntityUpdate;
import net.fexcraft.lib.mc.utils.Static;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TileEntityUpdatePacketHandler{
	
	public static class Client implements IMessageHandler<PacketTileEntityUpdate, IMessage>{
		
		private static HashSet<IPacketListener<PacketTileEntityUpdate>> set = new HashSet<IPacketListener<PacketTileEntityUpdate>>();
		
		@Override
		public IMessage onMessage(final PacketTileEntityUpdate packet, MessageContext ctx) {
			IThreadListener ls = Minecraft.getMinecraft();
			ls.addScheduledTask(new Runnable(){
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					Chunk ck = Minecraft.getMinecraft().world.getChunkFromBlockCoords(packet.pos);
					if(ck.isLoaded()){
						TileEntity te = Minecraft.getMinecraft().world.getTileEntity(packet.pos);
						if(te instanceof IPacketReceiver){
							((IPacketReceiver<PacketTileEntityUpdate>)te).processClientPacket(packet);
						}
						if(packet.nbt.hasKey("target_listener")){
							for(IPacketListener<PacketTileEntityUpdate> pktl : set){
								if(pktl.getId().equals(packet.nbt.getString("target_listener"))){
									pktl.process(packet, null);
								}
							}
						}
					}
				}
				
			});
			return null;
		}

		public static void addListener(IPacketListener<PacketTileEntityUpdate> listener) {
			set.add(listener);
		}
	}
	
	public static class Server implements IMessageHandler<PacketTileEntityUpdate, IMessage>{
		
		private static HashSet<IPacketListener<PacketTileEntityUpdate>> set = new HashSet<IPacketListener<PacketTileEntityUpdate>>();
		
		@Override
		public IMessage onMessage(final PacketTileEntityUpdate packet, MessageContext ctx) {
			IThreadListener ls = Static.getServer();
			ls.addScheduledTask(new Runnable(){
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					Chunk ck = Static.getServer().worlds[packet.dim].getChunkFromBlockCoords(packet.pos);
					if(ck.isLoaded()){
						TileEntity te = Static.getServer().worlds[packet.dim].getTileEntity(packet.pos);
						if(te instanceof IPacketReceiver){
							((IPacketReceiver<PacketTileEntityUpdate>)te).processServerPacket(packet);
						}
						if(packet.nbt.hasKey("target_listener")){
							for(IPacketListener<PacketTileEntityUpdate> pktl : set){
								if(pktl.getId().equals(packet.nbt.getString("target_listener"))){
									pktl.process(packet, null);
								}
							}
						}
					}
				}
				
			});
			return null;
		}

		public static void addListener(IPacketListener<PacketTileEntityUpdate> listener) {
			set.add(listener);
		}
	}
	
}