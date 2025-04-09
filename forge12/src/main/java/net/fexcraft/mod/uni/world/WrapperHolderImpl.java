package net.fexcraft.mod.uni.world;

import com.mojang.authlib.GameProfile;
import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class WrapperHolderImpl extends WrapperHolder {

	private static WorldW client;
	private static V3I mvec = new V3I();

	@Override
	public <W extends WorldW> W getClientWorld0(){
		if(client == null){
			client = getWorld(net.minecraft.client.Minecraft.getMinecraft().world);
		}
		return (W)client;
	}

	@Override
	protected boolean isOp0(EntityW player, int lvl){
		UserListOpsEntry entry = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(((EntityPlayerMP)player.direct()).getGameProfile());
		return entry != null && entry.getPermissionLevel() >= lvl;
	}

	@Override
	protected EntityW getPlayer0(UUID uuid){
		for(EntityPlayerMP player : Static.getServer().getPlayerList().getPlayers()){
			if(player.getGameProfile().getId().equals(uuid)){
				UniEntity ent = UniEntity.get(player);
				return ent != null ? ent.entity  : null;
			}
		}
		return null;
	}

	@Override
	protected List<UniEntity> getPlayers0(){
		ArrayList<UniEntity> list = new ArrayList<>();
		for(EntityPlayerMP player : Static.getServer().getPlayerList().getPlayers()){
			UniEntity ent = UniEntity.get(player);
			if(ent != null) list.add(ent);
		}
		return list;
	}

	@Override
	protected boolean isSinglePlayer0(){
		return Static.getServer() != null && Static.getServer().isSinglePlayer();
	}

	@Override
	public File getWorldFolder0(WorldW wr, String name){
		World world = wr.local();
		File wf = new File(world.getSaveHandler().getWorldDirectory(), (wr.dim() == 0 ? "" : world.provider.getSaveFolder()));
		wf = new File(wf, name);
		if(!wf.exists()) wf.mkdirs();
		return wf;
	}

	@Override
	public V3I getPos0(Object o){
		BlockPos pos = (BlockPos)o;
		return new V3I(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public V3I getPos0(long l){
		return getPos0(BlockPos.fromLong(l));
	}

	@Override
	public CubeSide getSide0(Object o){
		EnumFacing facing = (EnumFacing)o;
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
		switch(side){
			case UP: return (S)EnumFacing.UP;
			case DOWN: return (S)EnumFacing.DOWN;
			case NORTH: return (S)EnumFacing.NORTH;
			case SOUTH: return (S)EnumFacing.SOUTH;
			case EAST: return (S)EnumFacing.EAST;
			case WEST: return (S)EnumFacing.WEST;
		}
		return (S)EnumFacing.NORTH;
	}

	@Override
	public List<UUID> getOnlinePlayerIDs0(){
		List<UUID> list = new ArrayList<>();
		for(GameProfile prof : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOnlinePlayerProfiles()){
			list.add(prof.getId());
		}
		return list;
	}

	@Override
	public UUID getUUIDFor0(String string){
		GameProfile gp = Static.getServer().getPlayerProfileCache().getGameProfileForUsername(string);
		return gp == null ? null : gp.getId();
	}

	@Override
	public String getNameFor0(UUID uuid){
		GameProfile gp = Static.getServer().getPlayerProfileCache().getProfileByUUID(uuid);
		return gp == null ? "N/F" : gp.getName();
	}

	@Override
	public void schedule0(Runnable run){
		Static.getServer().addScheduledTask(run);
	}

	@Override
	public void reset(){
		client = null;
	}

	@Override
	public TagCW read0(File file){
		try{
			return TagCW.wrap(CompressedStreamTools.read(file));
		}
		catch(Exception e){
			return TagCW.create();
		}
	}

	@Override
	public void write0(TagCW compound, File file){
		try{
			CompressedStreamTools.write(compound.local(), file);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	protected V3I mutPos0(Object obj){
		BlockPos pos = (BlockPos)obj;
		return mvec.set(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	protected V3I mutPos0(int x, int y, int z){
		return mvec.set(x, y, z);
	}

	@Override
	protected InputStream getDataResource0(IDL loc) throws IOException{
		return WrapperHolder.class.getClassLoader().getResourceAsStream("data/" + loc.space() + "/" + loc.path());
	}

}
