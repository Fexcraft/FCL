package net.fexcraft.lib.mc.network;

import org.spongepowered.asm.mixin.MixinEnvironment.Side;

import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.network.Packet;

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
	public static void registerListener(PacketHandlerType type, Side side, PacketListener<?> listener){
		switch(type){
			/*case ITEMSTACK:{
				ISUPacketHandler.addListener(side, (IPacketListener<PacketItemStackUpdate>)listener);
				break;
			}*/
			case JSON:{
				JsonObjectPacketHandler.addListener(side, (PacketListener<PacketJsonObject>)listener);
				break;
			}
			case NBT:{
				NBTTagCompoundPacketHandler.addListener(side, (PacketListener<PacketNBTTagCompound>) listener);
				break;
			}
			case KEYINPUT:{
				if(side.isServer()){
					KeyInputPacketHandler.addListener((PacketListener<PacketKeyInput>) listener);
				}
				break;
			}
			case TILEENTITY:{
				if(side.isClient()){
					TileEntityUpdatePacketHandler.Client.addListener((PacketListener<PacketTileEntityUpdate>) listener);
				}
				if(side.isServer()){
					TileEntityUpdatePacketHandler.Server.addListener((PacketListener<PacketTileEntityUpdate>) listener);
				}
				break;
			}
			case ENTITY:{
				if(side.isClient()){
					EntityUpdatePacketHandler.Client.addListener((PacketListener<PacketEntityUpdate>) listener);
				}
				if(side.isServer()){
					EntityUpdatePacketHandler.Server.addListener((PacketListener<PacketEntityUpdate>) listener);
				}
				break;
			}
			default: break;
		}
		Print.log("[FCL] Registered new PacketListener with ID '" + listener.getId() + "' and type " + type.name() + " for Side:" + (side.isClient() ? "Client" : "Server") + ".");
	}
	
}