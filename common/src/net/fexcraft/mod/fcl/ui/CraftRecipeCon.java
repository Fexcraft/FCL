package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.ConfigBase;
import net.fexcraft.mod.uni.ConfigBase.ConfigEntry;
import net.fexcraft.mod.uni.FclRecipe;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftRecipeCon extends ContainerInterface {

	public CraftRecipeCon(JsonMap map, UniEntity ply, V3I pos){
		super(map, ply, pos);
	}

	@Override
	public void init(){
		//
	}

	@Override
	public void packet(TagCW com, boolean client){
		if(com.has("main")){
			player.entity.openUI(UniFCL.SELECT_RECIPE_CATEGORY, V3I.NULL);
		}
		if(com.has("cat")){
			int catidx = FclRecipe.indexOfCategory(com.getString("cat"));
			if(com.has("res")){
				player.entity.openUI(UniFCL.RECIPE_CRAFTING, catidx, FclRecipe.getResultIdx(com.getString("cat"), com.getString("res")), 0);
			}
			else{
				player.entity.openUI(UniFCL.SELECT_RECIPE_RESULT, catidx, 0, 0);
			}
		}
	}

}
