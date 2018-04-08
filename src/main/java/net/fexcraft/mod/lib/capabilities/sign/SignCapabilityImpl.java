package net.fexcraft.mod.lib.capabilities.sign;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class SignCapabilityImpl implements SignCapability {
	
	private TileEntitySign tileentity;
	private boolean active;
	private List<SignCapability.Listener> listeners;
	
	public SignCapabilityImpl(){
		listeners = new ArrayList<SignCapability.Listener>();
		SignCapabilityUtil.getListeners().forEach(clazz -> {
			try{
				listeners.add(clazz.newInstance());
			}
			catch(InstantiationException | IllegalAccessException e){
				e.printStackTrace();
			}
		});
	}

	@Override
	public void setTileEntity(TileEntitySign tileentity){
		this.tileentity = tileentity;
	}

	@Override
	public TileEntitySign getTileEntity(){
		return tileentity;
	}

	@Override
	public NBTBase writeToNBT(Capability<SignCapability> capability, EnumFacing side){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("active", active);
		if(isActive()){
			listeners.forEach(listener -> {
				NBTBase nbt = listener.writeToNBT(capability, side);
				if(nbt != null){
					compound.setTag(listener.getId().toString(), nbt);
				}
			});
		}
		return compound;
	}

	@Override
	public void readNBT(Capability<SignCapability> capability, EnumFacing side, NBTBase nbt){
		NBTTagCompound compound = (NBTTagCompound)nbt;
		active = compound.getBoolean("active");
		if(isActive()){
			listeners.forEach(listener -> {
				listener.readNBT(capability, side, compound.hasKey(listener.getId().toString()) ? compound.getTag(listener.getId().toString()) : null);
			});
		}
	}

	@Override
	public boolean isActive(){
		return active;
	}
	
	@Override
	public boolean setActive(){
		return active = true;
	}

	@Override
	public Collection<Listener> getListeners(){
		return listeners;
	}

	@Override
	public boolean onPlayerInteract(PlayerInteractEvent event, IBlockState state, TileEntitySign tileentity){
		/*if(Static.dev()){
			Print.log((event.getWorld().isRemote ? "CLIENT" : "SERVER") + ": " + tileentity.writeToNBT(new NBTTagCompound()));
		}*/
		for(Listener listener : listeners){
			if(listener.onPlayerInteract(this, event, state, tileentity)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isListenerActive(ResourceLocation rs){
		return listeners.stream().anyMatch(listener -> listener.getId().equals(rs) && listener.isActive());
	}

	@SuppressWarnings("unchecked") @Override
	public <T> T getListener(Class<T> clazz, ResourceLocation rs){
		return (T)listeners.stream().filter(listener -> listener.getId().equals(rs)).findFirst().get();
	}

}
