package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.EntityW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class InventoryInterface extends ContainerInterface {

	public InventoryInterface(JsonMap map, UniEntity ply, V3I pos){
		super(map, ply, pos);
	}

	public abstract Object getInventory();

	public abstract void setInventoryContent(int index, TagCW com);

	public abstract StackWrapper getInventoryContent(int index);

	public abstract boolean isInventoryEmpty(int at);

}
