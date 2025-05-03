package net.fexcraft.lib.mc.network;

import net.fexcraft.lib.mc.network.handlers.*;
import net.fexcraft.mod.uni.impl.PacketFileHandler;
import net.fexcraft.mod.uni.impl.PacketFileHandler.I12_PacketImg;
import net.fexcraft.lib.mc.network.packet.PacketEntityUpdate;
import net.fexcraft.lib.mc.network.packet.PacketTileEntityUpdate;
import net.fexcraft.mod.uni.impl.PacketTagHandler;
import net.fexcraft.mod.uni.impl.PacketTagHandler.I12_PacketTag;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	private static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel("fcl");
	
	public static void init(){
		instance.registerMessage(PacketTagHandler.Server.class, I12_PacketTag.class, 0, Side.SERVER);
		instance.registerMessage(PacketTagHandler.Client.class, I12_PacketTag.class, 1, Side.CLIENT);
		instance.registerMessage(PacketFileHandler.Client.class, I12_PacketImg.class, 2, Side.CLIENT);
		instance.registerMessage(PacketFileHandler.Server.class, I12_PacketImg.class, 3, Side.SERVER);
		instance.registerMessage(TileEntityUpdatePacketHandler.Client.class,  PacketTileEntityUpdate.class, 4, Side.CLIENT);
		instance.registerMessage(TileEntityUpdatePacketHandler.Server.class,  PacketTileEntityUpdate.class, 5, Side.SERVER);
		instance.registerMessage(EntityUpdatePacketHandler.Server.class, PacketEntityUpdate.class, 6, Side.SERVER);
		instance.registerMessage(EntityUpdatePacketHandler.Client.class, PacketEntityUpdate.class, 7, Side.CLIENT);
	}
	
	public static SimpleNetworkWrapper getInstance(){
		return instance;
	}
	
}