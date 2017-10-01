package net.fexcraft.mod.lib.network.packet;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.fexcraft.mod.lib.api.network.IPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketItemStackUpdate extends Packet implements IPacket, IMessage{
	
	public String player;
	public NBTTagCompound nbt;
	
	public PacketItemStackUpdate(){}
	
	public PacketItemStackUpdate(EntityPlayer player, NBTTagCompound nbt){
		this.player = player.getName();
		this.nbt = nbt;
	}

	@Override
	public void toBytes(ByteBuf bbuf){
		PacketBuffer buf = new PacketBuffer(bbuf);
		buf.writeString(player);
		buf.writeCompoundTag(nbt);
	}

	@Override
	public void fromBytes(ByteBuf bbuf){
		PacketBuffer buf = new PacketBuffer(bbuf);
		player = buf.readString(16);
		try {
			nbt = buf.readCompoundTag();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}