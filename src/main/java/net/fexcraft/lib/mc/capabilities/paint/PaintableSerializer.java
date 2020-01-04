package net.fexcraft.lib.mc.capabilities.paint;

import net.fexcraft.lib.mc.capabilities.FCLCapabilities;
import net.minecraft.block.BlockSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PaintableSerializer implements ICapabilitySerializable<NBTBase>{
	
	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation("fcl:paintable");
	private Paintable instance;
	
	public PaintableSerializer(Object object){
		instance = FCLCapabilities.PAINTABLE.getDefaultInstance();
		((PaintableImplementation)instance).setObject(object);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		return capability == FCLCapabilities.PAINTABLE;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		return capability == FCLCapabilities.PAINTABLE ? FCLCapabilities.PAINTABLE.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT(){
		return FCLCapabilities.PAINTABLE.getStorage().writeNBT(FCLCapabilities.PAINTABLE, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt){
		FCLCapabilities.PAINTABLE.getStorage().readNBT(FCLCapabilities.PAINTABLE, instance, null, nbt);
	}
	
	//
	
	public static class Storage implements IStorage<Paintable> {

		@Override
		public NBTBase writeNBT(Capability<Paintable> capability, Paintable instance, EnumFacing side){
			return instance.writeToNBT(capability, side);
		}

		@Override
		public void readNBT(Capability<Paintable> capability, Paintable instance, EnumFacing side, NBTBase nbt){
			instance.readNBT(capability, side, nbt);
		}
		
	}
	
	//
	
	public static class Callable implements java.util.concurrent.Callable<Paintable>{

		@Override
		public Paintable call() throws Exception {
			return new PaintableImplementation();
		}
		
	}
	
	//
	
	public static class EventHandler {
		
		@SubscribeEvent
		public void onAttachEvent(AttachCapabilitiesEvent<net.minecraft.tileentity.TileEntity> event){
			if(event.getObject() instanceof TileEntitySign){
				event.addCapability(REGISTRY_NAME, new PaintableSerializer((TileEntitySign)event.getObject()));
			}
		}
		
		@SubscribeEvent
		public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event){
			IBlockState state = event.getWorld().getBlockState(event.getPos());
			if(state.getBlock() instanceof BlockSign){
				TileEntitySign te_sign = (TileEntitySign)event.getWorld().getTileEntity(event.getPos());
				if(te_sign == null || te_sign.signText == null || te_sign.signText[0] == null){
					return;
				}
				Paintable cap = te_sign.getCapability(FCLCapabilities.PAINTABLE, null);
				if(cap != null){
					//cap.onPlayerInteract(event, state, te_sign);
					return;
				}
			}
			return;
		}
		
	}

}
