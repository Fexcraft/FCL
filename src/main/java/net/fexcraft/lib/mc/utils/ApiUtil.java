package net.fexcraft.lib.mc.utils;

import net.fexcraft.lib.common.math.RGB;
import net.minecraft.nbt.CompoundTag;

@Deprecated//till updated, if possible
public class ApiUtil{
	
	//private static final int range = 64;
	
	/*public static void sendTileEntityUpdatePacket(World world, BlockPos pos, CompoundTag nbt){
		PacketHandler.getInstance().sendToAllAround(new PacketTileEntityUpdate(world.provider.getDimension(), pos, nbt), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range));
	}
	
	public static void sendTileEntityUpdatePacket(World world, BlockPos pos, CompoundTag nbt, int range){
		PacketHandler.getInstance().sendToAllAround(new PacketTileEntityUpdate(world.provider.getDimension(), pos, nbt), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range));
	}
	
	public static void sendTileEntityUpdatePacket(int dim, BlockPos pos, CompoundTag nbt){
		PacketHandler.getInstance().sendToAllAround(new PacketTileEntityUpdate(dim, pos, nbt), new TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), 32));
	}
	
	public static void sendTileEntityUpdatePacket(int dim, BlockPos pos, CompoundTag nbt, int range){
		PacketHandler.getInstance().sendToAllAround(new PacketTileEntityUpdate(dim, pos, nbt), new TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), range));
	}

	public static void sendTileEntityUpdatePacket(TileEntity entity, CompoundTag nbt, int i){
		BlockPos pos = entity.getPos();
		PacketHandler.getInstance().sendToAllAround(new PacketTileEntityUpdate(entity.getWorld().provider.getDimension(), pos, nbt), new TargetPoint(entity.getWorld().provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), i));
	}
	
	public static void sendEntityUpdatePacketToClient(Entity ent, EntityPlayerMP player, CompoundTag nbt){
		PacketHandler.getInstance().sendTo(new PacketEntityUpdate(ent, nbt), player);
	}
	
	public static void sendEntityUpdatePacketToAll(Entity ent, CompoundTag nbt){
		PacketHandler.getInstance().sendToAll(new PacketEntityUpdate(ent, nbt));
	}
	
	public static void sendEntityUpdatePacketToAllAround(Entity ent, CompoundTag nbt){
		PacketHandler.getInstance().sendToAllAround(new PacketEntityUpdate(ent, nbt), new TargetPoint(ent.dimension, ent.posX, ent.posY, ent.posZ, 256));
	}
	
	public static void sendEntityUpdatePacketToServer(Entity ent, CompoundTag nbt){
		PacketHandler.getInstance().sendToServer(new PacketEntityUpdate(ent, nbt));
	}*/

	/**
	 * @param a additional name data to append into the nbt tag key
	 */
	public static final CompoundTag writeToNBT(RGB rgb, CompoundTag tag, String a){
		try{
			String s = a == null ? "" : "_" + a;
			tag.putInt("RGB" + s, rgb.packed);
			tag.putFloat("RGBA" + s, rgb.alpha);
			return tag;
		}
		catch(Exception e){
			e.printStackTrace();
			return tag;
		}
	}
	
	/**
	 * @param a additional name data to retrieve the right nbt key.
	 */
	public static final void readFromNBT(RGB rgb, CompoundTag tag, String a){
		try{
			String s = a == null ? "" : "_" + a;
			if(tag.containsKey("RGB_Red" + s)){
				byte red = tag.getByte("RGB_Red" + s);
				byte green = tag.getByte("RGB_Green" + s);
				byte blue = tag.getByte("RGB_Blue" + s);
				rgb.packed = new RGB(red, green, blue).packed;
			}
			else{
				rgb.packed = tag.getInt("RGB" + s);
				rgb.alpha = tag.getFloat("RGBA" + s);
			}
		}
		catch(Exception e){
			e.printStackTrace(); rgb.packed = RGB.WHITE.packed;
		}
	}
	
}