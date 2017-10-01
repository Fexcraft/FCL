package net.fexcraft.mod.lib.util.common;

import net.fexcraft.mod.lib.api.tileentity.Cable;
import net.fexcraft.mod.lib.network.PacketHandler;
import net.fexcraft.mod.lib.network.packet.PacketEntityUpdate;
import net.fexcraft.mod.lib.network.packet.PacketTileEntityUpdate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ApiUtil{
	
	private static final int range = 32;
	
	public static void sendTileEntityUpdatePacket(World world, BlockPos pos, NBTTagCompound nbt){
		PacketHandler.getInstance().sendToAllAround(new PacketTileEntityUpdate(world.provider.getDimension(), pos, nbt), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range));
	}
	
	public static void sendTileEntityUpdatePacket(World world, BlockPos pos, NBTTagCompound nbt, int range){
		PacketHandler.getInstance().sendToAllAround(new PacketTileEntityUpdate(world.provider.getDimension(), pos, nbt), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range));
	}
	
	public static void sendTileEntityUpdatePacket(int dim, BlockPos pos, NBTTagCompound nbt){
		PacketHandler.getInstance().sendToAllAround(new PacketTileEntityUpdate(dim, pos, nbt), new TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), 32));
	}
	
	public static void sendTileEntityUpdatePacket(int dim, BlockPos pos, NBTTagCompound nbt, int range){
		PacketHandler.getInstance().sendToAllAround(new PacketTileEntityUpdate(dim, pos, nbt), new TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), range));
	}

	public static void sendTileEntityUpdatePacket(TileEntity entity, NBTTagCompound nbt, int i) {
		BlockPos pos = entity.getPos();
		PacketHandler.getInstance().sendToAllAround(new PacketTileEntityUpdate(entity.getWorld().provider.getDimension(), pos, nbt), new TargetPoint(entity.getWorld().provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), i));
	}
	
	public static void sendEntityUpdatePacketToClient(Entity ent, EntityPlayerMP player, NBTTagCompound nbt){
		PacketHandler.getInstance().sendTo(new PacketEntityUpdate(ent, nbt), player);
	}
	
	public static void sendEntityUpdatePacketToAll(Entity ent, NBTTagCompound nbt){
		PacketHandler.getInstance().sendToAll(new PacketEntityUpdate(ent, nbt));
	}
	
	public static void sendEntityUpdatePacketToAllAround(Entity ent, NBTTagCompound nbt){
		PacketHandler.getInstance().sendToAllAround(new PacketEntityUpdate(ent, nbt), new TargetPoint(ent.dimension, ent.posX, ent.posY, ent.posZ, 256));
	}
	
	public static void sendEntityUpdatePacketToServer(Entity ent, NBTTagCompound nbt){
		PacketHandler.getInstance().sendToServer(new PacketEntityUpdate(ent, nbt));
	}
	
	public static void tryTransfer(Cable sender, World w, BlockPos pos, EnumFacing side){
		if(sender.fpu_get(side) > sender.fpu_min(side) && sender.fpu_get(side) > sender.fpu_min_transfer_speed(side)){
			TileEntity rte = w.getTileEntity(getPosFromFacing(side, pos));
			if(rte != null && rte instanceof Cable){
				Cable receiver = (Cable)rte;
				if(receiver.fpu_isInput(side.getOpposite())){
					if(receiver.fpu_get(side.getOpposite()) >= receiver.fpu_max(side.getOpposite())){
						return;
					}
					int t = getTransferSpeed(sender, side, receiver);
					if(t <= 0){
						return;
					}
					boolean b = hasSpaceForFullTransfer(side.getOpposite(), receiver, t);
					if(b){
						sender.fpu_subtract(side, t);
						receiver.fpu_add(side, t);
					}
					if(!b){
						int fs = getFreeSpace(side.getOpposite(), receiver);
						if((t - fs) == 0){
							return;
						}
						sender.fpu_subtract(side, fs);
						receiver.fpu_add(side, fs);
					}
				}
			}
		}
	}
	
	public static BlockPos getPosFromFacing(EnumFacing facing, BlockPos pos){
		switch(facing){
			case NORTH: return new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1);
			case SOUTH: return new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1);
			case WEST:  return new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ());
			case EAST:  return new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ());
			default: return pos;
		}
	}
	
	public static int getTransferSpeed(Cable sender, EnumFacing ef, Cable receiver){
		int rs = getTransferSpeed(sender, ef);
		int r = receiver.fpu_transfer_speed(ef.getOpposite());
		if(rs > r){
			rs = r;
		}
		if(rs < receiver.fpu_min_transfer_speed(ef.getOpposite())){
			rs = 0;
		}
		//Print.spam(1, sender.getContainerName() + "  --->  " + rs + "  --->  " + receiver.getContainerName());
		return rs;
	}

	/*public static void transferPower(CableEntity sender, EnumFacing ef, CableEntity target){
		if(isFull(target)){
			return;
		}
		int t = getTransferSpeed(sender, ef);
		if(t == 0){
			return;
		}
		boolean b = hasSpaceForFullTransfer(target, t);
		if(b){
			sender.fpu_subtract(t);
			target.fpu_add(t);
		}
		if(!b){
			int fs = getFreeSpace(target);
			if((t - fs) == 0){
				return;
			}
			sender.fpu_set(sender.fpu_get() - fs);
			target.fpu_set(target.fpu_get() + fs);
		}
	}*/
	
	public static int getTransferSpeed(Cable cable, EnumFacing ef){
		int r = cable.fpu_transfer_speed(ef);
		if(r > cable.fpu_get(ef)){
			r = cable.fpu_get(ef);
		}
		r = r - cable.fpu_resistivity(ef);
		if(r < 1){
			return 0;
		}
		return r;
	}
	
	public static boolean hasSpaceForFullTransfer(EnumFacing ef, Cable cable, int i){
		int t = cable.fpu_get(ef) + i;
		if(t > cable.fpu_max(ef)){
			return false;
		}
		else return true;
	}
	
	public static int getFreeSpace(EnumFacing ef, Cable cable){
		int r = cable.fpu_max(ef) - cable.fpu_get(ef);
		return r;
	}
	
	public static boolean isFull(EnumFacing ef, Cable cable){
		if(cable.fpu_get(ef) >= cable.fpu_max(ef)){
			return true;
		}
		else return false;
	}
	
}