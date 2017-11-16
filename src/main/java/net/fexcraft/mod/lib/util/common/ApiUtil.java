package net.fexcraft.mod.lib.util.common;

import net.fexcraft.mod.lib.network.PacketHandler;
import net.fexcraft.mod.lib.network.packet.PacketEntityUpdate;
import net.fexcraft.mod.lib.network.packet.PacketTileEntityUpdate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
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
	
	public static BlockPos getPosFromFacing(EnumFacing facing, BlockPos pos){
		switch(facing){
			case NORTH: return new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1);
			case SOUTH: return new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1);
			case WEST:  return new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ());
			case EAST:  return new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ());
			default: return pos;
		}
	}
	
	/** For compatibility with the removed EnumColor */
	public static final EnumDyeColor getDyeColorFromString(String s){
		return getDyeColorFromString(s, EnumDyeColor.WHITE);
	}
	
	public static final EnumDyeColor getDyeColorFromString(String s, EnumDyeColor def){
		switch(s){
			case "white":      return EnumDyeColor.WHITE;
			case "orange":     return EnumDyeColor.ORANGE;
			case "magenta":    return EnumDyeColor.MAGENTA;
			case "light_blue": return EnumDyeColor.LIGHT_BLUE;
			case "yellow":     return EnumDyeColor.YELLOW;
			case "lime":       return EnumDyeColor.LIME;
			case "pink":       return EnumDyeColor.PINK;
			case "gray":       return EnumDyeColor.GRAY;
			case "silver":
			case "light_gray": return EnumDyeColor.SILVER;
			case "cyan":       return EnumDyeColor.CYAN;
			case "purple":     return EnumDyeColor.PURPLE;
			case "blue":       return EnumDyeColor.BLUE;
			case "brown":      return EnumDyeColor.BROWN;
			case "green":      return EnumDyeColor.GREEN;
			case "red":        return EnumDyeColor.RED;
			case "black":      return EnumDyeColor.BLACK;
		}
		return def;
	}
	
}