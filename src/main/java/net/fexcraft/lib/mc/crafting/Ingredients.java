package net.fexcraft.lib.mc.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Ingredients {
	
	public static final Ingredient INGREDIENT_DYE = Ingredient.fromStacks(
		new ItemStack(Items.DYE, 1, 0),
		new ItemStack(Items.DYE, 1, 1),
		new ItemStack(Items.DYE, 1, 2),
		new ItemStack(Items.DYE, 1, 3),
		new ItemStack(Items.DYE, 1, 4),
		new ItemStack(Items.DYE, 1, 5),
		new ItemStack(Items.DYE, 1, 6),
		new ItemStack(Items.DYE, 1, 7),
		new ItemStack(Items.DYE, 1, 8),
		new ItemStack(Items.DYE, 1, 9),
		new ItemStack(Items.DYE, 1, 10),
		new ItemStack(Items.DYE, 1, 11),
		new ItemStack(Items.DYE, 1, 12),
		new ItemStack(Items.DYE, 1, 13),
		new ItemStack(Items.DYE, 1, 14),
		new ItemStack(Items.DYE, 1, 15)
	);
	public static final Ingredient INGREDIENT_WOOL = Ingredient.fromStacks(
		new ItemStack(Blocks.WOOL, 1, 0),
		new ItemStack(Blocks.WOOL, 1, 1),
		new ItemStack(Blocks.WOOL, 1, 2),
		new ItemStack(Blocks.WOOL, 1, 3),
		new ItemStack(Blocks.WOOL, 1, 4),
		new ItemStack(Blocks.WOOL, 1, 5),
		new ItemStack(Blocks.WOOL, 1, 6),
		new ItemStack(Blocks.WOOL, 1, 7),
		new ItemStack(Blocks.WOOL, 1, 8),
		new ItemStack(Blocks.WOOL, 1, 9),
		new ItemStack(Blocks.WOOL, 1, 10),
		new ItemStack(Blocks.WOOL, 1, 11),
		new ItemStack(Blocks.WOOL, 1, 12),
		new ItemStack(Blocks.WOOL, 1, 13),
		new ItemStack(Blocks.WOOL, 1, 14),
		new ItemStack(Blocks.WOOL, 1, 15)
	);
	public static final Ingredient INGREDIENT_WOOL_0 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 0));
	public static final Ingredient INGREDIENT_WOOL_1 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 1));
	public static final Ingredient INGREDIENT_WOOL_2 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 2));
	public static final Ingredient INGREDIENT_WOOL_3 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 3));
	public static final Ingredient INGREDIENT_WOOL_4 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 4));
	public static final Ingredient INGREDIENT_WOOL_5 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 5));
	public static final Ingredient INGREDIENT_WOOL_6 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 6));
	public static final Ingredient INGREDIENT_WOOL_7 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 7));
	public static final Ingredient INGREDIENT_WOOL_8 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 8));
	public static final Ingredient INGREDIENT_WOOL_9 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 9));
	public static final Ingredient INGREDIENT_WOOL_10 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 10));
	public static final Ingredient INGREDIENT_WOOL_11 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 11));
	public static final Ingredient INGREDIENT_WOOL_12 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 12));
	public static final Ingredient INGREDIENT_WOOL_13 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 13));
	public static final Ingredient INGREDIENT_WOOL_14 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 14));
	public static final Ingredient INGREDIENT_WOOL_15 = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 15));
	public static final Ingredient INGREDIENT_NULL = Ingredient.EMPTY;
	public static final Ingredient INGREDIENT_PLANKS = Ingredient.fromStacks(
		new ItemStack(Blocks.PLANKS, 1, 0),
		new ItemStack(Blocks.PLANKS, 1, 1),
		new ItemStack(Blocks.PLANKS, 1, 2),
		new ItemStack(Blocks.PLANKS, 1, 3),
		new ItemStack(Blocks.PLANKS, 1, 4),
		new ItemStack(Blocks.PLANKS, 1, 5)
	);
	public static final Ingredient INGREDIENT_LOG = Ingredient.fromStacks(
		new ItemStack(Blocks.LOG, 1, 0),
		new ItemStack(Blocks.LOG, 1, 1),
		new ItemStack(Blocks.LOG, 1, 2),
		new ItemStack(Blocks.LOG, 1, 3),
		new ItemStack(Blocks.LOG2, 1, 0),
		new ItemStack(Blocks.LOG2, 1, 1)
	);
	public static final Ingredient INGREDIENT_STONE = Ingredient.fromStacks(
		new ItemStack(Blocks.STONE, 1, 0),
		new ItemStack(Blocks.STONE, 1, 1),
		new ItemStack(Blocks.STONE, 1, 2),
		new ItemStack(Blocks.STONE, 1, 3),
		new ItemStack(Blocks.STONE, 1, 4),
		new ItemStack(Blocks.STONE, 1, 5),
		new ItemStack(Blocks.STONE, 1, 6)
	);
	public static final Ingredient INGREDIENT_REDSTONE = Ingredient.fromStacks(new ItemStack(Items.REDSTONE, 1, 0));
	public static final Ingredient INGREDIENT_IRON_INGOT = Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT, 1, 0));
	public static final Ingredient INGREDIENT_GOLD_INGOT = Ingredient.fromStacks(new ItemStack(Items.GOLD_INGOT, 1, 0));
	public static final Ingredient INGREDIENT_GOLD_NUGGET = Ingredient.fromStacks(new ItemStack(Items.GOLD_NUGGET, 1, 0));
	public static final Ingredient INGREDIENT_SLIME_BALL = Ingredient.fromStacks(new ItemStack(Items.SLIME_BALL, 1, 0));
	
	public static Ingredient ing(){
		return Ingredient.EMPTY;
	}
	
	public static Ingredient ing(Item item){
		return Ingredient.fromStacks(new ItemStack(item, 1, 0));
	}
	
	public static Ingredient ing(Item item, int a){
		return Ingredient.fromStacks(new ItemStack(item, a, 0));
	}
	
	public static Ingredient ing(Item item, int a, int m){
		return Ingredient.fromStacks(new ItemStack(item, a, m));
	}
	
	public static Ingredient ing(Block block){
		return Ingredient.fromStacks(new ItemStack(block, 1, 0));
	}
	
	public static Ingredient ing(Block block, int a){
		return Ingredient.fromStacks(new ItemStack(block, a, 0));
	}
	
	public static Ingredient ing(Block block, int a, int m){
		return Ingredient.fromStacks(new ItemStack(block, a, m));
	}
	
}