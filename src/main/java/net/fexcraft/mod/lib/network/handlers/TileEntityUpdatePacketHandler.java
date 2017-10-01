package net.fexcraft.mod.lib.network.handlers;

import java.util.HashSet;

import net.fexcraft.mod.lib.api.network.IPacketListener;
import net.fexcraft.mod.lib.api.network.IPacketReceiver;
import net.fexcraft.mod.lib.network.packet.PacketTileEntityUpdate;
import net.fexcraft.mod.lib.util.common.Static;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TileEntityUpdatePacketHandler{
	
	public static class Client implements IMessageHandler<PacketTileEntityUpdate, IMessage>{
		
		private static HashSet<IPacketListener> set = new HashSet<IPacketListener>();
		
		@Override
		public IMessage onMessage(final PacketTileEntityUpdate packet, MessageContext ctx) {
			IThreadListener ls = Minecraft.getMinecraft();
			ls.addScheduledTask(new Runnable(){
				@Override
				public void run() {
					Chunk ck = Minecraft.getMinecraft().world.getChunkFromBlockCoords(packet.pos);
					if(ck.isLoaded()){
						TileEntity te = Minecraft.getMinecraft().world.getTileEntity(packet.pos);
						if(te instanceof IPacketReceiver){
							((IPacketReceiver)te).processClientPacket(packet);
						}
						if(packet.nbt.hasKey("target_listener")){
							for(IPacketListener pktl : set){
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

		public static void addListener(IPacketListener listener) {
			set.add(listener);
		}
	}
	
	public static class Server implements IMessageHandler<PacketTileEntityUpdate, IMessage>{
		
		private static HashSet<IPacketListener> set = new HashSet<IPacketListener>();
		
		@Override
		public IMessage onMessage(final PacketTileEntityUpdate packet, MessageContext ctx) {
			IThreadListener ls = Static.getServer();
			ls.addScheduledTask(new Runnable(){
				@Override
				public void run() {
					Chunk ck = Static.getServer().worlds[packet.dim].getChunkFromBlockCoords(packet.pos);
					if(ck.isLoaded()){
						TileEntity te = Static.getServer().worlds[packet.dim].getTileEntity(packet.pos);
						if(te instanceof IPacketReceiver){
							((IPacketReceiver)te).processServerPacket(packet);
						}
						if(packet.nbt.hasKey("target_listener")){
							for(IPacketListener pktl : set){
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

		public static void addListener(IPacketListener listener) {
			set.add(listener);
		}
	}
	
}