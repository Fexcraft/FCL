package net.fexcraft.lib.mc.gui;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.fexcraft.lib.mc.network.IPacketListener;
import net.fexcraft.lib.mc.network.packet.CompoundTagPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class ClientReceiver implements IPacketListener<CompoundTagPacket> {

	@Override
	public String getId(){
		return "fcl_gui";
	}

	@Override
	public void process(CompoundTagPacket packet, Object[] objs){
		if(!packet.nbt.hasKey("task")) return;
        EntityPlayer player = (EntityPlayer)objs[0];
		switch(packet.nbt.getString("task")){
	        case "generic_gui":{
	            ((GenericContainer)player.openContainer).packet(Side.CLIENT, packet.nbt, player);
	        	return;
	        }
	        case "open_guicontainer":{
				try {
		            int[] arr = packet.nbt.getIntArray("args"); NBTTagCompound compound = packet.nbt.getCompoundTag("data");
					GenericGui<?> gui = GuiHandler.GUIS.get(packet.nbt.getInteger("gui")).getConstructor(EntityPlayer.class, int[].class, NBTTagCompound.class).newInstance(player, arr, compound);
		            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
		            mc.currentScreen = gui;
		            if(gui != null){
		                mc.setIngameNotInFocus(); net.minecraft.client.settings.KeyBinding.unPressAllKeys();
		                while(Mouse.next()){ ; } while(Keyboard.next()){ ; }
		                net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(mc);
		                gui.setWorldAndResolution(mc, sr.getScaledWidth(), sr.getScaledHeight());
		                mc.skipRenderWorld = false;
		            }
		            else{ mc.getSoundHandler().resumeSounds(); mc.setIngameFocus(); }
		            player.openContainer = gui.container;
				}
				catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	return;
	        }
	        //
		}
	}
	
}