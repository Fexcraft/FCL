package net.fexcraft.mod.uni.impl;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.StateWrapper;
import net.fexcraft.mod.uni.world.WorldType;
import net.fexcraft.mod.uni.world.WorldW;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class WorldWI extends WorldW {

	protected WorldType type;
	protected World world;

	public WorldWI(World level){
		world = level;
		type = new WorldType(world.provider.dimensionId, level.isRemote);
	}

	@Override
	public boolean isClient(){
		return world.isRemote;
	}

	@Override
	public boolean isTilePresent(V3I pos){
		return world.getTileEntity(pos.x, pos.y, pos.z) != null;
	}

	@Override
	public Object getBlockEntity(V3I pos){
		return world.getTileEntity(pos.x, pos.y, pos.z);
	}

	@Override
	public <W> W local(){
		return (W)world;
	}

	@Override
	public Object direct(){
		return world;
	}

	@Override
	public WorldType type(){
		return type;
	}

	@Override
	public void setBlockState(V3I pos, StateWrapper state, int flag) {
		world.setBlock(pos.x, pos.y, pos.z, (Block)state.getBlock(), state.get12Meta(), flag);
	}

	@Override
	public void spawnBlockSeat(V3D vec, EntityW player){
		//
	}

	@Override
	public void drop(StackWrapper stack, V3D vec){
		EntityItem item = new EntityItem(world);
		item.setPosition(vec.x, vec.y, vec.z);
		item.setEntityItemStack(stack.local());
		world.spawnEntityInWorld(item);
	}

	@Override
	public StateWrapper getStateAt(V3I pos){
		return StateWrapper.of(world.getBlock(pos.x, pos.y, pos.z));
	}

	@Override
	public List<EntityW> getPlayers(){
		ArrayList<EntityW> list = new ArrayList();
		for(Object ent : world.playerEntities){
			list.add(UniEntity.getEntity(ent));
		}
		return list;
	}

	@Override
	public boolean isPositionLoaded(V3I pos){
		return true;//TODO
	}

	@Override
	public EntityW getEntity(int id){
		Entity ent = world.getEntityByID(id);
		return ent == null ? null : UniEntity.getEntityN(ent);
	}

	@Override
	public UniEntity getUniEntity(int id){
		Entity ent = world.getEntityByID(id);
		return ent == null ? null : UniEntity.get(ent);
	}

	@Override
	public long getDayTime(){
		return world.getWorldTime();
	}

	@Override
	public List<EntityW> getEntities(V3D pos, double range){
		List<EntityW> list = new ArrayList<>();
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(pos.x - range, pos.y - range, pos.z - range, pos.x + range, pos.y + range, pos.z + range);
		Entity entity;
		for(Object eo : world.loadedEntityList){
			entity = (Entity)eo;
			if(entity.getBoundingBox().intersectsWith(aabb)){
				list.add(UniEntity.getEntityN(entity));
			}
		}
		return list;
	}

	@Override
	public boolean isRainingAt(double x, double y, double z){
		return world.isRaining();
	}

}
