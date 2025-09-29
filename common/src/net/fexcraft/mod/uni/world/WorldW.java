package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.inv.StackWrapper;

import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class WorldW {

	public abstract boolean isClient();

    public abstract boolean isTilePresent(V3I pos);

    public abstract Object getBlockEntity(V3I pos);

    public abstract <W> W local();

    public abstract Object direct();

	public abstract WorldType type();

    public void setBlockState(V3I pos, StateWrapper state){
		setBlockState(pos, state, 3);
	}

    public abstract void setBlockState(V3I pos, StateWrapper state, int flag);

    public abstract void spawnBlockSeat(V3D add, EntityW player);

	public abstract void drop(StackWrapper stack, V3D vec);

	public abstract StateWrapper getStateAt(V3I pos);

	public abstract List<EntityW> getPlayers();

	public abstract boolean isPositionLoaded(V3I pos);

	public abstract EntityW getEntity(int ent);

	public abstract UniEntity getUniEntity(int ent);

	public abstract long getDayTime();

	public abstract List<EntityW> getEntities(V3D pos, double range);

	public abstract boolean isRainingAt(double x, double y, double z);

	public boolean isRainingAt(V3D pos){
		return isRainingAt(pos.x, pos.y, pos.z);
	}

}
