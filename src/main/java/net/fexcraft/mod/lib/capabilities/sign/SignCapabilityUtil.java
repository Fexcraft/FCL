package net.fexcraft.mod.lib.capabilities.sign;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.fexcraft.mod.lib.capabilities.sign.SignCapability.Listener;
import net.minecraft.block.BlockSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SignCapabilityUtil implements ICapabilitySerializable<NBTBase>{
	
	@CapabilityInject(SignCapability.class)
	public static final Capability<SignCapability> SIGN_CAPABILITY = null;
	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation("fcl:sign");
	private static final ArrayList<Class<? extends SignCapability.Listener>> listeners = new ArrayList<Class<? extends SignCapability.Listener>>();
	private SignCapability instance;
	
	public SignCapabilityUtil(TileEntitySign object){
		instance = SIGN_CAPABILITY.getDefaultInstance();
		instance.setTileEntity(object);
	}
	
	public static Collection<Class<? extends Listener>> getListeners(){
		return Collections.unmodifiableCollection(listeners);
	}
	
	public static void addListener(Class<? extends Listener> clazz){
		listeners.add(clazz);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		return capability == SIGN_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		return capability == SIGN_CAPABILITY ? SIGN_CAPABILITY.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT(){
		return SIGN_CAPABILITY.getStorage().writeNBT(SIGN_CAPABILITY, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt){
		SIGN_CAPABILITY.getStorage().readNBT(SIGN_CAPABILITY, instance, null, nbt);
	}
	
	//
	
	public static class Storage implements IStorage<SignCapability> {

		@Override
		public NBTBase writeNBT(Capability<SignCapability> capability, SignCapability instance, EnumFacing side){
			return instance.writeToNBT(capability, side);
		}

		@Override
		public void readNBT(Capability<SignCapability> capability, SignCapability instance, EnumFacing side, NBTBase nbt){
			instance.readNBT(capability, side, nbt);
		}
		
	}
	
	//
	
	public static class Callable implements java.util.concurrent.Callable<SignCapability>{

		@Override
		public SignCapability call() throws Exception {
			return new SignCapabilityImpl();
		}
		
	}
	
	//
	
	public static class EventHandler {
		
		@SubscribeEvent
		public void onAttachEvent(AttachCapabilitiesEvent<net.minecraft.tileentity.TileEntity> event){
			if(event.getObject() instanceof TileEntitySign){
				event.addCapability(REGISTRY_NAME, new SignCapabilityUtil((TileEntitySign)event.getObject()));
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
				SignCapability cap = te_sign.getCapability(SIGN_CAPABILITY, null);
				if(cap != null){
					cap.onPlayerInteract(event, state, te_sign);
					return;
				}
			}
			return;
		}
	}

}
