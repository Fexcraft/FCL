package net.fexcraft.lib.mc.network.packet;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.fexcraft.lib.mc.api.IPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketTileEntityUpdate extends Packet implements IPacket, IMessage {
	
	public BlockPos pos;
	public NBTTagCompound nbt;
	public int dim;
	
	public PacketTileEntityUpdate(){}
	
	public PacketTileEntityUpdate(int dim, BlockPos pos, NBTTagCompound nbt){
		this.pos = pos;
		this.nbt = nbt;
		this.dim = dim;
	}

	@Override
	public void toBytes(ByteBuf bbuf){
		PacketBuffer buf = new PacketBuffer(bbuf);
		buf.writeInt(dim);
		buf.writeBlockPos(pos);
		buf.writeCompoundTag(nbt);
	}

	@Override
	public void fromBytes(ByteBuf bbuf){
		PacketBuffer buf = new PacketBuffer(bbuf);
		dim = buf.readInt();
		pos = buf.readBlockPos();
		try {
			nbt = buf.readCompoundTag();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}