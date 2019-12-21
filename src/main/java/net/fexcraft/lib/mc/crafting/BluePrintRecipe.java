package net.fexcraft.lib.mc.crafting;

import net.minecraft.item.ItemStack;

@Deprecated
public class BluePrintRecipe {
	
	public BluePrintRecipe(String category, ItemStack stack, ItemStack[] components){
		this.category = category;
		this.output = stack;
		this.components = components;
	}

	public String category;
	public ItemStack[] components;
	public ItemStack output;
	
}