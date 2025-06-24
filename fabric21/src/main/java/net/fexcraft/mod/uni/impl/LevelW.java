package net.fexcraft.mod.uni.impl;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.StateWrapper;
import net.fexcraft.mod.uni.world.WorldW;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class LevelW extends WorldW {

	protected MutableBlockPos mpos = new MutableBlockPos();
	protected Level level;

	public LevelW(Level world){
		super();
		level = world;
	}

	@Override
	public boolean isClient(){
		return level.isClientSide;
	}

	@Override
	public boolean isTilePresent(V3I pos){
		return level.getBlockEntity(new BlockPos(pos.x, pos.y, pos.z)) != null;
	}

	@Override
	public Object getBlockEntity(V3I pos){
		return level.getBlockEntity(new BlockPos(pos.x, pos.y, pos.z));
	}

	@Override
	public <W> W local(){
		return (W)level;
	}

	@Override
	public Object direct(){
		return level;
	}

	@Override
	public void setBlockState(V3I pos, StateWrapper state, int flag){
		level.setBlock(new BlockPos(pos.x, pos.y, pos.z), state.local(), flag);
	}

	@Override
	public void spawnBlockSeat(V3D add, EntityW player){
		//
	}

	@Override
	public int dim(){
		return 0;
	}

	@Override
	public void drop(StackWrapper stack, V3D vec){
		level.addFreshEntity(new ItemEntity(level, vec.x, vec.y, vec.z, stack.local()));
	}

	@Override
	public StateWrapper getStateAt(V3I pos){
		return StateWrapper.of(level.getBlockState(new BlockPos(pos.x, pos.y, pos.z)));
	}

	@Override
	public List<EntityW> getPlayers(){
		List<EntityW> list = new ArrayList<>();
		for(Player player : level.players()){
			list.add(UniEntity.getEntity(player));
		}
		return list;
	}

	@Override
	public boolean isPositionLoaded(V3I pos){
		return level.isLoaded(mpos.set(pos.x, pos.y, pos.z));
	}

	@Override
	public String dimkey(){
		return level.dimension().location() + (level.isClientSide ? "c" : "s");
	}

	@Override
	public EntityW getEntity(int id){
		Entity ent = level.getEntity(id);
		return ent == null ? null : UniEntity.getEntity(ent);
	}

	@Override
	public UniEntity getUniEntity(int id){
		Entity ent = level.getEntity(id);
		return ent == null ? null : UniEntity.get(ent);
	}

	@Override
	public long getDayTime(){
		return level.getDayTime();
	}

	@Override
	public List<EntityW> getEntities(V3D pos, double range){
		List<EntityW> list = new ArrayList<>();
		AABB aabb = new AABB(pos.x - range, pos.y - range, pos.z - range, pos.x + range, pos.y + range, pos.z + range);
		for(Entity entity : level.getEntities(null, aabb)) list.add(UniEntity.getEntity(entity));
		return List.of();
	}

}
