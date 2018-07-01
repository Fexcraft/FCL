package net.fexcraft.mod.lib.adjgui;

import net.fexcraft.mod.lib.api.network.IPacketListener;
import net.fexcraft.mod.lib.network.packet.PacketNBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public class GuiPacketListener implements IPacketListener<PacketNBTTagCompound> {
	
	private Side side;
	
	public GuiPacketListener(Side side){
		this.side = side;
	}

	@Override
	public String getId(){
		return "fcl:adjustable_gui";
	}

	@Override
	public void process(PacketNBTTagCompound packet, Object[] objs){
		EntityPlayer player = (EntityPlayer)objs[0];
		if(player.openContainer instanceof SynchronizedContainer){
			((SynchronizedContainer)player.openContainer).onDataPacket0(player, packet.nbt, side);
		}
	}
	
}