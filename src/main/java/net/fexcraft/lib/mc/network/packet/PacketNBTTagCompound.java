package net.fexcraft.lib.mc.network.packet;

import io.netty.buffer.ByteBuf;
import net.fexcraft.lib.mc.api.IPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketNBTTagCompound implements IPacket, IMessage {
	
	public NBTTagCompound nbt;
	
	public PacketNBTTagCompound(){}
	
	public PacketNBTTagCompound(NBTTagCompound obj){
		this.nbt = obj;
	}

	@Override
	public void toBytes(ByteBuf buf){
		ByteBufUtils.writeTag(buf, nbt);
	}

	@Override
	public void fromBytes(ByteBuf buf){
		nbt = ByteBufUtils.readTag(buf);
	}
	
}