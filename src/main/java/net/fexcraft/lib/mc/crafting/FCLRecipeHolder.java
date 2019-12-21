package net.fexcraft.lib.mc.crafting;

import net.fexcraft.lib.mc.api.registry.fRecipeHolder;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.lib.mc.utils.Static;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

@fRecipeHolder("fcl")
public class FCLRecipeHolder {
	
	public FCLRecipeHolder(){
		RecipeRegistry.addShapedRecipe("fcl:blueprinttable", null, new ItemStack(FCLRegistry.getBlock("fcl:blueprinttable"), 1), 3, 2, new Ingredient[]{
			Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)), Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)), Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)),
			Ingredients.INGREDIENT_LOG, Ingredient.fromStacks(new ItemStack(Blocks.CRAFTING_TABLE)), Ingredients.INGREDIENT_LOG
		});
		if(Static.dev()){
			RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.testcat0", new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE, 4));
			RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.testcat0", new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE, 6));
			RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.testcat0", new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE, 8));
			RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.testcat1", new ItemStack(Blocks.STONE, 1, 1), new ItemStack(Blocks.COBBLESTONE, 8));
			RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.testcat1", new ItemStack(Blocks.STONE, 1, 2), new ItemStack(Blocks.COBBLESTONE, 8));
			RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.testcat1", new ItemStack(Blocks.STONE, 1, 3), new ItemStack(Blocks.COBBLESTONE, 8));
			RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.testcat2", new ItemStack(Blocks.PLANKS, 64), new ItemStack(Blocks.LOG, 4));
			//
			ItemStack[] con = new ItemStack[64];
			for(int i = 0; i < con.length; i++){ con[i] = new ItemStack(i % 3 == 0 ? Items.GOLDEN_APPLE : Items.APPLE); }
			RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.testcat2", new ItemStack(Blocks.ANVIL, 1), con);
			//
			//for(int i = 0; i < 7; i ++ ){ RecipeRegistry.addBluePrintRecipe("TestCategory0" + i, new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.DIRT, 32)); }
		}
	}
	
}