package net.fexcraft.mod.uni.impl;

import com.mojang.authlib.GameProfile;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.CubeSide;
import net.fexcraft.mod.uni.world.WorldW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class WrapperHolderImpl extends WrapperHolder {

	private V3I vpos = new V3I();
	private WorldW client;

	@Override
	protected boolean isSinglePlayer0(){
		MinecraftServer server = FCL.SERVER.get();
		return server != null && server.isSingleplayer();
	}

	@Override
	public <W extends WorldW> W getClientWorld0(){
		if(client == null){
			client = getWorld(net.minecraft.client.Minecraft.getInstance().level);
		}
		return (W)client;
	}

	@Override
	public File getWorldFolder0(WorldW world){
		return FCL.GAMEDIR;
	}

	@Override
	public V3I getPos0(Object o){
		BlockPos pos = (BlockPos)o;
		return new V3I(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public V3I getPos0(long l){
		return getPos0(BlockPos.of(l));
	}

	@Override
	public CubeSide getSide0(Object o){
		switch((Direction)o){
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
		switch(side){
			case UP: return (S)Direction.UP;
			case DOWN: return (S)Direction.DOWN;
			case NORTH: return (S)Direction.NORTH;
			case WEST: return (S)Direction.WEST;
			case EAST: return (S)Direction.EAST;
			case SOUTH: return (S)Direction.SOUTH;
		}
		return (S)Direction.NORTH;
	}

	@Override
	public List<UUID> getOnlinePlayerIDs0(){
		List<UUID> list = new ArrayList<>();
		for(ServerPlayer player : FCL.SERVER.get().getPlayerList().getPlayers()){
			list.add(player.getGameProfile().getId());
		}
		return list;
	}

	@Override
	public UUID getUUIDFor0(String string){
		Optional<GameProfile> gp = FCL.SERVER.get().getProfileCache().get(string);
		return gp.isPresent() ? gp.get().getId() : null;
	}

	@Override
	public String getNameFor0(UUID uuid){
		Optional<GameProfile> gp = FCL.SERVER.get().getProfileCache().get(uuid);
		return gp.isPresent() ? gp.get().getName() : "N/F";
	}

	@Override
	public void schedule0(Runnable run){
		FCL.SERVER.get().execute(run);
	}

	@Override
	public void reset(){
		client = null;
	}

	@Override
	public TagCW read0(File file){
		try{
			return TagCW.create();//TODO .wrap(NbtIo.read(file));
		}
		catch(Exception e){
			e.printStackTrace();
			return TagCW.create();
		}
	}

	@Override
	public void write0(TagCW compound, File file){
		try{
			//TODO NbtIo.write(compound.local(), file);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected V3I mutPos0(Object pos){
		BlockPos bpos = (BlockPos)pos;
		return vpos.set(bpos.getX(), bpos.getY(), bpos.getZ());
	}

	@Override
	protected V3I mutPos0(int x, int y, int z){
		return vpos.set(x, y, z);
	}

	@Override
	protected InputStream getDataResource0(IDL loc) throws IOException {
		Optional<Resource> is = FCL.SERVER.get().getResourceManager().getResource(loc.local());
		return is.isPresent() ? is.get().open() : null;
	}

}
