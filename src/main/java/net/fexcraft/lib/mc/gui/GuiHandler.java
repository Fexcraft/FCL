package net.fexcraft.lib.mc.gui;

import net.fexcraft.lib.mc.crafting.BluePrintTableContainer2;
import net.fexcraft.lib.mc.crafting.BluePrintTableGui0;
import net.fexcraft.lib.mc.crafting.BluePrintTableGui1;
import net.fexcraft.lib.mc.crafting.BluePrintTableGui2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		if(ID == 0 || ID == 1) return new GenericContainer.DefImpl(player);
		if(ID == 2) return new BluePrintTableContainer2(player, world, x, y, z);
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		if(ID == 0) return new BluePrintTableGui0(player, world, x, y, z);
		if(ID == 1) return new BluePrintTableGui1(player, world, x, y, z);
		if(ID == 2) return new BluePrintTableGui2(player, world, x, y, z);
		return null;
	}
	
}