package net.fexcraft.lib.mc.utils;

import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.packet.PacketEntityUpdate;
import net.fexcraft.lib.mc.network.packet.PacketTileEntityUpdate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ApiUtil{
	
	private static final int range = 64;
	
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

	public static void sendTileEntityUpdatePacket(TileEntity entity, NBTTagCompound nbt, int i){
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

	/**
	 * @param a additional name data to append into the nbt tag key
	 */
	public static final NBTTagCompound writeToNBT(RGB rgb, NBTTagCompound tag, String a){
		try{
			String s = a == null ? "" : "_" + a;
			tag.setInteger("RGB" + s, rgb.packed);
			tag.setFloat("RGBA" + s, rgb.alpha);
			return tag;
		}
		catch(Exception e){
			e.printStackTrace();
			return tag;
		}
	}
	
	/**
	 * @param tag additional name data to retrieve the right nbt key.
	 */
	public static final void readFromNBT(RGB rgb, NBTTagCompound tag, String a){
		try{
			String s = a == null ? "" : "_" + a;
			if(tag.hasKey("RGB_Red" + s)){
				byte red = tag.getByte("RGB_Red" + s);
				byte green = tag.getByte("RGB_Green" + s);
				byte blue = tag.getByte("RGB_Blue" + s);
				rgb.packed = new RGB(red, green, blue).packed;
			}
			else{
				rgb.packed = tag.getInteger("RGB" + s);
				rgb.alpha = tag.getFloat("RGBA" + s);
			}
		}
		catch(Exception e){
			e.printStackTrace(); rgb.packed = RGB.WHITE.packed;
		}
	}
	
}