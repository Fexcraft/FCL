package net.fexcraft.lib.mc.crafting;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

public class FCLRecipeHolder {
	
	public FCLRecipeHolder(){
		RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.generic", new ItemStack(Blocks.OAK_PLANKS, 64), new ItemStack(Blocks.OAK_LOG, 16));
		RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.generic", new ItemStack(Blocks.BIRCH_PLANKS, 64), new ItemStack(Blocks.BIRCH_LOG, 16));
		RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.generic", new ItemStack(Blocks.SPRUCE_PLANKS, 64), new ItemStack(Blocks.SPRUCE_LOG, 16));
		RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.generic", new ItemStack(Blocks.JUNGLE_PLANKS, 64), new ItemStack(Blocks.JUNGLE_LOG, 16));
		RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.generic", new ItemStack(Blocks.ACACIA_PLANKS, 64), new ItemStack(Blocks.ACACIA_LOG, 16));
		RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.generic", new ItemStack(Blocks.DARK_OAK_PLANKS, 64), new ItemStack(Blocks.DARK_OAK_LOG, 16));
		RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.generic", new ItemStack(Blocks.COBBLESTONE_WALL, 64), new ItemStack(Blocks.COBBLESTONE, 64));
		RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.generic", new ItemStack(Blocks.COBBLESTONE_STAIRS, 64), new ItemStack(Blocks.COBBLESTONE, 64));
		RecipeRegistry.addBluePrintRecipe("gui.fcl.recipe.generic", new ItemStack(Blocks.COBBLESTONE_SLAB, 64), new ItemStack(Blocks.COBBLESTONE, 32));
	}
	
}