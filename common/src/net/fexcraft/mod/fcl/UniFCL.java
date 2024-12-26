package net.fexcraft.mod.fcl;

import net.fexcraft.mod.fcl.ui.ManageConfigCon;
import net.fexcraft.mod.fcl.ui.SelEntryConfig;
import net.fexcraft.mod.fcl.ui.SelectConfig;
import net.fexcraft.mod.fcl.ui.SelCatConfig;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.ui.UIKey;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniFCL {

	public static UIKey SELECT_CONFIG = new UIKey(100, "fcl:sel_cfg");
	public static UIKey SELECT_CONFIG_CATEGORY = new UIKey(101, "fcl:sel_cfg_cat");
	public static UIKey SELECT_CONFIG_ENTRY = new UIKey(102, "fcl:sel_cfg_entry");
	public static UIKey EDIT_CONFIG = new UIKey(103, "fcl:edit_cfg");

	public static void registerUI(Object instance){
		UniReg.registerMod("fcl", instance);
		UniReg.registerUI(UniFCL.SELECT_CONFIG, SelectConfig.class);
		UniReg.registerMenu(UniFCL.SELECT_CONFIG, "fcl:uis/select_config", ManageConfigCon.class);
		UniReg.registerUI(UniFCL.SELECT_CONFIG_CATEGORY, SelCatConfig.class);
		UniReg.registerMenu(UniFCL.SELECT_CONFIG_CATEGORY, "fcl:uis/select_config", ManageConfigCon.class);
		UniReg.registerUI(UniFCL.SELECT_CONFIG_ENTRY, SelEntryConfig.class);
		UniReg.registerMenu(UniFCL.SELECT_CONFIG_ENTRY, "fcl:uis/select_config", ManageConfigCon.class);
	}

}
