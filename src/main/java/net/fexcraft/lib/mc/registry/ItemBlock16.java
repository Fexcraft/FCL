package net.fexcraft.lib.mc.registry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlock16 extends net.minecraft.item.ItemBlock {
	
	private int burn_time;
	
	public ItemBlock16(Block block){
		super(block);
	}

	@Override
	public int getMetadata(int meta){
		return meta;
	}
	
	public int getItemBurnTime(ItemStack stack){
        return burn_time;
    }

	public void setItemBurnTime(int burn_time){
		if(burn_time < -1){
			burn_time = -1;
		}
		this.burn_time = burn_time;
	}
	
}