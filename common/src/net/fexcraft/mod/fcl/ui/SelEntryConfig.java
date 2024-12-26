package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.mod.uni.ConfigBase;
import net.fexcraft.mod.uni.ConfigBase.ConfigEntry;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.ArrayList;
import java.util.List;

import static net.fexcraft.lib.common.utils.Formatter.PARAGRAPH_SIGN;
import static net.fexcraft.mod.uni.ui.ContainerInterface.*;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class SelEntryConfig extends UserInterface {

	private ConfigBase config;
	private String category;
	private ArrayList<ConfigEntry> entries;
	private int scroll;

	public SelEntryConfig(JsonMap map, ContainerInterface container) throws Exception{
		super(map, container);
	}

	@Override
	public void init(){
		config = ((ManageConfigCon)container).configs.get(container.pos.x);
		category = config.getCategoryByIndex(container.pos.y);
		entries = config.getCategories().get(category);
		texts.get("title").value(config.name() + " / " + category);
	}

	@Override
	public void predraw(float ticks, int mx, int my){
		if(entries == null) return;
		for(int i = 0; i < 6; i++){
			int j = scroll + i;
			if(j >= entries.size()) buttons.get("entry_" + i).text.value("");
			else buttons.get("entry_" + i).text.value(entries.get(j).key);
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
			if(idx < 0 || idx >= entries.size()) return true;
			TagCW com = TagCW.create();
			com.set("mod", config.name());
			com.set("cat", category);
			com.set("ent", entries.get(idx).key);
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
			if(j < entries.size() && buttons.get("entry_" + i).hovered()){
				ConfigEntry entry = entries.get(j);
				list.add(PARAGRAPH_SIGN + "6" + category + "/" + entry.key);
				list.add(PARAGRAPH_SIGN + "e" + entry.info());
				list.add(TRANSFORMAT.apply("ui.fcl.config.value", new Object[]{ asString(entry.value()) }));
				list.add(TRANSFORMAT.apply("ui.fcl.config.default", new Object[]{ asString(entry.initial()) }));
				if(entry.min() != 0f || entry.max() != 0f){
					list.add(TRANSFORMAT.apply("ui.fcl.config.range", new Object[]{ RGB.df.format(entry.min()), RGB.df.format(entry.max()) }));
				}
				if(entry.reqLevelRestart() == null){
					list.add(TRANSLATOR.apply("ui.fcl.config.no_req_info"));
				}
				else if(entry.reqLevelRestart()){
					list.add(TRANSLATOR.apply("ui.fcl.config.req_level_restart0"));
					list.add(TRANSLATOR.apply("ui.fcl.config.req_level_restart1"));
				}
				else if(entry.reqGameRestart()){
					list.add(TRANSLATOR.apply("ui.fcl.config.req_game_restart"));
				}
				else{
					list.add(TRANSLATOR.apply("ui.fcl.config.no_req_restart"));
					list.add(TRANSLATOR.apply("ui.fcl.config.no_req_restart_info"));
				}
			}
		}
	}

	private String asString(JsonValue value){
		if(value.isMap()) return "{ Json Object }";
		if(value.isArray()) return "[ Json Array ]";
		return value.string_value();
	}

	@Override
	public boolean keytyped(char c, int code){
		if(code == 1){
			TagCW com = TagCW.create();
			com.set("mod", config.name());
			SEND_TO_SERVER.accept(com);
			return true;
		}
		return false;
	}

}
