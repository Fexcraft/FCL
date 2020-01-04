package net.fexcraft.lib.mc.gui;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.fexcraft.lib.mc.api.packet.IPacketListener;
import net.fexcraft.lib.mc.capabilities.FCLCapabilities;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

public class ClientReceiver implements IPacketListener<PacketNBTTagCompound> {

	@Override
	public String getId(){
		return "fcl_gui";
	}

	@Override
	public void process(PacketNBTTagCompound packet, Object[] objs){
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
	        case "paintable":{
	        	String type = packet.nbt.getString("type");
	        	switch(type){
					case "entity":{
						int id = packet.nbt.getInteger("id");
						Entity entity = player.world.getEntityByID(id); if(entity == null) return;
						entity.getCapability(FCLCapabilities.PAINTABLE, null).readNBT(null, null, packet.nbt);
						break;
					}
					case "tileentity":{
						BlockPos pos = BlockPos.fromLong(packet.nbt.getLong("pos"));
						TileEntity tile = player.world.getTileEntity(pos); if(tile == null) return;
						tile.getCapability(FCLCapabilities.PAINTABLE, null).readNBT(null, null, packet.nbt);
					}
					default: break;
				}
	        	return;
	        }
		}
	}
	
}