package net.fexcraft.mod.uni.item;

import net.fexcraft.mod.uni.Appendable;
import net.fexcraft.mod.uni.Appended;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniStack {

	public static Function<Object, UniStack> GETTER = null;
	public static Function<Object, StackWrapper> STACK_GETTER = null;
	private static ArrayList<Appendable<UniStack>> appendables = new ArrayList<>();
	public final Appended<UniStack> appended = new Appended(this);
	public StackWrapper stack;

	public UniStack(){}

	public UniStack set(Object ent){
		stack = STACK_GETTER.apply(ent);
		appended.init(appendables);
		return this;
	}

	public static UniStack get(Object stack){
		return GETTER.apply(stack);
	}

	public <A> A getApp(Class<A> clazz){
		return appended.get(clazz);
	}

	public <A> A getApp(String id){
		return appended.get(id);
	}

	public static <A> A getApp(Object stack, Class<A> clazz){
		UniStack uni = GETTER.apply(stack);
		return stack == null ? null : uni.getApp(clazz);
	}

	public static StackWrapper getStack(Object stack){
		if(GETTER == null || stack instanceof ItemWrapper) return STACK_GETTER.apply(stack);
		UniStack uni = GETTER.apply(stack);
		if(uni == null) return STACK_GETTER.apply(stack);//happens on init before capabilities are functional
		return GETTER.apply(stack).stack;
	}

	public static void register(Appendable<UniStack> app){
		appendables.add(app);
	}

}
