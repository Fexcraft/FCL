package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.*;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;

import java.util.ArrayList;

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
		if(com.has("ret")){
			int catidx = FclRecipe.indexOfCategory(com.getString("ret"));
			player.entity.openUI(UniFCL.SELECT_RECIPE_RESULT, catidx, 0, 0);
		}
		if(com.has("cat")){
			FclRecipe rec = FclRecipe.RECIPES.get(com.getString("cat")).get(IDLManager.getIDL(com.getString("res"))).get(com.getInteger("idx"));
			int cr = 0;
			for(int am = 0; am < com.getInteger("am"); am++) {
				if(canCraft(rec)){
					doCraft(rec);
					player.entity.addStack(rec.output.copy());
					cr++;
				}
				else break;
			}
			if(cr > 0) player.entity.send("ui.fcl.recipe.craft_success", cr, rec.output.getName(), rec.output.count());
			if(com.getBoolean("exit")) player.entity.closeUI();
		}
	}

	private boolean canCraft(FclRecipe rec){
		ArrayList<StackWrapper> copy = new ArrayList<>();
		for(int idx = 0; idx < player.entity.getInventorySize(); idx++){
			StackWrapper wrp = player.entity.getStackAt(idx);
			if(!wrp.empty()) copy.add(wrp.copy());
		}
		for(FclRecipe.Component comp : rec.components){
			if(comp.tag){
				if(comp.list == null) comp.refreshList();
				if(comp.list.isEmpty()){
					player.entity.send("ui.fcl.recipe.comp_invalid0");
					player.entity.send("ui.fcl.recipe.comp_invalid1");
					player.entity.send(comp.id);
					return false;
				}
				int needed = comp.amount;
				for(StackWrapper stack : comp.list){
					needed -= consume(copy, stack, needed);
					if(needed <= 0) break;
				}
				if(needed > 0){
					player.entity.send("ui.fcl.recipe.not_enough_comp", comp.id);
					return false;
				}
			}
			else{
				if(comp.stack.empty()){
					player.entity.send("ui.fcl.recipe.comp_invalid0");
					player.entity.send("ui.fcl.recipe.comp_invalid1");
					return false;
				}
				if(consume(copy, comp.stack, comp.amount) < comp.amount){
					player.entity.send("ui.fcl.recipe.not_enough_item", comp.stack.getName());
					return false;
				}
			}
		}
		return true;
	}

	private int consume(ArrayList<StackWrapper> copy, StackWrapper stack, int am){
		int cons = 0;
		for(StackWrapper wrp : copy){
			if(wrp.equals(stack)){
				cons += wrp.count();
				wrp.count(wrp.count() - am);
				if(cons >= am) break;
			}
		}
		copy.removeIf(StackWrapper::empty);
		return cons;
	}

	private void doCraft(FclRecipe rec){
		ArrayList<StackWrapper> copy = new ArrayList<>();
		for(int idx = 0; idx < player.entity.getInventorySize(); idx++){
			StackWrapper wrp = player.entity.getStackAt(idx);
			if(!wrp.empty()) copy.add(wrp);
		}
		for(FclRecipe.Component comp : rec.components){
			if(comp.tag){
				int needed = comp.amount;
				for(StackWrapper stack : comp.list){
					needed -= consume(copy, stack, needed);
					if(needed <= 0) break;
				}
			}
			else{
				if(comp.stack.empty()) return;
				consume(copy, comp.stack, comp.stack.count());
			}
		}
	}

}
