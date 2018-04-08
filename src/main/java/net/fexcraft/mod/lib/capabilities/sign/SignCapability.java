package net.fexcraft.mod.lib.capabilities.sign;

import java.util.Collection;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public interface SignCapability {
	
	public void setTileEntity(TileEntitySign tileentity);
	
	public TileEntitySign getTileEntity();

	public NBTBase writeToNBT(Capability<SignCapability> capability, EnumFacing side);

	public void readNBT(Capability<SignCapability> capability, EnumFacing side, NBTBase nbt);

	public boolean isActive();
	
	public boolean setActive();
	
	public Collection<Listener> getListeners();
	
	public boolean isListenerActive(ResourceLocation rs);

	public boolean onPlayerInteract(PlayerInteractEvent event, IBlockState state, TileEntitySign tileentity);
	
	public <T> T getListener(Class<T> clazz, ResourceLocation rs);
	
	//
	
	public static interface Listener {
		
		public ResourceLocation getId();
		
		public boolean isActive();

		public boolean onPlayerInteract(SignCapability cap, PlayerInteractEvent event, IBlockState state, TileEntitySign tileentity);

		public NBTBase writeToNBT(Capability<SignCapability> capability, EnumFacing side);

		public void readNBT(Capability<SignCapability> capability, EnumFacing side, NBTBase nbt);
		
	}
	
}
