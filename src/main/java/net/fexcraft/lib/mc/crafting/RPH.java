package net.fexcraft.lib.mc.crafting;

import net.fexcraft.lib.mc.api.registry.fRecipeHolder;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

@fRecipeHolder("fcl")
public class RPH {
	
	public RPH(){
		RecipeRegistry.addShapedRecipe("fcl:blueprinttable", null, new ItemStack(FCLRegistry.getBlock("fcl:blueprinttable"), 1), 3, 2, new Ingredient[]{
			Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)), Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)), Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)),
			RecipeRegistry.INGREDIENT_LOG, Ingredient.fromStacks(new ItemStack(Blocks.CRAFTING_TABLE)), RecipeRegistry.INGREDIENT_LOG
		});
	}
	
}