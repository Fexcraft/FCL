package net.fexcraft.mod.lib.util.registry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlock16 extends ItemBlock {
	
	public ItemBlock16(Block block){
		super(block);
	}

	@Override
	public int getMetadata(int meta){
		return meta;
	}
	
}