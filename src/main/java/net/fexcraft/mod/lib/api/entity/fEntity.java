package net.fexcraft.mod.lib.api.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface fEntity {
	
	public String modid();
	
	public String name();

	public int tracking_range() default 64;

	public int update_frequency() default 1;

	boolean send_velocity_updates() default true;
	
}