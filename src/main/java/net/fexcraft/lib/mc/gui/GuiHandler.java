package net.fexcraft.lib.mc.gui;

import java.util.TreeMap;

import net.fexcraft.lib.mc.crafting.BluePrintTableGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	public static final TreeMap<Integer, Class<GenericGui<?>>> GUIS = new TreeMap<>();
	public static final TreeMap<Integer, Class<GenericGuiContainer>> CONTAINERS = new TreeMap<>();
	public static final TreeMap<String, Object> GUIMODS = new TreeMap<>();
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		if(ID == 1) return new BluePrintTableGui.Server(player, world, x, y, z);
		try{
			return GuiHandler.CONTAINERS.get(ID).getConstructor(EntityPlayer.class, World.class, int.class, int.class, int.class).newInstance(player, world, x, y, z);
		}
		catch(Exception e){ e.printStackTrace(); return null; }
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		if(ID == 1) return new BluePrintTableGui.Client(player, world, x, y, z);
		try{
			return GuiHandler.GUIS.get(ID).getConstructor(EntityPlayer.class, World.class, int.class, int.class, int.class).newInstance(player, world, x, y, z);
		} catch(Exception e){ e.printStackTrace(); return true; }
	}
	
}