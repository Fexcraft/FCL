package net.fexcraft.mod.lib.api.item;

import net.fexcraft.mod.lib.util.render.RGB;
import net.minecraft.item.EnumDyeColor;

public interface PaintItem {
	
	public EnumDyeColor getColor();
	
	public RGB getRGBColor();
	
}