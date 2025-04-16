package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.inv.ItemWrapper;
import net.minecraft.item.Item;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class IWI extends ItemWrapper {

	public Item item;

	public IWI(Item item){
		this.item = item;
	}

	@Override
	public Item local(){
		return item;
	}

	@Override
	public Object direct(){
		return item;
	}

}
