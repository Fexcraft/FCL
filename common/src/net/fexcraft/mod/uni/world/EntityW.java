package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.UIKey;

import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface EntityW extends MessageSender {

	public boolean isOnClient();

	public int getId();

	public WorldW getWorld();

	public boolean isPlayer();

	public boolean isAnimal();

	public boolean isHostile();

	public boolean isLiving();

	public boolean isRiding();

	public String getRegName();

	public <E> E local();

	public Object direct();

	public V3D getPos();

	public void setPos(V3D pos);

	public V3D getPrevPos();

	public void setPrevPos(V3D pos);

	public V3I getV3I();

	public void decreaseXZMotion(double x);

	public void setYawPitch(float oyaw, float opitch, float yaw, float pitch);

	public void openUI(String id, V3I pos);

	public void openUI(UIKey key, V3I pos);

	public default void openUI(UIKey key, int x, int y, int z){
		openUI(key, new V3I(x, y, z));
	}

	public void drop(StackWrapper stack, float height);

	public boolean isCreative();

	public UUID getUUID();

	public StackWrapper getHeldItem(boolean main);

	public void closeUI();

	public String dimid();

	public int dim12();

	public int getInventorySize();

	public StackWrapper getStackAt(int idx);

	public void addStack(StackWrapper stack);

	public V3D getEyeVec();

	public V3D getLookVec();

	public boolean isShiftDown();

	public void playSound(Object event, float volume, float pitch);

	public void remove();

	public boolean isRemoved();

	public void onPacket(EntityW player, TagCW packet);

	public void setOnGround(boolean bool);

}
