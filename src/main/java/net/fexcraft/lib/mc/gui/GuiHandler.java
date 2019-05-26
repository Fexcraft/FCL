package net.fexcraft.lib.mc.gui;

import java.util.TreeMap;

import net.fexcraft.lib.mc.crafting.BluePrintTableContainer2;
import net.fexcraft.lib.mc.crafting.BluePrintTableGui0;
import net.fexcraft.lib.mc.crafting.BluePrintTableGui1;
import net.fexcraft.lib.mc.crafting.BluePrintTableGui2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	public static final TreeMap<Integer, Class<? extends GenericGui<?>>> GUIS = new TreeMap<>();
	public static final TreeMap<Integer, Class<? extends GenericContainer>> CONTAINERS = new TreeMap<>();
	public static final TreeMap<String, Object> GUIMODS = new TreeMap<>();
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		if(ID == 0 || ID == 1) return new GenericContainer.DefImpl(player);
		if(ID == 2) return new BluePrintTableContainer2(player, world, x, y, z);
		try{ return GuiHandler.CONTAINERS.get(ID).getConstructor(EntityPlayer.class, World.class, int.class, int.class, int.class).newInstance(player, world, x, y, z); }
		catch(Exception e){ e.printStackTrace(); return null; }
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		if(ID == 0) return new BluePrintTableGui0(player, world, x, y, z);
		if(ID == 1) return new BluePrintTableGui1(player, world, x, y, z);
		if(ID == 2) return new BluePrintTableGui2(player, world, x, y, z);
		try{ return GuiHandler.GUIS.get(ID).getConstructor(EntityPlayer.class, World.class, int.class, int.class, int.class).newInstance(player, world, x, y, z); }
		catch(Exception e){ e.printStackTrace(); return true; }
	}

	//** For Client Side. */
	public static boolean insert(int id, Class<? extends GenericGui<?>> gui, Class<? extends GenericContainer> container){
		if(!GUIS.containsKey(id) && !CONTAINERS.containsKey(id)){
			GUIS.put(id, gui); CONTAINERS.put(id, container); return true;
		} else return false;
	}

	//** For Server Side. */
	public static boolean insert(int id, Class<? extends GenericContainer> container){
		if(!CONTAINERS.containsKey(id)){ CONTAINERS.put(id, container); return true; } else return false;
	}

	public static void register(String modid, Object mod){
		GUIMODS.put(modid, mod);
	}
	
}