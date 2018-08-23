package net.fexcraft.mod.lib.fmr;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface FCLItemModel {

	public void renderItem(TransformType type, ItemStack item, EntityLivingBase entity);

	public default void onResourceManagerReload(IResourceManager resmag){}
	
}