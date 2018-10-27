package net.fexcraft.lib.mc.gui;

import java.lang.reflect.InvocationTargetException;

import net.fexcraft.lib.mc.api.packet.IPacketListener;
import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class ServerReceiver implements IPacketListener<PacketNBTTagCompound> {

	@Override
	public String getId(){
		return "fcl_gui";
	}

	@Override
	public void process(PacketNBTTagCompound packet, Object[] objs){
		if(!packet.nbt.hasKey("task")) return;
		EntityPlayerMP player = (EntityPlayerMP)objs[0];
		switch(packet.nbt.getString("task")){
			case "open_gui":{
                int gui = packet.nbt.getInteger("gui"); String str = packet.nbt.getString("guimod"); int[] args = packet.nbt.hasKey("args") ? packet.nbt.getIntArray("args") : new int[0];
                player.openGui(GuiHandler.GUIMODS.get(str), gui, player.world, args.length >= 1 ? args[0] : 0, args.length >= 2 ? args[1] : 0, args.length >= 3 ? args[2] : 0);
                return;
			}
			case "open_guicontainer":{
				try{
	                int[] err = packet.nbt.getIntArray("args"); NBTTagCompound compound = packet.nbt.getCompoundTag("data");
					GenericGuiContainer container = GuiHandler.CONTAINERS.get(packet.nbt.getInteger("gui")).getConstructor(EntityPlayer.class, int[].class, NBTTagCompound.class).newInstance(player, err, compound);
	                player.openContainer = container;
	                PacketHandler.getInstance().sendTo(new PacketNBTTagCompound(packet.nbt), player);
				}
				catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
                return;
			}
            case "generic_gui":{
                ((GenericGuiContainer)player.openContainer).packet(Side.SERVER, packet.nbt, player);
            	return;
            }
            //
		}
	}
	
}