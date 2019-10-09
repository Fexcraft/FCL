package net.fexcraft.lib.mc.gui;

import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.utils.Print;

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
        packet.setString("target_listener", "fcl_gui"); packet.setString("task", "generic_gui"); Print.debug(target, packet);
    	if(target == Side.SERVER){
    		PacketHandler.getInstance().sendToServer(new CompoundTagPacket(packet));
    	}
    	else{
    		PacketHandler.getInstance().sendTo(new CompoundTagPacket(packet), (EntityPlayerMP)player);
    	}
	}
	
	protected abstract void packet(Side side, NBTTagCompound packet, EntityPlayer player);
	
	public static class DefImpl extends GenericContainer {

		public DefImpl(EntityPlayer player){ super(player); }

		@Override
		protected void packet(Side side, NBTTagCompound packet, EntityPlayer player){
			Print.debug("def, impl", side, packet, player);
		}
		
	}
    
    /** Server Side Method. */
    public static void openGui(String mod, int gui, int[] xyz, EntityPlayer player){
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("target_listener", "fcl_gui");
        compound.setString("task", "open_gui");
        compound.setInteger("gui", gui);
        compound.setString("guimod", mod);
        if(xyz != null) compound.setIntArray("args", xyz);
        ServerReceiver.INSTANCE.process(new CompoundTagPacket(compound), new Object[]{ player });
    }

    /** Server Side Method. */
    public static void openGenericGui(int gui, int[] xyz, NBTTagCompound data, EntityPlayer player){
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("target_listener", "fcl_gui");
        compound.setString("task", "open_guicontainer");
        compound.setInteger("gui", gui); compound.setTag("data", data);
        if(xyz != null) compound.setIntArray("args", xyz);
        ServerReceiver.INSTANCE.process(new CompoundTagPacket(compound), new Object[]{ player });
    }

}
