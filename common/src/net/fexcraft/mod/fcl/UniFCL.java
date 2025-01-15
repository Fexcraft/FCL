package net.fexcraft.mod.fcl;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.fcl.ui.*;
import net.fexcraft.mod.uni.ConfigBase;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.ui.UIKey;
import net.fexcraft.mod.uni.world.MessageSender;

import java.io.File;
import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniFCL extends ConfigBase {

	public static UIKey SELECT_CONFIG = new UIKey(100, "fcl:sel_cfg");
	public static UIKey SELECT_CONFIG_CATEGORY = new UIKey(101, "fcl:sel_cfg_cat");
	public static UIKey SELECT_CONFIG_ENTRY = new UIKey(102, "fcl:sel_cfg_entry");
	public static UIKey EDIT_CONFIG = new UIKey(103, "fcl:edit_cfg");
	//
	public static UIKey SELECT_RECIPE_CATEGORY = new UIKey(104, "fcl:sel_rec_cat");
	public static UIKey SELECT_RECIPE_RESULT = new UIKey(105, "fcl:sel_rec_res");
	public static UIKey RECIPE_CRAFTING = new UIKey(106, "fcl:craft_recipe");
	//
	public static UUID NULL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	public static boolean EXAMPLE_RECIPES;

	public UniFCL(File configdir) {
		super(new File(configdir, "/fcl.json"), "Fexcraft Common Library");
	}

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

	@Override
	protected void fillInfo(JsonMap map){
		map.add("info", "FCL (Fexcraft Common Library) Config File");
	}

	@Override
	protected void fillEntries(){
		entries.add(new ConfigEntry(this, "general", "example_recipes", true)
			.cons((entry, map) -> EXAMPLE_RECIPES = entry.getBoolean(map))
			.info("Should FCL testing/example recipes in the Crafting block be enabled?")
			.req(false, true)
		);
	}

	@Override
	protected void onReload(JsonMap map){
		//
	}

	public static final MessageSender LOG = new MessageSender(){

		@Override
		public void send(String s){
			FCL.LOGGER.info(s);
		}

		@Override
		public void send(String str, Object... args){
			FCL.LOGGER.info(str, args);
		}

		@Override
		public void bar(String s){
			FCL.LOGGER.info(s);
		}

		@Override
		public void bar(String str, Object... args){
			FCL.LOGGER.info(str, args);
		}

		@Override
		public void dismount(){
			//
		}

		@Override
		public String getName(){
			return "LOG";
		}

		@Override
		public UUID getUUID(){
			return NULL_UUID;
		}

	};

}
