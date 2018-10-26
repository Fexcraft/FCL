package net.fexcraft.lib.mc.network.handlers;

import java.util.HashMap;

import net.fexcraft.lib.mc.api.IPacketListener;
import net.fexcraft.lib.mc.network.packet.PacketJsonObject;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class JsonObjectPacketHandler{
	
	private static HashMap<String, IPacketListener<PacketJsonObject>> sls = new HashMap<String, IPacketListener<PacketJsonObject>>();
	private static HashMap<String, IPacketListener<PacketJsonObject>> cls = new HashMap<String, IPacketListener<PacketJsonObject>>();
	
	public static class Server implements IMessageHandler<PacketJsonObject, IMessage> {
		@Override
		public IMessage onMessage(final PacketJsonObject packet, final MessageContext ctx) {
			IThreadListener ls = FMLCommonHandler.instance().getMinecraftServerInstance();
			ls.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					if(!packet.obj.has("target_listener")){
						Print.log("[FCL] Received JSON Packet, but had no target listener, ignoring!");
						Print.log("[OBJ] " + packet.obj.toString());
						return;
					}
					IPacketListener<PacketJsonObject> listener = sls.get(packet.obj.get("target_listener").getAsString());
					if(listener != null){
						listener.process(packet, new Object[]{ctx.getServerHandler().player});
					}
				}
			});
			return null;
		}
	}
	
	public static class Client implements IMessageHandler<PacketJsonObject, IMessage> {
		@Override
		public IMessage onMessage(final PacketJsonObject packet, final MessageContext ctx) {
			IThreadListener ls = Minecraft.getMinecraft();
			ls.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					if(!packet.obj.has("target_listener")){
						Print.log("[FCL] Received JSON Packet, but had no target listener, ignoring!");
						Print.log("[OBJ] " + packet.obj.toString());
						return;
					}
					IPacketListener<PacketJsonObject> listener = cls.get(packet.obj.get("target_listener").getAsString());
					if(listener != null){
						listener.process(packet, new Object[]{Minecraft.getMinecraft().player});
					}
				}
			});
			return null;
		}
	}
	
	public static void addListener(Side side, IPacketListener<PacketJsonObject> listener){
		if(side.isClient()){
			cls.put(listener.getId(), listener);
		}
		else{
			sls.put(listener.getId(), listener);
		}
	}
	
}