package net.fexcraft.lib.mc.capabilities.sign;

import java.util.Collection;

import javax.annotation.Nullable;

import net.fexcraft.lib.mc.utils.Static;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
		
		@Nullable
		public NBTBase writeToNBT(Capability<SignCapability> capability, EnumFacing side);

		public void readNBT(Capability<SignCapability> capability, EnumFacing side, @Nullable NBTBase nbt);
		
		default void sendUpdate(TileEntitySign tileentity){
			
			Static.getServer().getPlayerList().getPlayers().forEach(player -> {
				if(player.getPosition().distanceSq(tileentity.getPos()) < 256)
				player.connection.sendPacket(tileentity.getUpdatePacket());
			});
		}
		
		default BlockPos getPosAtBack(IBlockState state, TileEntitySign tileentity){
			EnumFacing facing = state.getBlock() instanceof BlockWallSign ? EnumFacing.byIndex(tileentity.getBlockMetadata()) : EnumFacing.UP;
			if(facing == EnumFacing.UP){
				facing = EnumFacing.fromAngle((tileentity.getBlockMetadata() * 360) / 16.0);
			}
			facing = facing.getOpposite();
			//
			if(facing.getAxis().isVertical()){
				return tileentity.getPos().add(0, -1, 0);//invalid
			}
			else{
				BlockPos pos = tileentity.getPos();
				if(facing.getAxis() == EnumFacing.Axis.X){
					pos = pos.add(facing.getAxisDirection().getOffset(), 0, 0);
				}
				else{
					pos = pos.add(0, 0, facing.getAxisDirection().getOffset());
				}
				return pos;
			}
		}
		
	}
	
}
