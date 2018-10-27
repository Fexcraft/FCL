package net.fexcraft.lib.mc.network.handlers;

import java.util.HashMap;

import net.fexcraft.lib.mc.api.packet.IPacketListener;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class NBTTagCompoundPacketHandler {
	
	private static HashMap<String, IPacketListener<PacketNBTTagCompound>> sls = new HashMap<String, IPacketListener<PacketNBTTagCompound>>();
	private static HashMap<String, IPacketListener<PacketNBTTagCompound>> cls = new HashMap<String, IPacketListener<PacketNBTTagCompound>>();
	
	public static class Server implements IMessageHandler<PacketNBTTagCompound, IMessage> {
		@Override
		public IMessage onMessage(final PacketNBTTagCompound packet, final MessageContext ctx) {
			IThreadListener ls = FMLCommonHandler.instance().getMinecraftServerInstance();
			ls.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					if(!packet.nbt.hasKey("target_listener")){
						Print.log("[FCL] Received NBT Packet, but had no target listener, ignoring!");
						Print.log("[NBT] " + packet.nbt.toString());
						return;
					}
					IPacketListener<PacketNBTTagCompound> listener = sls.get(packet.nbt.getString("target_listener"));
					if(listener != null){
						listener.process(packet, new Object[]{ctx.getServerHandler().player});
					}
				}
			});
			return null;
		}
	}
	
	public static class Client implements IMessageHandler<PacketNBTTagCompound, IMessage> {
		@Override
		public IMessage onMessage(final PacketNBTTagCompound packet, final MessageContext ctx) {
			IThreadListener ls = Minecraft.getMinecraft();
			ls.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					if(!packet.nbt.hasKey("target_listener")){
						Print.log("[FCL] Received NBT Packet, but had no target listener, ignoring!");
						Print.log("[NBT] " + packet.nbt.toString());
						return;
					}
					IPacketListener<PacketNBTTagCompound> listener = cls.get(packet.nbt.getString("target_listener"));
					if(listener != null){
						listener.process(packet, new Object[]{Minecraft.getMinecraft().player});
					}
				}
			});
			return null;
		}
	}
	
	public static void addListener(Side side, IPacketListener<PacketNBTTagCompound> listener) {
		if(side.isClient()){
			cls.put(listener.getId(), listener);
		}
		else{
			sls.put(listener.getId(), listener);
		}
	}
	
}