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
	public static BiFunction<Component, StackWrapper, Boolean> IS_IN_TAG;
	public static BiFunction<StackWrapper, StackWrapper, Boolean> EQUALS;
	public static Function<Component, Boolean> VALIDATE;
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

		public boolean equals(StackWrapper other){
			if(tag) return IS_IN_TAG.apply(this, other);
			else return EQUALS.apply(stack, other);
		}

	}

}