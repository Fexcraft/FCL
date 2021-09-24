package net.fexcraft.lib.mc;

import net.fabricmc.api.ClientModInitializer;
import net.fexcraft.lib.mc.network.PacketHandler;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 * 
 */
public class FCL_C implements ClientModInitializer {
	
	@Override
	public void onInitializeClient(){
		System.out.println("[FCL] Initializing (CLIENT).");
		FCL.CLIENT = true;
		PacketHandler.regClientPackets();
	}
	
}