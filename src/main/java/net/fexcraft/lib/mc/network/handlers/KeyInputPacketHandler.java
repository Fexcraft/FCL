package net.fexcraft.lib.mc.network.handlers;

import java.util.HashSet;

import net.fexcraft.lib.mc.api.packet.IPacketListener;
import net.fexcraft.lib.mc.network.packet.PacketKeyInput;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KeyInputPacketHandler implements IMessageHandler<PacketKeyInput, IMessage>{
	
	private static HashSet<IPacketListener<PacketKeyInput>> lsts = new HashSet<IPacketListener<PacketKeyInput>>();
	
	@Override
	public IMessage onMessage(final PacketKeyInput packet, final MessageContext ctx) {
		IThreadListener ls = FMLCommonHandler.instance().getMinecraftServerInstance();
		ls.addScheduledTask(new Runnable(){
			@Override
			public void run(){
				String name = ctx.getServerHandler().player.getName();
				/*switch(packet.target){
					case "entity_ridden":
						if(e.getRidingEntity() != null && e.getRidingEntity() instanceof IPacketReceiver){
							((IPacketReceiver)e.getRidingEntity()).processServerPacket(packet);
						}
						break;
					case "player":
						break;
					default:
						Print.log("Key Input Packet received, but no target specified!");
						Print.chat(e, "PACKET ERROR, read console.");
						break;
				}*/
				for(IPacketListener<PacketKeyInput> pktls : lsts){
					if(pktls.getId().equals(packet.target)){
						pktls.process(packet, new Object[]{name});
					}
				}
			}
			
		});
		return null;
	}

	public static void addListener(IPacketListener<PacketKeyInput> listener) {
		lsts.add(listener);
	}
	
}