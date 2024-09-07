package net.fexcraft.lib.mc.network;

import net.fexcraft.lib.mc.api.packet.IPacketListener;
import net.fexcraft.lib.mc.network.handlers.EntityUpdatePacketHandler;
import net.fexcraft.lib.mc.network.handlers.ExamplePacketHandler;
import net.fexcraft.lib.mc.network.handlers.JsonObjectPacketHandler;
import net.fexcraft.lib.mc.network.handlers.KeyInputPacketHandler;
import net.fexcraft.lib.mc.network.handlers.NBTTagCompoundPacketHandler;
import net.fexcraft.lib.mc.network.handlers.TileEntityUpdatePacketHandler;
import net.fexcraft.lib.mc.network.packet.Packet;
import net.fexcraft.lib.mc.network.packet.PacketEntityUpdate;
import net.fexcraft.lib.mc.network.packet.PacketJsonObject;
import net.fexcraft.lib.mc.network.packet.PacketKeyInput;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.fexcraft.lib.mc.network.packet.PacketTileEntityUpdate;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	private static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel("frsm");
	public static int nextpacketid = 12;
	
	public static void init(){
		Print.log("Initialising Packet Handler.");
		instance.registerMessage(ExamplePacketHandler.class,                  Packet.class,                  0, Side.SERVER);
		instance.registerMessage(TileEntityUpdatePacketHandler.Client.class,  PacketTileEntityUpdate.class,  1, Side.CLIENT);
		instance.registerMessage(TileEntityUpdatePacketHandler.Server.class,  PacketTileEntityUpdate.class,  2, Side.SERVER);
		instance.registerMessage(KeyInputPacketHandler.class,                 PacketKeyInput.class,          3, Side.SERVER);
		//instance.registerMessage(ISUPacketHandler.Server.class,               PacketItemStackUpdate.class,   4, Side.SERVER);
		//instance.registerMessage(ISUPacketHandler.Client.class,               PacketItemStackUpdate.class,   5, Side.CLIENT);
		instance.registerMessage(JsonObjectPacketHandler.Server.class,        PacketJsonObject.class,        6, Side.SERVER);
		instance.registerMessage(JsonObjectPacketHandler.Client.class,        PacketJsonObject.class,        7, Side.CLIENT);
		instance.registerMessage(NBTTagCompoundPacketHandler.Server.class,    PacketNBTTagCompound.class,    8, Side.SERVER);
		instance.registerMessage(NBTTagCompoundPacketHandler.Client.class,    PacketNBTTagCompound.class,    9, Side.CLIENT);
		instance.registerMessage(EntityUpdatePacketHandler.Server.class,      PacketEntityUpdate.class,     10, Side.SERVER);
		instance.registerMessage(EntityUpdatePacketHandler.Client.class,      PacketEntityUpdate.class,     11, Side.CLIENT);
		Print.log("Done initialising Packet Handler.");
	}
	
	public static SimpleNetworkWrapper getInstance(){
		return instance;
	}
	
	public static enum PacketHandlerType {
		TILEENTITY, KEYINPUT, ITEMSTACK, JSON, NBT, ENTITY;
	}
	
	@SuppressWarnings("unchecked")
	public static void registerListener(PacketHandlerType type, Side side, IPacketListener<?> listener){
		switch(type){
			/*case ITEMSTACK:{
				ISUPacketHandler.addListener(side, (IPacketListener<PacketItemStackUpdate>)listener);
				break;
			}*/
			case JSON:{
				JsonObjectPacketHandler.addListener(side, (IPacketListener<PacketJsonObject>)listener);
				break;
			}
			case NBT:{
				NBTTagCompoundPacketHandler.addListener(side, (IPacketListener<PacketNBTTagCompound>) listener);
				break;
			}
			case KEYINPUT:{
				if(side.isServer()){
					KeyInputPacketHandler.addListener((IPacketListener<PacketKeyInput>) listener);
				}
				break;
			}
			case TILEENTITY:{
				if(side.isClient()){
					TileEntityUpdatePacketHandler.Client.addListener((IPacketListener<PacketTileEntityUpdate>) listener);
				}
				if(side.isServer()){
					TileEntityUpdatePacketHandler.Server.addListener((IPacketListener<PacketTileEntityUpdate>) listener);
				}
				break;
			}
			case ENTITY:{
				if(side.isClient()){
					EntityUpdatePacketHandler.Client.addListener((IPacketListener<PacketEntityUpdate>) listener);
				}
				if(side.isServer()){
					EntityUpdatePacketHandler.Server.addListener((IPacketListener<PacketEntityUpdate>) listener);
				}
				break;
			}
			default: break;
		}
		Print.log("[FCL] Registered new PacketListener with ID '" + listener.getId() + "' and type " + type.name() + " for Side:" + (side.isClient() ? "Client" : "Server") + ".");
	}
	
}