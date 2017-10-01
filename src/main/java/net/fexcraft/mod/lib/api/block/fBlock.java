package net.fexcraft.mod.lib.api.block;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.fexcraft.mod.lib.util.registry.ItemBlock16;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface fBlock{
	
	public String modid();
	
	public String name();
	
	public int variants() default 1;
	
	public String[] custom_variants() default {};
	
	public Class<? extends ItemBlock> item() default ItemBlock16.class;
	
	public Class<? extends TileEntity> tileentity() default TileEntity.class;
	
}