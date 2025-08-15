package net.fexcraft.mod.fcl.util;

import net.minecraft.nbt.CompoundTag;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface UIPacketReceiver {

	public void onPacket(CompoundTag com, boolean client);

}
