package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.item.ItemWrapper;
import net.minecraft.world.item.Item;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class IWI extends ItemWrapper {

	public Item item;

	public IWI(Item item){
		this.item = item;
	}

	public void linkContainer(){}

	public void regToDict(){}

	public Item local(){
		return this.item;
	}

	public Object direct(){
		return this.item;
	}

}