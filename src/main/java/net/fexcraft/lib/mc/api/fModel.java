package net.fexcraft.lib.mc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.fexcraft.lib.mc.render.ModelType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface fModel {
	
	public String name();
	
	public ModelType type();
	
}