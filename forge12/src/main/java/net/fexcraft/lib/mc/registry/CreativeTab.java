package net.fexcraft.lib.mc.registry;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {
	
	private static final Set<CreativeTab> tabs = new HashSet<CreativeTab>();
	private String item;
	private short meta;

	public CreativeTab(String label){
		this(label, "minecraft:snowball", 0);
	}
	
	public CreativeTab(String label, String item){
		this(label, item, 0);
	}

	public CreativeTab(String label, String item, int i){
		super(label);
		this.item = item;
		this.meta = (short)i;
		tabs.add(this);
	}

	@Override
	public ItemStack createIcon(){
		return new ItemStack(Item.getByNameOrId(item), 1, meta);
	}
	
	public static void getIcons(){
		for(CreativeTab tab : tabs){
			tab.createIcon();
		}
	}
	
}