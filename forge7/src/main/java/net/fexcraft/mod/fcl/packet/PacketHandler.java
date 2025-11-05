package net.fexcraft.mod.fcl.packet;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
	
	private static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel("fcl");
	
	public static void init(){
		instance.registerMessage(PacketTagHandler.Server.class, PacketTagHandler.I12_PacketTag.class, 0, Side.SERVER);
		instance.registerMessage(PacketTagHandler.Client.class, PacketTagHandler.I12_PacketTag.class, 1, Side.CLIENT);
		instance.registerMessage(PacketFileHandler.Client.class, PacketFileHandler.I12_PacketImg.class, 2, Side.CLIENT);
		instance.registerMessage(PacketFileHandler.Server.class, PacketFileHandler.I12_PacketImg.class, 3, Side.SERVER);
	}
	
	public static SimpleNetworkWrapper getInstance(){
		return instance;
	}
	
}