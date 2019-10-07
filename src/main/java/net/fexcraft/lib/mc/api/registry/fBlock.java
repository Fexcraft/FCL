package net.fexcraft.lib.mc.api.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.fexcraft.lib.mc.registry.BlockItem16;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface fBlock {
	
	public String modid();
	
	public String name();
	
	public int variants() default 1;
	
	public String[] custom_variants() default {};
	
	public Class<? extends BlockItem> item() default BlockItem16.class;
	
	public Class<? extends BlockEntity> tileentity() default BlockEntity.class;

	public int burn_time() default -1;
	
}