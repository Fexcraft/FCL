package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class WorldWI extends WorldW {

	protected World world;

	public WorldWI(World level){
		world = level;
	}

	@Override
	public boolean isClient(){
		return world.isRemote;
	}

	@Override
	public boolean isTilePresent(V3I pos){
		return world.getTileEntity(new BlockPos(pos.x, pos.y, pos.z)) != null;
	}

	@Override
	public Object getBlockEntity(V3I pos){
		return world.getTileEntity(new BlockPos(pos.x, pos.y, pos.z));
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
	public void setBlockState(V3I pos, StateWrapper state, int flag) {
		world.setBlockState(new BlockPos(pos.x, pos.y, pos.z), state.local(), flag);
	}

	@Override
	public void spawnBlockSeat(V3D vec, EntityW player){
		//
	}

	@Override
	public int dim(){
		return world.provider.getDimension();
	}

	@Override
	public void drop(StackWrapper stack, V3D vec){
		EntityItem item = new EntityItem(world);
		item.setPosition(vec.x, vec.y, vec.z);
		item.setItem(stack.local());
		world.spawnEntity(item);
	}

	@Override
	public StateWrapper getStateAt(V3I pos){
		return StateWrapper.of(world.getBlockState(new BlockPos(pos.x, pos.y, pos.z)));
	}

	@Override
	public List<EntityW> getPlayers(){
		ArrayList<EntityW> list = new ArrayList();
		for(EntityPlayer ent : world.playerEntities){
			list.add(UniEntity.getEntity(ent));
		}
		return list;
	}

}
