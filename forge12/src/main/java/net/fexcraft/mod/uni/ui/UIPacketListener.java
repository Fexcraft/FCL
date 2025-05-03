package net.fexcraft.mod.uni.ui;

import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.packet.PacketTagListener;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.EntityW;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UIPacketListener {

	public static class Client implements PacketTagListener {

		@Override
		public void handle(TagCW packet, EntityW entity){
			EntityPlayer player = entity.local();
			if(player.openContainer instanceof UniCon == false){
				FCL.LOGGER.info("ERROR: Received packet for UniCon, but Container is of different type.");
				FCL.LOGGER.info("PACKET: " + packet);
				return;
			}
			((UniCon)player.openContainer).container().packet(packet, true);
		}

	}

	public static class Server implements PacketTagListener {

		@Override
		public void handle(TagCW packet, EntityW entity){
			EntityPlayer player = entity.local();
			if(player.openContainer instanceof UniCon == false){
				FCL.LOGGER.info("ERROR: Received packet for UniCon, but Container is of different type.");
				FCL.LOGGER.info("PACKET: " + packet);
				return;
			}
			((UniCon)player.openContainer).container().packet(packet, false);
		}

	}

}
