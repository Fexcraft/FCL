package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.ConfigBase;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.ArrayList;
import java.util.List;

import static net.fexcraft.lib.common.utils.Formatter.PARAGRAPH_SIGN;
import static net.fexcraft.mod.uni.ui.ContainerInterface.SEND_TO_SERVER;
import static net.fexcraft.mod.uni.ui.ContainerInterface.TRANSFORMAT;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class SelCatConfig extends UserInterface {

	private ConfigBase config;
	private ArrayList<String> cats;
	private int scroll;

	public SelCatConfig(JsonMap map, ContainerInterface container) throws Exception{
		super(map, container);
	}

	@Override
	public void init(){
		config = ((ManageConfigCon)container).configs.get(container.pos.x);
		cats = new ArrayList<>(config.getCategories().keySet());
		texts.get("title").value("Categories: " + config.name());
	}

	@Override
	public void predraw(float ticks, int mx, int my){
		if(cats == null) return;
		for(int i = 0; i < 6; i++){
			int j = scroll + i;
			if(j >= cats.size()) buttons.get("entry_" + i).text.value("");
			else buttons.get("entry_" + i).text.value(cats.get(j));
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
			if(idx < 0 || idx >= cats.size()) return true;
			TagCW com = TagCW.create();
			com.set("mod", config.name());
			com.set("cat", cats.get(idx));
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
		for(int i = 0; i < 6; i++){
			int j = scroll + i;
			if(j < cats.size() && buttons.get("entry_" + i).hovered()){
				list.add(TRANSFORMAT.apply("ui.fcl.config.entries", new Object[]{ config.getCategoryEntries(j).size() }));
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
