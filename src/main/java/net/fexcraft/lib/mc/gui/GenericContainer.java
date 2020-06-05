package net.fexcraft.lib.mc.gui;

import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.handlers.NBTTagCompoundPacketHandler;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public abstract class GenericContainer extends Container {
	
	protected EntityPlayer player;
	
	public GenericContainer(EntityPlayer player){ this.player = player; }

    @Override
    public boolean canInteractWith(EntityPlayer player){
        return !(player == null);
    }

	public void setPlayer(EntityPlayer player){
		this.player = player;
	}
	
	public void send(Side target, NBTTagCompound packet){
        packet.setString("target_listener", "fcl_gui");
        packet.setString("task", "generic_gui");
        //Print.debug(target, packet);
        try{
        	if(target == Side.SERVER){
        		PacketHandler.getInstance().sendToServer(new PacketNBTTagCompound(packet));
        	}
        	else{
        		PacketHandler.getInstance().sendTo(new PacketNBTTagCompound(packet), (EntityPlayerMP)player);
        	}
        }
        catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	public void initPacket(NBTTagCompound compound){}
	
	protected abstract void packet(Side side, NBTTagCompound packet, EntityPlayer player);
	
	public static class DefImpl extends GenericContainer {

		public DefImpl(EntityPlayer player){ super(player); }

		@Override
		protected void packet(Side side, NBTTagCompound packet, EntityPlayer player){
			Print.debug("def, impl", side, packet, player);
		}

		@Override
		public void initPacket(NBTTagCompound compound){
			Print.debug("def, impl, init", compound);
		}
		
	}
    
    /** Server Side Method. */
    public static void openGui(int gui, int[] xyz, String listener, EntityPlayer player){
        NBTTagCompound compound = new NBTTagCompound();
        listener = listener == null ? "fcl_gui" : listener;
        compound.setString("target_listener", listener);
        compound.setString("task", "open_gui");
        compound.setInteger("gui", gui);
        if(xyz != null) compound.setIntArray("args", xyz);
        NBTTagCompoundPacketHandler.getListener(listener, false).process(new PacketNBTTagCompound(compound), new Object[]{ player });
    }

    /** Server Side Method. */
    public static void openGui(int gui, int[] xyz, String listener, NBTTagCompound data, EntityPlayer player){
        NBTTagCompound compound = new NBTTagCompound();
        listener = listener == null ? "fcl_gui" : listener;
        compound.setString("target_listener", listener);
        compound.setString("task", "open_gui");
        compound.setInteger("gui", gui);
        compound.setTag("data", data);
        if(xyz != null) compound.setIntArray("args", xyz);
        NBTTagCompoundPacketHandler.getListener(listener, false).process(new PacketNBTTagCompound(compound), new Object[]{ player });
    }

}
