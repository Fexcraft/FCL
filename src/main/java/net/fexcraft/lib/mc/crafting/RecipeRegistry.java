package net.fexcraft.lib.mc.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.item.ItemStack;

public class RecipeRegistry {
	
	private static final TreeMap<String, TreeMap<String, List<BluePrintRecipe>>> RECIPES = new TreeMap<String, TreeMap<String, List<BluePrintRecipe>>>();
	
	public static final void addBluePrintRecipe(String category, ItemStack stack, ItemStack... recipeComponents){
		if(!RECIPES.containsKey(category)){
			RECIPES.put(category, new TreeMap<>());
			//Print.debug("[BPT] Created Category: " + category);
		}
		String reg = stack.getTranslationKey();
		if(!RECIPES.get(category).containsKey(reg)){
			//Print.log("[BPT] Created Stack: " + reg.toString());
			RECIPES.get(category).put(reg, new ArrayList<BluePrintRecipe>());
		}
		if(RECIPES.get(category).get(reg) == null){
			//Print.debug("[BPT] Fixing... " + reg.toString());
			RECIPES.get(category).put(reg, new ArrayList<BluePrintRecipe>());
		}
		//Print.debug("[BPT] Registering Recipe for Stack: " + stack.toString());
		RECIPES.get(category).get(reg).add(new BluePrintRecipe(category, stack, recipeComponents));
	}
	
	//
	
	public static List<BluePrintRecipe> getRecipes(int x, int y){
		TreeMap<String, List<BluePrintRecipe>> cat = getRecipes(x);
		//ItemStackComparable stack = cat == null ? null : (ItemStackComparable)cat.keySet().toArray()[y];
		//return stack == null ? null : cat.get(stack);
		return cat == null || y >= cat.values().size() ? null : (List<BluePrintRecipe>)cat.values().toArray()[y];
	}
	
	public static TreeMap<String, List<BluePrintRecipe>> getRecipes(String category){
		return category == null ? null : RECIPES.get(category);
	}

	public static TreeMap<String, List<BluePrintRecipe>> getRecipes(int x){
		String key = (String)RECIPES.keySet().toArray()[x];
		return RECIPES.get(key);
	}
	
	public static Set<String> getCategories(){
		return RECIPES.keySet();
	}

	public static String getCategory(int x){
		return (String)RECIPES.keySet().toArray()[x];
	}

	public static String getCategory(int x, int y){
		return (String)getRecipes(x).keySet().toArray()[y];
	}
	
	public static List<BluePrintRecipe> getRecipes(String category, ItemStack stack){
		TreeMap<String, List<BluePrintRecipe>> cat = RECIPES.get(category == null ? "" : category);
		return cat == null ? null : cat.get(stack.getTranslationKey());
	}
	
}