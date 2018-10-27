package net.fexcraft.lib.mc.crafting;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import net.fexcraft.lib.common.lang.ArrayList;
import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.PacketHandler.PacketHandlerType;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.entity.player.EntityPlayer;
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
		FCLRegistry.getAutoRegisterer(rs.getResourceDomain()).addRecipe(rs, new ShapelessRecipes(string == null ? "" : string, output, list));
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
		FCLRegistry.getAutoRegisterer(rs.getResourceDomain()).addRecipe(rs, new ShapedRecipes(string == null ? "" : string, width, height, list, output));
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
		workbench = new WorkBench(); blueprinttable = new BluePrintTable();
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
	
}