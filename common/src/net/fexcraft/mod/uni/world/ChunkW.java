package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.ui.UIKey;

import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface ChunkW {

	public boolean isOnClient();

	public int getX();

	public int getZ();

	public WorldW getWorld();

	public <E> E local();

	public Object direct();

	public void markChanged();

}
