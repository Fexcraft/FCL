package net.fexcraft.lib.mc.api;

import net.fexcraft.lib.common.math.RGB;
import net.minecraft.item.EnumDyeColor;

//TODO replace with capability
@Deprecated
public interface PaintItem {
	
	public EnumDyeColor getColor();
	
	public RGB getRGBColor();
	
}