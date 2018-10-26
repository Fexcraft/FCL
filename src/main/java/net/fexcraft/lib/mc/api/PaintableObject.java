package net.fexcraft.lib.mc.api;

import net.fexcraft.lib.common.math.RGB;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PaintableObject {
	
	public void onPaintItemUse(RGB color, EnumDyeColor dye, ItemStack stack, EntityPlayer player, BlockPos pos, World world);
	
}