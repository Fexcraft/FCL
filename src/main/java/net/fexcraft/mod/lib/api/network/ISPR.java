package net.fexcraft.mod.lib.api.network;

import net.fexcraft.mod.lib.network.packet.PacketItemStackUpdate;
import net.minecraft.item.ItemStack;

public interface ISPR extends IPacket {

	public void processServerPacket(PacketItemStackUpdate packet, ItemStack stack);

	public void processClientPacket(PacketItemStackUpdate packet, ItemStack stack);
	
}