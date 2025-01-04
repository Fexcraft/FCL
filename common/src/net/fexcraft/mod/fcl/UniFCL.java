package net.fexcraft.mod.fcl;

import net.fexcraft.mod.fcl.ui.*;
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
	//
	public static UIKey SELECT_RECIPE_CATEGORY = new UIKey(104, "fcl:sel_rec_cat");
	public static UIKey SELECT_RECIPE_RESULT = new UIKey(105, "fcl:sel_rec_res");
	public static UIKey RECIPE_CRAFTING = new UIKey(106, "fcl:craft_recipe");

	public static void registerUI(Object instance){
		UniReg.registerMod("fcl", instance);
		UniReg.registerUI(UniFCL.SELECT_CONFIG, SelModConfig.class);
		UniReg.registerMenu(UniFCL.SELECT_CONFIG, "fcl:uis/select_config", ManageConfigCon.class);
		UniReg.registerUI(UniFCL.SELECT_CONFIG_CATEGORY, SelCatConfig.class);
		UniReg.registerMenu(UniFCL.SELECT_CONFIG_CATEGORY, "fcl:uis/select_config", ManageConfigCon.class);
		UniReg.registerUI(UniFCL.SELECT_CONFIG_ENTRY, SelEntryConfig.class);
		UniReg.registerMenu(UniFCL.SELECT_CONFIG_ENTRY, "fcl:uis/select_config", ManageConfigCon.class);
		//
		UniReg.registerUI(UniFCL.SELECT_RECIPE_CATEGORY, SelCatRecipe.class);
		UniReg.registerMenu(UniFCL.SELECT_RECIPE_CATEGORY, "fcl:uis/select_recipe", SelRecipeCon.class);
		UniReg.registerUI(UniFCL.SELECT_RECIPE_RESULT, SelResRecipe.class);
		UniReg.registerMenu(UniFCL.SELECT_RECIPE_RESULT, "fcl:uis/select_recipe", SelRecipeCon.class);
		UniReg.registerUI(UniFCL.RECIPE_CRAFTING, CraftRecipeUI.class);
		UniReg.registerMenu(UniFCL.RECIPE_CRAFTING, "fcl:uis/recipe_crafting", CraftRecipeCon.class);
	}

}
