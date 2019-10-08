package net.fexcraft.lib.mc.network;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fexcraft.lib.mc.network.handlers.EntityPacketHandler;
import net.fexcraft.lib.mc.network.handlers.CompoundTagPacketHandler;
import net.fexcraft.lib.mc.network.handlers.BlockEntityPacketHandler;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class PacketHandler {
	
	public static Identifier PACKET_NBT = new Identifier("fcl:nbt");
	public static Identifier PACKET_ENTITY = new Identifier("fcl:entity");
	public static Identifier PACKET_BLOCKENTITY = new Identifier("fcl:blockentity");

	public static void registerPackets(boolean server){
		if(server){
			ServerSidePacketRegistry.INSTANCE.register(PACKET_NBT, new CompoundTagPacketHandler());
			ServerSidePacketRegistry.INSTANCE.register(PACKET_ENTITY, new EntityPacketHandler());
			ServerSidePacketRegistry.INSTANCE.register(PACKET_BLOCKENTITY, new BlockEntityPacketHandler());
		}
		else{
			ClientSidePacketRegistry.INSTANCE.register(PACKET_NBT, new CompoundTagPacketHandler());
			ClientSidePacketRegistry.INSTANCE.register(PACKET_ENTITY, new EntityPacketHandler());
			ClientSidePacketRegistry.INSTANCE.register(PACKET_BLOCKENTITY, new BlockEntityPacketHandler());
		}
	}
	
	public static enum Type {
		BLOCKENTITY, NBT, ENTITY;
	}
	
	public static void registerListener(Type type, boolean server, IPacketListener<CompoundTag> listener){
		switch(type){
			case NBT:{
				CompoundTagPacketHandler.addListener(server, (IPacketListener<CompoundTag>)listener); break;
			}
			case BLOCKENTITY:{
				BlockEntityPacketHandler.addListener(server, (IPacketListener<CompoundTag>)listener); break;
			}
			case ENTITY:{
				EntityPacketHandler.addListener(server, (IPacketListener<CompoundTag>)listener); break;
			}
			default: break;
		}
		Print.log("Registered new PacketListener with ID '" + listener.getId() + "' and type " + type.name() + " for Side:" + (!server ? "Client" : "Server") + ".");
	}
	
	public static void sendTo(PlayerEntity player, Identifier id, PacketByteBuf buf){
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, id, buf);
	}
	
	public static void sendToAll(){
		//TODO
	}
	
	//TODO more send methods
	
}