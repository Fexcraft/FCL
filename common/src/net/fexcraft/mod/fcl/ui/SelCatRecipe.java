package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.List;

import static net.fexcraft.lib.common.utils.Formatter.PARAGRAPH_SIGN;
import static net.fexcraft.mod.fcl.ui.SelRecipeCon.categories;
import static net.fexcraft.mod.uni.ui.ContainerInterface.*;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class SelCatRecipe extends UserInterface {

	private int scroll;

	public SelCatRecipe(JsonMap map, ContainerInterface container) throws Exception{
		super(map, container);
	}

	@Override
	public void init(){
		TagCW req = TagCW.create();
		req.set("sync_cat", true);
		SEND_TO_SERVER.accept(req);
		texts.get("title").value("ui.fcl.recipe.categories");
		texts.get("title").translate();
	}

	@Override
	public void predraw(float ticks, int mx, int my){
		for(int i = 0; i < 12; i++){
			int j = scroll + i;
			if(j >= categories.size()) buttons.get("entry_" + i).text.value("");
			else buttons.get("entry_" + i).text.value(translate(categories.get(j)));
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
			if(idx < 0 || idx >= categories.size()) return true;
			TagCW com = TagCW.create();
			com.set("cat", categories.get(idx));
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
			if(j < categories.size() && buttons.get("entry_" + i).hovered()){
				list.add(PARAGRAPH_SIGN + "9" + TRANSLATOR.apply(categories.get(j)));
			}
		}
	}

}
