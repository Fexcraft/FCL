package net.fexcraft.mod.uni.impl;

import com.mojang.authlib.GameProfile;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.world.CubeSide;
import net.fexcraft.mod.uni.world.WorldW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class WrapperHolderImpl extends WrapperHolder {

	private WorldW client;

	@Override
	public <W extends WorldW> W getClientWorld0(){
		if(client == null){
			client = getWorld(net.minecraft.client.Minecraft.getInstance().level);
		}
		return (W)client;
	}

	@Override
	public V3I getPos0(Object o){
		BlockPos pos = (BlockPos)o;
		return new V3I(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public CubeSide getSide0(Object o){
		CubeSide facing = (CubeSide)o;
		switch(facing){
			case UP: return CubeSide.UP;
			case DOWN: return CubeSide.DOWN;
			case NORTH: return CubeSide.NORTH;
			case WEST: return CubeSide.WEST;
			case EAST: return CubeSide.EAST;
			case SOUTH: return CubeSide.SOUTH;
		}
		return CubeSide.NORTH;
	}

	@Override
	public <S> S getLocalSide0(CubeSide side){
		Direction dir = Direction.NORTH;
		switch(side){
			case UP: return (S)Direction.UP;
			case DOWN: return (S)Direction.DOWN;
			case NORTH: return (S)Direction.NORTH;
			case WEST: return (S)Direction.WEST;
			case EAST: return (S)Direction.EAST;
			case SOUTH: return (S)Direction.SOUTH;
		}
		return (S)dir;
	}

	@Override
	public List<UUID> getOnlinePlayerIDs0(){
		List<UUID> list = new ArrayList<>();
		for(ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()){
			list.add(player.getGameProfile().getId());
		}
		return list;
	}

	@Override
	public UUID getUUIDFor0(String string){
		Optional<GameProfile> gp = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(string);
		return gp.isPresent() ? gp.get().getId() : null;
	}

	@Override
	public String getNameFor0(UUID uuid){
		Optional<GameProfile> gp = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(uuid);
		return gp.isPresent() ? gp.get().getName() : "N/F";
	}

	@Override
	public void schedule0(Runnable run){
		ServerLifecycleHooks.getCurrentServer().execute(run);
	}

	@Override
	public void reset(){
		client = null;
	}

}
