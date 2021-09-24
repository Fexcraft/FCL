package net.fexcraft.lib.mc.gui;

import net.fexcraft.lib.mc.capabilities.FCLCapabilities;
import net.fexcraft.lib.mc.network.PacketListener;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

public class ClientReceiver implements PacketListener<PacketNBTTagCompound> {

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
	        case "paintable":{
	        	String type = packet.nbt.getString("type");
	        	switch(type){
					case "entity":{
						int id = packet.nbt.getInteger("id");
						Entity entity = player.world.getEntityByID(id); if(entity == null) return;
						entity.getCapability(FCLCapabilities.PAINTABLE, null).readNBT(null, null, packet.nbt);
						break;
					}
					case "tileentity":{
						BlockPos pos = BlockPos.fromLong(packet.nbt.getLong("pos"));
						TileEntity tile = player.world.getTileEntity(pos); if(tile == null) return;
						tile.getCapability(FCLCapabilities.PAINTABLE, null).readNBT(null, null, packet.nbt);
					}
					default: break;
				}
	        	return;
	        }
		}
	}
	
}