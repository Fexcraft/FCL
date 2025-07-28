package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.FclRecipe;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.ui.ContainerInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class SelRecipeCon extends ContainerInterface {

	public static ArrayList<String> categories = new ArrayList<>();
	public static ArrayList<StackWrapper> results = new ArrayList<>();
	public static ArrayList<String> reskeys = new ArrayList<>();

	public SelRecipeCon(JsonMap map, UniEntity ply, V3I pos){
		super(map, ply, pos);
	}

	@Override
	public void init(){
		//
	}

	@Override
	public void packet(TagCW com, boolean client){
		if(com.has("sync_cat")){
			if(client){
				categories.clear();
				TagLW lw = com.getList("cats");
				int am = com.getInteger("cata");
				for(int i = 0; i < am; i++){
					categories.add(lw.getString(i));
				}
			}
			else{
				TagLW list = TagLW.create();
				for(String str : FclRecipe.RECIPES.keySet()) list.add(str);
				com.set("cats", list);
				com.set("cata", list.size());
				SEND_TO_CLIENT.accept(com, player);
			}
		}
		if(com.has("sync_res")){
			if(client){
				results.clear();
				reskeys.clear();
				for(TagCW res : com.getList("res")){
					results.add(UniStack.createStack(res));
					reskeys.add(res.getString("fcl-rec-key"));
				}
			}
			else{
				TagLW list = TagLW.create();
				LinkedHashMap<IDL, ArrayList<FclRecipe>> res = FclRecipe.RECIPES.get(com.getString("c"));
				for(Map.Entry<IDL, ArrayList<FclRecipe>> entry : res.entrySet()){
					TagCW save = TagCW.create();
					entry.getValue().get(0).output.save(save);
					save.set("fcl-rec-key", entry.getKey().colon());
					list.add(save);
				}
				com.set("res", list);
				SEND_TO_CLIENT.accept(com, player);
			}
		}
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
