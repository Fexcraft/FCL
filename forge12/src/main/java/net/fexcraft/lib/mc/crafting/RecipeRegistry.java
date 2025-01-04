package net.fexcraft.lib.mc.crafting;

import java.util.Arrays;

import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class RecipeRegistry {

	@Deprecated
	public static void addBluePrintRecipe(String category, ItemStack stack, ItemStack... comps){}
	
	public static void addShapelessRecipe(String rs, String string, ItemStack output, Ingredient... ingredients){
		if(rs == null){ return; } addShapelessRecipe(new ResourceLocation(rs), string, output, ingredients);
	}
	
	public static void addShapelessRecipe(ResourceLocation rs, String string, ItemStack output, Ingredient... ingredients){
		if(ingredients.length < 1 || rs == null){ return; }
		NonNullList<Ingredient> list = NonNullList.<Ingredient>create(); list.addAll(Arrays.asList(ingredients));
		FCLRegistry.getAutoRegisterer(rs.getNamespace()).addRecipe(rs, new ShapelessRecipes(string == null ? "" : string, output, list));
	}

	public static void addShapedRecipe(String rs, String string, ItemStack output, int width, int height, Ingredient... ingredients){
		if(rs == null){ return; } addShapedRecipe(new ResourceLocation(rs), string, output, width, height, ingredients);
	}
	
	public static void addShapedRecipe(ResourceLocation rs, String string, ItemStack output, int width, int height, Ingredient... ingredients){
		if(ingredients.length < 1 || rs == null){ return; }
		NonNullList<Ingredient> list = NonNullList.create();
		list.addAll(Arrays.asList(ingredients));
		FCLRegistry.getAutoRegisterer(rs.getNamespace()).addRecipe(rs, new ShapedRecipes(string == null ? "" : string, width, height, list, output));
	}
	
}