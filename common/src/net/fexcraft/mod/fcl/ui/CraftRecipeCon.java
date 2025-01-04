package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.*;
import net.fexcraft.mod.uni.ConfigBase.ConfigEntry;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.InventoryInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftRecipeCon extends InventoryInterface {

	public CraftRecipeCon(JsonMap map, UniEntity ply, V3I pos){
		super(map, ply, pos);
	}

	@Override
	public void init(){
		//
	}

	@Override
	public void packet(TagCW com, boolean client){
		if(com.has("ret")){
			int catidx = FclRecipe.indexOfCategory(com.getString("ret"));
			player.entity.openUI(UniFCL.SELECT_RECIPE_RESULT, catidx, 0, 0);
		}
		if(com.has("cat")){
			FclRecipe rec = FclRecipe.RECIPES.get(com.getString("cat")).get(IDLManager.getIDL(com.getString("res"))).get(com.getInteger("idx"));
			//
		}
	}

	@Override
	public Object getInventory(){
		return null;
	}

	@Override
	public void setInventoryContent(int index, TagCW com){

	}

	@Override
	public StackWrapper getInventoryContent(int index){
		return null;
	}

	@Override
	public boolean isInventoryEmpty(int at){
		return true;
	}

}
