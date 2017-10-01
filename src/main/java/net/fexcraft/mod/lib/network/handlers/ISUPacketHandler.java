package net.fexcraft.mod.lib.network.handlers;
import java.util.HashSet;

import net.fexcraft.mod.lib.api.network.IPacketListener;
import net.fexcraft.mod.lib.api.network.ISPR;
import net.fexcraft.mod.lib.network.packet.PacketItemStackUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ISUPacketHandler{
	
	private static HashSet<IPacketListener<PacketItemStackUpdate>> sls = new HashSet<IPacketListener<PacketItemStackUpdate>>();
	private static HashSet<IPacketListener<PacketItemStackUpdate>> cls = new HashSet<IPacketListener<PacketItemStackUpdate>>();
	
	public static class Server implements IMessageHandler<PacketItemStackUpdate, IMessage>{
		@Override
		public IMessage onMessage(final PacketItemStackUpdate packet, MessageContext ctx) {
			IThreadListener ls = FMLCommonHandler.instance().getMinecraftServerInstance();
			ls.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					ItemStack stack = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getPlayerList().getPlayerByUsername(packet.player).getHeldItemMainhand();
					if(stack != null && stack.getItem() instanceof ISPR){
						((ISPR)stack.getItem()).processServerPacket(packet, stack);
					}
					if(packet.nbt.hasKey("target_listener")){
						for(IPacketListener<PacketItemStackUpdate> pktl : sls){
							if(pktl.getId().equals(packet.nbt.getString("target_listener"))){
								pktl.process(packet, new Object[]{stack});
							}
						}
					}
				}
			});
			return null;
		}
	}
	
	public static class Client implements IMessageHandler<PacketItemStackUpdate, IMessage>{
		@Override
		public IMessage onMessage(final PacketItemStackUpdate packet, MessageContext ctx) {
			IThreadListener ls = Minecraft.getMinecraft();
			ls.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					ItemStack stack = Minecraft.getMinecraft().player.getHeldItemMainhand();
					if(stack != null && stack.getItem() instanceof ISPR){
						((ISPR)stack.getItem()).processClientPacket(packet, stack);
					}
					if(packet.nbt.hasKey("target_listener")){
						for(IPacketListener<PacketItemStackUpdate> pktl : cls){
							if(pktl.getId().equals(packet.nbt.getString("target_listener"))){
								pktl.process(packet, new Object[]{stack});
							}
						}
					}
				}
			});
			return null;
		}
	}

	public static void addListener(Side side, IPacketListener<PacketItemStackUpdate> listener) {
		if(side.isClient()){
			cls.add(listener);
		}
		else{
			sls.add(listener);
		}
	}
	
}