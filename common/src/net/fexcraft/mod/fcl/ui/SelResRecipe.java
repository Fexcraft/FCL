package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.List;

import static net.fexcraft.lib.common.utils.Formatter.PARAGRAPH_SIGN;
import static net.fexcraft.mod.fcl.ui.SelRecipeCon.reskeys;
import static net.fexcraft.mod.fcl.ui.SelRecipeCon.results;
import static net.fexcraft.mod.uni.ui.ContainerInterface.SEND_TO_SERVER;
import static net.fexcraft.mod.uni.ui.ContainerInterface.translate;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class SelResRecipe extends UserInterface {

	private String category;
	private int scroll;

	public SelResRecipe(JsonMap map, ContainerInterface container) throws Exception{
		super(map, container);
		category = SelRecipeCon.categories.get(container.pos.x);
	}

	@Override
	public void init(){
		results.clear();
		TagCW req = TagCW.create();
		req.set("sync_res", true);
		req.set("c", category);
		SEND_TO_SERVER.accept(req);
		texts.get("title").value("ui.fcl.recipe.results");
		texts.get("title").translate(translate(category));
	}

	@Override
	public void predraw(float ticks, int mx, int my){
		for(int i = 0; i < 12; i++){
			int j = scroll + i;
			if(j >= results.size()) buttons.get("entry_" + i).text.value("");
			else buttons.get("entry_" + i).text.value(results.get(j).getName());
		}
	}

	@Override
	public boolean onAction(UIButton button, String id, int x, int y, int b){
		if(id.equals("up")){
			scroll--;
			if(scroll < 0) scroll = 0;
			return true;
		}
		else if(id.equals("down")){
			scroll++;
			return true;
		}
		else if(id.startsWith("entry_")){
			int idx = Integer.parseInt(id.substring(6));
			idx += scroll;
			if(idx < 0 || idx >= results.size()) return true;
			TagCW com = TagCW.create();
			com.set("cat", category);
			com.set("res", reskeys.get(idx));
			SEND_TO_SERVER.accept(com);
			return true;
		}
		return false;
	}

	@Override
	public boolean onScroll(UIButton button, String id, int mx, int my, int am){
		scroll += am;
		if(scroll < 0) scroll = 0;
		return true;
	}

	@Override
	public void getTooltip(int mx, int my, List<String> list){
		for(int i = 0; i < 12; i++){
			int j = scroll + i;
			if(j < results.size() && buttons.get("entry_" + i).hovered()){
				list.add(PARAGRAPH_SIGN + "9" + results.get(j).getName());
			}
		}
	}

	@Override
	public boolean keytyped(char c, int code){
		if(code == 1){
			TagCW com = TagCW.create();
			com.set("main", true);
			SEND_TO_SERVER.accept(com);
			return true;
		}
		return false;
	}

}
