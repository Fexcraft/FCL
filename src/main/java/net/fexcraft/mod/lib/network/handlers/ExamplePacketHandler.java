package net.fexcraft.mod.lib.network.handlers;

import net.fexcraft.mod.lib.network.packet.Packet;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ExamplePacketHandler implements IMessageHandler<Packet, IMessage>{
	
	@Override
	public IMessage onMessage(Packet pkt, MessageContext ctx) {
		//int smg = pkt.data;
		return null;
	}
}