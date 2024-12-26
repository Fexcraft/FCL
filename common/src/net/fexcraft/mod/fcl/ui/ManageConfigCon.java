package net.fexcraft.mod.fcl.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.ConfigBase;
import net.fexcraft.mod.uni.ConfigBase.ConfigEntry;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UserInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static net.fexcraft.mod.uni.ui.ContainerInterface.SEND_TO_SERVER;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ManageConfigCon extends ContainerInterface {

	protected ArrayList<ConfigBase> configs;

	public ManageConfigCon(JsonMap map, UniEntity ply, V3I pos){
		super(map, ply, pos);
		configs = new ArrayList<>();
		configs.addAll(ConfigBase.CONFIGS.values());
	}

	@Override
	public void init(){
		//
	}

	@Override
	public void packet(TagCW com, boolean client){
		if(com.has("main")){
			player.entity.openUI(UniFCL.SELECT_CONFIG, V3I.NULL);
		}
		if(com.has("mod")){
			ConfigBase config = null;
			for(ConfigBase base : configs){
				if(base.name().equals(com.getString("mod"))){
					config = base;
					break;
				}
			}
			if(config == null) return;
			if(com.has("cat")){
				int idx = config.getCategoryIndex(com.getString("cat"));
				if(idx < 0) return;
				if(com.has("ent")){
					ConfigEntry entry = config.getEntry(com.getString("cat"), com.getString("ent"));
					if(entry == null) return;
					player.entity.openUI(UniFCL.EDIT_CONFIG, new V3I(configs.indexOf(config), idx, config.getCategories().get(com.getString("cat")).indexOf(entry.key)));
				}
				else{
					player.entity.openUI(UniFCL.SELECT_CONFIG_ENTRY, new V3I(configs.indexOf(config), idx, 0));
				}
			}
			else{
				player.entity.openUI(UniFCL.SELECT_CONFIG_CATEGORY, new V3I(configs.indexOf(config), 0, 0));
			}
			return;
		}
	}

}
