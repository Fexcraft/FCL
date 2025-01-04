package net.fexcraft.mod.uni;

import com.google.common.collect.ImmutableList;
import net.fexcraft.mod.uni.item.StackWrapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FclRecipe {

	public static LinkedHashMap<String, LinkedHashMap<IDL, ArrayList<FclRecipe>>> RECIPES = new LinkedHashMap<>();
	public static BiFunction<StackWrapper, StackWrapper, Boolean> EQUALS;
	public static Function<Component, Boolean> VALIDATE;
	public static Function<Component, List<StackWrapper>> GET_TAG_AS_LIST;
	//
	public final StackWrapper output;
	public final List<Component> components;

	public FclRecipe(StackWrapper stack, Component... comps){
		output = stack;
		components = ImmutableList.copyOf(comps);
	}

	public FclRecipe(Object stack, Component... comps){
		output = StackWrapper.wrap(stack);
		components = ImmutableList.copyOf(comps);
	}

	public static void register(String category, FclRecipe recipe){
		if(category.length() == 0) return;
		if(!RECIPES.containsKey(category)) RECIPES.put(category, new LinkedHashMap<>());
		if(!RECIPES.get(category).containsKey(recipe.output.getIDL())){
			RECIPES.get(category).put(recipe.output.getIDL(), new ArrayList<>());
		}
		if(recipe.output.empty()) return;//TODO error logging
		for(Component comp : recipe.components){
			if(!VALIDATE.apply(comp)) return;//TODO error logging
		}
		RECIPES.get(category).get(recipe.output.getIDL()).add(recipe);
	}

	public void register(String category){
		register(category, this);
	}

	public static int indexOfCategory(String cat){
		int idx = 0;
		for (String key : RECIPES.keySet()) {
			if(key.equals(cat)) return idx;
			idx++;
		}
		return -1;
	}

	public static LinkedHashMap<IDL, ArrayList<FclRecipe>> getCategoryAt(int cat){
		int idx = 0;
		for(String key : RECIPES.keySet()) {
			if(idx == cat) return RECIPES.get(key);
			idx++;
		}
		return null;
	}

	public static String getCategoryIdAt(int cat){
		int idx = 0;
		for(String key : RECIPES.keySet()) {
			if(idx == cat) return key;
			idx++;
		}
		return null;
	}

	public static IDL getResultKey(String category, int kid){
		LinkedHashMap<IDL, ArrayList<FclRecipe>> results = RECIPES.get(category);
		int idx = 0;
		for(IDL key : results.keySet()){
			if(idx == kid) return key;
			idx++;
		}
		return null;
	}

	public static int getResultIdx(String category, String res){
		LinkedHashMap<IDL, ArrayList<FclRecipe>> results = RECIPES.get(category);
		int idx = 0;
		for(IDL key : results.keySet()){
			if(key.colon().equals(res)) return idx;
			idx++;
		}
		return -1;
	}

	public static Builder newBuilder(String cat){
		return new Builder(cat);
	}

	public static class Builder {

		private ArrayList<Component> comps = new ArrayList<>();
		private StackWrapper stack;
		private String category;

		public Builder(String cat){
			category = cat;
		}

		public Builder output(StackWrapper stack){
			this.stack = stack;
			return this;
		}

		public Builder output(Object stack){
			this.stack = StackWrapper.wrap(stack);
			return this;
		}

		public Builder add(Object stack){
			comps.add(new Component(stack));
			return this;
		}

		public Builder add(Component comp){
			comps.add(comp);
			return this;
		}

		public Builder add(String tag, int am){
			comps.add(new Component(tag, am));
			return this;
		}

		public void register(){
			FclRecipe.register(category, new FclRecipe(stack, comps.toArray(new Component[0])));
		}

	}

	public static class Component {

		public final boolean tag;
		public final String id;
		public final int amount;
		public final StackWrapper stack;
		public List<StackWrapper> list;

		public Component(String tagid, int am){
			tag = true;
			id = tagid;
			amount = am;
			stack = null;
		}

		public Component(StackWrapper stack){
			this.stack = stack;
			amount = stack.count();
			tag = false;
			id = null;
		}

		public Component(Object stack){
			this.stack = StackWrapper.wrap(stack);
			amount = this.stack.count();
			tag = false;
			id = null;
		}

		public void refreshList(){
			list = GET_TAG_AS_LIST.apply(this);
		}

	}

}