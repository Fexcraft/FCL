package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.FclRecipe;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static net.fexcraft.lib.common.utils.Formatter.PARAGRAPH_SIGN;
import static net.fexcraft.mod.uni.ui.ContainerInterface.SEND_TO_SERVER;
import static net.fexcraft.mod.uni.ui.ContainerInterface.translate;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftRecipeUI extends UserInterface {

	private ArrayList<FclRecipe> results;
	private String category;
	private IDL key;
	private int selrec;

	public CraftRecipeUI(JsonMap map, ContainerInterface container) throws Exception{
		super(map, container);
		category = FclRecipe.getCategoryIdAt(container.pos.x);
		key = FclRecipe.getResultKey(category, container.pos.y);
		results = FclRecipe.getCategoryAt(container.pos.x).get(key);
	}

	@Override
	public void init(){
		//
	}

	@Override
	public void predraw(float ticks, int mx, int my){
		//
	}

	@Override
	public boolean onAction(UIButton button, String id, int x, int y, int b){
		if(id.equals("prev")){
			selrec--;
			if(selrec < 0) selrec = 0;
			return true;
		}
		else if(id.equals("next")){
			selrec++;
			return true;
		}
		else if(id.startsWith("entry_")){
			//
			return true;
		}
		return false;
	}

	@Override
	public void getTooltip(int mx, int my, List<String> list){
		//
	}

	@Override
	public boolean keytyped(char c, int code){
		if(code == 1){
			TagCW com = TagCW.create();
			com.set("cat", category);
			SEND_TO_SERVER.accept(com);
			return true;
		}
		return false;
	}

}
