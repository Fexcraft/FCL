package net.fexcraft.mod.lib.network;

import net.fexcraft.mod.lib.api.network.IPacketListener;
import net.fexcraft.mod.lib.network.handlers.EntityUpdatePacketHandler;
import net.fexcraft.mod.lib.network.handlers.ExamplePacketHandler;
import net.fexcraft.mod.lib.network.handlers.ISUPacketHandler;
import net.fexcraft.mod.lib.network.handlers.JsonObjectPacketHandler;
import net.fexcraft.mod.lib.network.handlers.KeyInputPacketHandler;
import net.fexcraft.mod.lib.network.handlers.NBTTagCompoundPacketHandler;
import net.fexcraft.mod.lib.network.handlers.TileEntityUpdatePacketHandler;
import net.fexcraft.mod.lib.network.packet.Packet;
import net.fexcraft.mod.lib.network.packet.PacketEntityUpdate;
import net.fexcraft.mod.lib.network.packet.PacketItemStackUpdate;
import net.fexcraft.mod.lib.network.packet.PacketJsonObject;
import net.fexcraft.mod.lib.network.packet.PacketKeyInput;
import net.fexcraft.mod.lib.network.packet.PacketNBTTagCompound;
import net.fexcraft.mod.lib.network.packet.PacketTileEntityUpdate;
import net.fexcraft.mod.lib.util.common.Print;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler{
	
	private static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel("frsm");
	
	public static void init(){
		Print.log("Initialising Packet Handler.");
		instance.registerMessage(ExamplePacketHandler.class,                  Packet.class,                  0, Side.SERVER);
		instance.registerMessage(TileEntityUpdatePacketHandler.Client.class,  PacketTileEntityUpdate.class,  1, Side.CLIENT);
		instance.registerMessage(TileEntityUpdatePacketHandler.Server.class,  PacketTileEntityUpdate.class,  2, Side.SERVER);
		instance.registerMessage(KeyInputPacketHandler.class,                 PacketKeyInput.class,          3, Side.SERVER);
		instance.registerMessage(ISUPacketHandler.Server.class,               PacketItemStackUpdate.class,   4, Side.SERVER);
		instance.registerMessage(ISUPacketHandler.Client.class,               PacketItemStackUpdate.class,   5, Side.CLIENT);
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
	
	public static enum PacketHandlerType{
		TILEENTITY, KEYINPUT, ITEMSTACK, JSON, NBT, ENTITY;
	}
	
	public static void registerListener(PacketHandlerType type, Side side, IPacketListener listener){
		switch(type){
			case ITEMSTACK:{
				ISUPacketHandler.addListener(side, listener);
				break;
			}
			case JSON:{
				JsonObjectPacketHandler.addListener(side, listener);break;
			}
			case NBT:{
				NBTTagCompoundPacketHandler.addListener(side, listener);break;
			}
			case KEYINPUT:{
				if(side.isServer()){
					KeyInputPacketHandler.addListener(listener);
				}
				break;
			}
			case TILEENTITY:{
				if(side.isClient()){
					TileEntityUpdatePacketHandler.Client.addListener(listener);
				}
				if(side.isServer()){
					TileEntityUpdatePacketHandler.Server.addListener(listener);
				}
				break;
			}
			case ENTITY:{
				if(side.isClient()){
					EntityUpdatePacketHandler.Client.addListener(listener);
				}
				if(side.isServer()){
					EntityUpdatePacketHandler.Server.addListener(listener);
				}
				break;
			}
			default: break;
		}
		Print.log("[FCL] Registered new PacketListener with ID '" + listener.getId() + "' and type " + type.name() + " for Side:" + (side.isClient() ? "Client" : "Server") + ".");
	}
	
}