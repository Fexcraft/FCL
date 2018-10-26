package net.fexcraft.lib.mc.crafting;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import net.fexcraft.lib.common.lang.ArrayList;
import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.PacketHandler.PacketHandlerType;
import net.fexcraft.lib.mc.registry.RegistryUtil;
import net.fexcraft.lib.mc.util.Print;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

public class RecipeRegistry {
	
	//private static final TreeMap<String, ArrayList<BluePrintRecipe>> recipes = new TreeMap<String, ArrayList<BluePrintRecipe>>();
	private static final TreeMap<String, TreeMap<String, List<BluePrintRecipe>>> RECIPES = new TreeMap<String, TreeMap<String, List<BluePrintRecipe>>>();
	
	public static final void addBluePrintRecipe(String category, ItemStack stack, ItemStack... recipeComponents){
		if(!RECIPES.containsKey(category)){
			RECIPES.put(category, new TreeMap<String, List<BluePrintRecipe>>());
			Print.debug("[BPT] Created Category: " + category);
		}
		String reg = stack.getUnlocalizedName();
		if(!RECIPES.get(category).containsKey(reg)){
			Print.debug("[BPT] Created Stack: " + reg.toString());
			RECIPES.get(category).put(reg, new ArrayList<BluePrintRecipe>());
		}
		if(RECIPES.get(category).get(reg) == null){
			Print.debug("[BPT] Fixing... " + reg.toString());
			RECIPES.get(category).put(reg, new ArrayList<BluePrintRecipe>());
		}
		Print.debug("[BPT] Registering Recipe for Stack: " + stack.toString());
		RECIPES.get(category).get(reg).add(new BluePrintRecipe(category, stack, recipeComponents));
	}
	
	public static void addShapelessRecipe(String rs, String string, ItemStack output, Ingredient... ingredients){
		if(rs == null){
			return;
		}
		addShapelessRecipe(new ResourceLocation(rs), string, output, ingredients);
	}
	
	public static void addShapelessRecipe(ResourceLocation rs, String string, ItemStack output, Ingredient... ingredients){
		if(ingredients.length < 1 || rs == null){
			return;
		}
		NonNullList<Ingredient> list = NonNullList.<Ingredient>create();
		list.addAll(Arrays.asList(ingredients));
		//vrecipes.put(rs, new ShapelessRecipes(string == null ? "" : string, output, list));
		RegistryUtil.get(rs.getResourceDomain()).addRecipe(rs, new ShapelessRecipes(string == null ? "" : string, output, list));
	}

	public static void addShapedRecipe(String rs, String string, ItemStack output, int width, int height, Ingredient... ingredients){
		if(rs == null){
			return;
		}
		addShapedRecipe(new ResourceLocation(rs), string, output, width, height, ingredients);
	}
	
	public static void addShapedRecipe(ResourceLocation rs, String string, ItemStack output, int width, int height, Ingredient... ingredients){
		if(ingredients.length < 1 || rs == null){
			return;
		}
		NonNullList<Ingredient> list = NonNullList.<Ingredient>create();
		list.addAll(Arrays.asList(ingredients));
		//vrecipes.put(rs, new ShapedRecipes(string == null ? "" : string, width, height, list, output));
		RegistryUtil.get(rs.getResourceDomain()).addRecipe(rs, new ShapedRecipes(string == null ? "" : string, width, height, list, output));
	}
	
	public static class GuiHandler implements IGuiHandler {
		@Override
		public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
			switch(ID){
				case 1:
					return new BluePrintTableGui.Server(player, world, x, y, z);
				default: return null;
			}
		}
		@Override
		public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
			switch(ID){
				case 1:
					return new BluePrintTableGui.Client(player, world, x, y, z);
				default: return null;
			}
		}
	}

	public static WorkBench getWorkBench(){
		return workbench;
	}
	
	public static BluePrintTable getBluePrintTable(){
		return blueprinttable;
	}
	
	private static WorkBench workbench;
	private static BluePrintTable blueprinttable;

	public static void initialize(){
		workbench = new WorkBench();
		blueprinttable = new BluePrintTable();
		PacketHandler.registerListener(PacketHandlerType.NBT, Side.SERVER, new BluePrintTableGui.SRBTP());
	}
	
	public static List<BluePrintRecipe> getRecipes(String category, ItemStack stack){
		TreeMap<String, List<BluePrintRecipe>> cat = RECIPES.get(category == null ? "" : category);
		return cat == null ? null : cat.get(stack.getUnlocalizedName());
	}
	
	@SuppressWarnings("unchecked")
	public static List<BluePrintRecipe> getRecipes(int x, int y){
		TreeMap<String, List<BluePrintRecipe>> cat = getRecipes(x);
		//ItemStackComparable stack = cat == null ? null : (ItemStackComparable)cat.keySet().toArray()[y];
		//return stack == null ? null : cat.get(stack);
		return cat == null ? null : (List<BluePrintRecipe>)cat.values().toArray()[y];
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
	
	//
	
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