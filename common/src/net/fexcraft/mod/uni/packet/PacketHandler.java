package net.fexcraft.mod.uni.packet;

import net.fexcraft.mod.uni.world.EntityW;

/**
 * Originally in FVTM
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public interface PacketHandler<PACKET extends PacketBase> {

	public Runnable handleServer(PACKET packet, EntityW player);

	public Runnable handleClient(PACKET packet, EntityW player);

}
