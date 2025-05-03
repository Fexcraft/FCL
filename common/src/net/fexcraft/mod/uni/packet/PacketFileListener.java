package net.fexcraft.mod.uni.packet;

import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@FunctionalInterface
public interface PacketFileListener {

	public void handle(String loc, byte[] packet, EntityW player);

}
