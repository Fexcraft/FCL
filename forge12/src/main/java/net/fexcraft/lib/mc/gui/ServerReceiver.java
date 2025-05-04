package net.fexcraft.lib.mc.gui;

import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.packet.PacketTagListener;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.EntityW;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;

public class ServerReceiver implements PacketTagListener {

	@Override
	public void handle(TagCW packet, EntityW entity){
		if(!packet.has("task")) return;
		EntityPlayerMP player = entity.local();
		switch(packet.getString("task")){
			case "open_gui":{
				int gui = packet.getInteger("gui");
				int[] args = packet.has("args") ? packet.getIntArray("args") : new int[3];
				player.openGui(FCL.getInstance(), gui, player.world, args[0], args[1], args[2]);
				return;
			}
			case "packet_gui":{
				((GenericContainer)player.openContainer).packet(Side.SERVER, packet.local(), player);
				return;
			}
		}
	}

}