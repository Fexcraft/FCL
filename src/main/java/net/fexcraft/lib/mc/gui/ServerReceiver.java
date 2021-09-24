package net.fexcraft.lib.mc.gui;

import net.fexcraft.lib.mc.FCL;
import net.fexcraft.lib.mc.network.PacketListener;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;

public class ServerReceiver implements PacketListener<PacketNBTTagCompound> {
	
	public static ServerReceiver INSTANCE;
	
	public ServerReceiver(){ INSTANCE = this; }

	@Override
	public String getId(){
		return "fcl_gui";
	}

	@Override
	public void process(PacketNBTTagCompound packet, Object[] objs){
		if(!packet.nbt.hasKey("task")) return;
		EntityPlayerMP player = (EntityPlayerMP)objs[0];
		switch(packet.nbt.getString("task")){
			case "open_gui":{
                int gui = packet.nbt.getInteger("gui");
                int[] args = packet.nbt.hasKey("args") ? packet.nbt.getIntArray("args") : new int[3];
                player.openGui(FCL.getInstance(), gui, player.world, args[0], args[1], args[2]);
                return;
			}
            case "packet_gui":{
                ((GenericContainer)player.openContainer).packet(Side.SERVER, packet.nbt, player);
            	return;
            }
            //
		}
	}
	
}