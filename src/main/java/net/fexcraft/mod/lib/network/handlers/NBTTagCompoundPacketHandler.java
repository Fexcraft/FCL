package net.fexcraft.mod.lib.network.handlers;

import java.util.HashMap;
import net.fexcraft.mod.lib.api.network.IPacketListener;
import net.fexcraft.mod.lib.network.packet.PacketNBTTagCompound;
import net.fexcraft.mod.lib.util.common.Print;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class NBTTagCompoundPacketHandler {
	
	private static HashMap<String, IPacketListener> sls = new HashMap<String, IPacketListener>();
	private static HashMap<String, IPacketListener> cls = new HashMap<String, IPacketListener>();
	
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
					IPacketListener listener = sls.get(packet.nbt.getString("target_listener"));
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
					IPacketListener listener = cls.get(packet.nbt.getString("target_listener"));
					if(listener != null){
						listener.process(packet, new Object[]{Minecraft.getMinecraft().player});
					}
				}
			});
			return null;
		}
	}
	
	public static void addListener(Side side, IPacketListener listener) {
		if(side.isClient()){
			cls.put(listener.getId(), listener);
		}
		else{
			sls.put(listener.getId(), listener);
		}
	}
	
}