package net.fexcraft.lib.mc.network.packet;

import io.netty.buffer.ByteBuf;
import net.fexcraft.lib.mc.api.packet.IPacket;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEntityUpdate extends Packet implements IPacket, IMessage {
	
	public NBTTagCompound nbt;
	public int id, dim;
	
	public PacketEntityUpdate(){}
	
	public PacketEntityUpdate(int dim, int id, NBTTagCompound nbt){
		this.id = id;
		this.nbt = nbt;
		this.dim = dim;
	}
	
	public PacketEntityUpdate(Entity ent, NBTTagCompound nbt){
		this.id = ent.getEntityId();
		this.nbt = nbt;
		this.dim = ent.dimension;
	}

	@Override
	public void toBytes(ByteBuf bbuf){
		PacketBuffer buf = new PacketBuffer(bbuf);
		buf.writeInt(dim);
		buf.writeInt(id);
		buf.writeCompoundTag(nbt);
	}

	@Override
	public void fromBytes(ByteBuf bbuf){
		PacketBuffer buf = new PacketBuffer(bbuf);
		dim = buf.readInt();
		id = buf.readInt();
		try{
			nbt = buf.readCompoundTag();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}