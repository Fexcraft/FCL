package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.common.utils.Formatter;
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
public class SelectConfig extends UserInterface {

	private ArrayList<ConfigBase> configs;
	private int scroll;

	public SelectConfig(JsonMap map, ContainerInterface container) throws Exception{
		super(map, container);
		configs = ((ManageConfigCon)container).configs;
	}

	@Override
	public void init(){
		//
	}

	@Override
	public void predraw(float ticks, int mx, int my){
		for(int i = 0; i < 6; i++){
			int j = scroll + i;
			if(j >= configs.size()) buttons.get("entry_" + i).text.value("");
			else buttons.get("entry_" + i).text.value(configs.get(j).name());
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
			if(idx < 0 || idx >= configs.size()) return true;
			TagCW com = TagCW.create();
			com.set("mod", configs.get(idx).name());
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
			if(j < configs.size() && buttons.get("entry_" + i).hovered()){
				list.add(PARAGRAPH_SIGN + "9" + configs.get(j).name());
				list.add(TRANSFORMAT.apply("ui.fcl.config.categories", new Object[]{ configs.get(j).getCategories().size() }));
				list.add(TRANSFORMAT.apply("ui.fcl.config.entries", new Object[]{ configs.get(j).getEntries().size() }));
			}
		}
	}

}
