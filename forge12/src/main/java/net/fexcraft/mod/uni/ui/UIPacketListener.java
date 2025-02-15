package net.fexcraft.mod.uni.ui;

import net.fexcraft.lib.mc.api.packet.IPacketListener;
import net.fexcraft.lib.mc.api.packet.IPacketReceiver;
import net.fexcraft.lib.mc.network.handlers.NBTTagCompoundPacketHandler;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UIPacketListener {

	public static class Client implements IPacketListener<PacketNBTTagCompound> {

		@Override
		public String getId(){
			return "fcl:ui";
		}

		@Override
		public void process(PacketNBTTagCompound packet, Object[] objs){
			if(((EntityPlayer)objs[0]).openContainer instanceof UniCon == false){
				FCL.LOGGER.info("ERROR: Received packet for UniCon, but Container is of different type.");
				FCL.LOGGER.info("PACKET: " + packet.nbt);
				return;
			}
			((UniCon)((EntityPlayer)objs[0]).openContainer).container().packet(TagCW.wrap(packet.nbt), true);
		}

	}

	public static class Server implements IPacketListener<PacketNBTTagCompound> {

		@Override
		public String getId(){
			return "fcl:ui";
		}

		@Override
		public void process(PacketNBTTagCompound packet, Object[] objs){
			if(((EntityPlayer)objs[0]).openContainer instanceof UniCon == false){
				FCL.LOGGER.info("ERROR: Received packet for UniCon, but Container is of different type.");
				FCL.LOGGER.info("PACKET: " + packet.nbt);
				return;
			}
			((UniCon)((EntityPlayer)objs[0]).openContainer).container().packet(TagCW.wrap(packet.nbt), false);
		}

	}


	public static void register(){
		if(EnvInfo.CLIENT){
			NBTTagCompoundPacketHandler.addListener(Side.CLIENT, new UIPacketListener.Client());
		}
		NBTTagCompoundPacketHandler.addListener(Side.SERVER, new UIPacketListener.Server());
	}

}
