package net.fexcraft.mod.uni.world;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface ChunkW {

	public boolean isOnClient();

	@Deprecated
	public int getX();

	@Deprecated
	public int getZ();

	public int x();

	public int z();

	public WorldW getWorld();

	public <E> E local();

	public Object direct();

	public void markChanged();

}
