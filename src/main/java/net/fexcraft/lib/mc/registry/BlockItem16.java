package net.fexcraft.lib.mc.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockItem16 extends BlockItem {
	
	private int burn_time;
	
	public BlockItem16(Block block, Item.Settings settings){
		super(block, settings);
	}

	//@Override
	public int getMetadata(int meta){
		return meta;
	}
	
	public int getItemBurnTime(ItemStack stack){
        return burn_time;
    }

	public void setItemBurnTime(int burn_time){
		if(burn_time < -1){ burn_time = -1; }
		this.burn_time = burn_time;
	}
	
}