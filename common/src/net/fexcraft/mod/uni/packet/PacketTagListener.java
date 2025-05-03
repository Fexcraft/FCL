package net.fexcraft.mod.uni.packet;

import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * Originally in FVTM
 *
 * @author Ferdinand Calo' (FEX___96)
 */
@FunctionalInterface
public interface PacketTagListener {

	public void handle(TagCW packet, EntityW player);

}
