package net.fexcraft.lib.mc.gui;

import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.impl.PacketTagHandler.I12_PacketTag;
import net.fexcraft.mod.uni.tag.TagCW;
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
		TagCW com = TagCW.wrap(packet);
        com.set("task", "packet_gui");
        //Print.debug(target, packet);
        try{
        	if(target == Side.SERVER){
        		PacketHandler.getInstance().sendToServer(new I12_PacketTag("fcl:gui", com));
        	}
        	else{
        		PacketHandler.getInstance().sendTo(new I12_PacketTag("fcl:gui", com), (EntityPlayerMP)player);
        	}
        }
        catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	public void initPacket(NBTTagCompound compound){}
	
	public boolean isInit(){ return true; }
	
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
	@Deprecated
    public static void openGui(int gui, int[] xyz, String listener, EntityPlayer player){
        NBTTagCompound compound = new NBTTagCompound();
        listener = listener == null ? "fcl_gui" : listener;
        compound.setString("target_listener", listener);
        compound.setString("task", "open_gui");
        compound.setInteger("gui", gui);
        if(xyz != null) compound.setIntArray("args", xyz);
		UniFCL.TAG_S.get(listener).handle(TagCW.wrap(compound), UniEntity.getEntity(player));
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
		UniFCL.TAG_S.get(listener).handle(TagCW.wrap(compound), UniEntity.getEntity(player));
    }

}
