package net.fexcraft.mod.uni.ui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniCon extends Container {

	protected ContainerInterface con;

	public UniCon(ContainerInterface con, UIKey key, EntityPlayer player){

	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_){
		return false;
	}

	public ContainerInterface container(){
		return con;
	}

}
