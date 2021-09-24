package net.fexcraft.lib.mc.network;

import java.util.HashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketHandler {

	public static final Identifier NBT_PACKET = new Identifier("fcl:nbt");
	public static final HashMap<String, PacketListener<NbtCompound>> nbtpktc = new HashMap<>();
	public static final HashMap<String, PacketListener<NbtCompound>> nbtpkts = new HashMap<>();
	
	public static void registerListener(String type, EnvType side, PacketListener<?> listener){
		switch(type){
			case "nbt":{
				if(side == EnvType.CLIENT) nbtpktc.put(type, (PacketListener<NbtCompound>)listener);
				else nbtpkts.put(type, (PacketListener<NbtCompound>)listener);
			}
			default: break;
		}
		Print.log("[FCL] Registered new PacketListener with ID '" + listener.getId() + "' and type " + type + " for side:" + side.name().toLowerCase() + ".");
	}

	public static void regCommonPackets(){
		
	}
	
	@Environment(EnvType.CLIENT)
	public static void regClientPackets(){
		ClientPlayNetworking.registerGlobalReceiver(NBT_PACKET, new PlayChannelHandler(){
			@Override
			public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender){
				NbtCompound compound = buf.readNbt();
				if(!compound.contains("listener")) return;
				PacketListener<NbtCompound> listener = nbtpktc.get(compound.getString("listener"));
				client.execute(() -> listener.process(compound, sender, handler));
			}
		});
	}
	
	@Environment(EnvType.CLIENT)
	public static void sendToServer(PlayerEntity entity, String listener, NbtCompound compound){
		compound.putString("listener", listener);
		ClientPlayNetworking.send(NBT_PACKET, PacketByteBufs.create().writeNbt(compound));
	}
	
	public static void sendToPlayer(PlayerEntity entity, String listener, NbtCompound compound){
		compound.putString("listener", listener);
		ServerPlayNetworking.send((ServerPlayerEntity)entity, NBT_PACKET, PacketByteBufs.create().writeNbt(compound));
	}
	
	public static void sendToTracking(World world, BlockPos pos, String listener, NbtCompound compound){
		compound.putString("listener", listener);
		for(PlayerEntity entity : PlayerLookup.tracking((ServerWorld)world, pos)){
			ServerPlayNetworking.send((ServerPlayerEntity)entity, NBT_PACKET, PacketByteBufs.create().writeNbt(compound));
		}
	}
	
	public static void sendToTracking(Entity tracked, String listener, NbtCompound compound){
		compound.putString("listener", listener);
		for(PlayerEntity entity : PlayerLookup.tracking(tracked)){
			ServerPlayNetworking.send((ServerPlayerEntity)entity, NBT_PACKET, PacketByteBufs.create().writeNbt(compound));
		}
	}
	
}