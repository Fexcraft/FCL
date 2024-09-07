package net.fexcraft.lib.mc.gui;

import net.fexcraft.lib.mc.api.packet.IPacketListener;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public class ClientReceiver implements IPacketListener<PacketNBTTagCompound> {

	@Override
	public String getId(){
		return "fcl_gui";
	}

	@Override
	public void process(PacketNBTTagCompound packet, Object[] objs){
		if(!packet.nbt.hasKey("task")) return;
        EntityPlayer player = (EntityPlayer)objs[0];
		switch(packet.nbt.getString("task")){
	        case "packet_gui":{
	            ((GenericContainer)player.openContainer).packet(Side.CLIENT, packet.nbt, player);
	        	return;
	        }
		}
	}
	
}