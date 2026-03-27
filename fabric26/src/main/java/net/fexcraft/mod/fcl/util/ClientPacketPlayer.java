package net.fexcraft.mod.fcl.util;

import net.minecraft.world.entity.player.Player;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ClientPacketPlayer {

	public static Player get(){
		return net.minecraft.client.Minecraft.getInstance().player;
	}

}
