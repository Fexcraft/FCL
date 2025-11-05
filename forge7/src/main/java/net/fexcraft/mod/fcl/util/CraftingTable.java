package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftingTable extends Block {
	
    public CraftingTable(){
    	super(Material.glass);
    	setHarvestLevel("axe", 1);
		setHardness(1.0F);
		setResistance(32.0F);
    	setCreativeTab(CreativeTabs.tabTools);
		setBlockName("fcl:crafting");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hx, float hy, float hz){
		if(!world.isRemote && !player.isSneaking()){
			UniEntity.getEntity(player).openUI(UniFCL.SELECT_RECIPE_CATEGORY, x, y, z);
			return true;
		}
		return false;
	}
	
}