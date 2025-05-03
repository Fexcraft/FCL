package net.fexcraft.lib.mc.network;

import net.fexcraft.lib.mc.api.packet.IPacketListener;
import net.fexcraft.lib.mc.network.handlers.*;
import net.fexcraft.mod.uni.impl.PacketFileHandler;
import net.fexcraft.mod.uni.impl.PacketFileHandler.I12_PacketImg;
import net.fexcraft.lib.mc.network.packet.Packet;
import net.fexcraft.lib.mc.network.packet.PacketEntityUpdate;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.fexcraft.lib.mc.network.packet.PacketTileEntityUpdate;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	private static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel("fcl");
	
	public static void init(){
		Print.log("Initialising Packet Handler.");
		instance.registerMessage(ExamplePacketHandler.class,                  Packet.class,                  0, Side.SERVER);
		instance.registerMessage(TileEntityUpdatePacketHandler.Client.class,  PacketTileEntityUpdate.class,  1, Side.CLIENT);
		instance.registerMessage(TileEntityUpdatePacketHandler.Server.class,  PacketTileEntityUpdate.class,  2, Side.SERVER);
		instance.registerMessage(NBTTagCompoundPacketHandler.Server.class,    PacketNBTTagCompound.class,    3, Side.SERVER);
		instance.registerMessage(NBTTagCompoundPacketHandler.Client.class,    PacketNBTTagCompound.class,    4, Side.CLIENT);
		instance.registerMessage(EntityUpdatePacketHandler.Server.class,      PacketEntityUpdate.class,      5, Side.SERVER);
		instance.registerMessage(EntityUpdatePacketHandler.Client.class,      PacketEntityUpdate.class,      6, Side.CLIENT);
		instance.registerMessage(PacketFileHandler.Client.class, I12_PacketImg.class, 12, Side.CLIENT);
		instance.registerMessage(PacketFileHandler.Server.class, I12_PacketImg.class, 13, Side.SERVER);
		Print.log("Done initialising Packet Handler.");
	}
	
	public static SimpleNetworkWrapper getInstance(){
		return instance;
	}
	
	public static enum PacketHandlerType {
		NBT;
	}
	
	@SuppressWarnings("unchecked")
	public static void registerListener(PacketHandlerType type, Side side, IPacketListener<?> listener){
		switch(type){
			case NBT:{
				NBTTagCompoundPacketHandler.addListener(side, (IPacketListener<PacketNBTTagCompound>) listener);
				break;
			}
			default: break;
		}
		Print.log("[FCL] Registered new PacketTagListener with ID '" + listener.getId() + "' and type " + type.name() + " for Side:" + (side.isClient() ? "Client" : "Server") + ".");
	}
	
}