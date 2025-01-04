package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.FclRecipe;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.ArrayList;
import java.util.List;

import static net.fexcraft.mod.uni.ui.ContainerInterface.SEND_TO_SERVER;
import static net.fexcraft.mod.uni.ui.ContainerInterface.transformat;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftRecipeUI extends UserInterface {

	private ArrayList<FclRecipe> results;
	private FclRecipe.Component comp;
	private String category;
	private IDL key;
	private int selrec;
	private int compscr;
	private int amount = 1;
	private byte ticker;
	private int current;

	public CraftRecipeUI(JsonMap map, ContainerInterface container) throws Exception{
		super(map, container);
		category = FclRecipe.getCategoryIdAt(container.pos.x);
		key = FclRecipe.getResultKey(category, container.pos.y);
		results = FclRecipe.getCategoryAt(container.pos.x).get(key);
	}

	@Override
	public void init(){
		updateText();
	}

	private void updateText(){
		texts.get("recipe").value(results.get(selrec).output.getName());
		texts.get("selected").value(transformat("ui.fcl.recipe.selected", selrec + 1, results.size()));
		texts.get("amount").value(transformat("ui.fcl.recipe.amount", amount));
	}

	@Override
	public void predraw(float ticks, int mx, int my){
		ticker++;
		if(ticker > 10){
			ticker = 0;
			current++;
		}
	}

	@Override
	public void postdraw(float ticks, int mx, int my){
		FclRecipe rec = results.get(selrec);
		drawer.draw(gLeft + 7, gTop + 10, rec.output, true);
		for(int x = 0; x < 12; x++){
			for(int y = 0; y < 5; y++){
				int z = y * 12 + x + compscr * 12;
				if(z >= rec.components.size()) break;
				comp = rec.components.get(z);
				if(comp.tag){
					if(comp.list == null) comp.refreshList();
					if(comp.list.isEmpty()) continue;
					drawer.draw(gLeft + 6 + x * 18, gTop + 34 + y * 18, comp.list.get(current % comp.list.size()), true);
				}
				else drawer.draw(gLeft + 6 + x * 18, gTop + 34 + y * 18, comp.stack, true);
			}
		}
	}

	@Override
	public boolean onAction(UIButton button, String id, int x, int y, int b){
		switch(id){
			case "prev":{
				selrec--;
				if(selrec < 0) selrec = 0;
				updateText();
				return true;
			}
			case "next":{
				selrec++;
				if(selrec >= results.size()){
					selrec = results.size() - 1;
				}
				updateText();
				return true;
			}
			case "up":{
				compscr--;
				if(compscr < 0) compscr = 0;
				return true;
			}
			case "down":{
				compscr++;
				return true;
			}
			case "less":{
				amount--;
				if(amount < 1) amount = 1;
				updateText();
				return true;
			}
			case "more":{
				amount++;
				if(amount > 64) amount = 64;
				updateText();
				return true;
			}
			case "craft":
			case "craftexit":{
				TagCW com = TagCW.create();
				com.set("exit", id.equals("craftexit"));
				com.set("cat", category);
				com.set("res", key.colon());
				com.set("idx", selrec);
				com.set("am", amount);
				SEND_TO_SERVER.accept(com);
				return true;
			}
		}
		return false;
	}

	@Override
	public void getTooltip(int mx, int my, List<String> list){
		if(mx >= gLeft + 7 && mx <= gLeft + 23 && my >= gTop + 10 && my <= gTop + 26){
			list.add(results.get(selrec).output.getName());//TODO full tooltip later on
		}
		if(mx >= gLeft + 6 && mx <= gLeft + 222 && my >= gTop + 34 && my <= gTop + 104){
			int x = (mx - gLeft - 6) / 18;
			int y = (my - gTop - 34) / 18;
			int z = y * 12 + x + compscr * 12;
			if(z>= 0 && z < results.get(selrec).components.size()){
				comp = results.get(selrec).components.get(z);
				if(comp.tag){
					if(comp.list != null){
						list.add(comp.list.get(current % comp.list.size()).getName());
					}
				}
				else list.add(comp.stack.getName());
			}
		}
	}

	@Override
	public boolean keytyped(char c, int code){
		if(code == 1){
			TagCW com = TagCW.create();
			com.set("ret", category);
			SEND_TO_SERVER.accept(com);
			return true;
		}
		return false;
	}

}
