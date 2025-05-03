package net.fexcraft.lib.mc.gui;

import net.fexcraft.lib.mc.api.packet.IPacketListener;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.fexcraft.mod.uni.packet.PacketTagListener;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.EntityW;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public class ClientReceiver implements PacketTagListener {

	@Override
	public void handle(TagCW packet, EntityW entity){
		if(!packet.has("task")) return;
		EntityPlayer player = entity.local();
		switch(packet.getString("task")){
			case "packet_gui":{
				((GenericContainer)player.openContainer).packet(Side.CLIENT, packet.local(), player);
				return;
			}
		}
	}

}