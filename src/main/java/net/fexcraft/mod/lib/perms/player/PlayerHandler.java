package net.fexcraft.mod.lib.perms.player;

import net.fexcraft.mod.lib.util.common.Print;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerHandler {
	
	@CapabilityInject(IPlayerPerms.class)
	public static final Capability<IPlayerPerms> PERMISSIONS = null;
	
	public static void initialize(){
		Print.log("[PERMS] Initialising Player Permission Capability Handler.");
		CapabilityManager.INSTANCE.register(IPlayerPerms.class, new Storage(), PlayerPerms.class);
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
	
	public static class Provider implements ICapabilitySerializable<NBTBase>{
		
		private final IPlayerPerms instance;
		private final EntityPlayer player;
		private boolean loaded;
		
		public Provider(EntityPlayer entity){
			instance = PERMISSIONS.getDefaultInstance();
			player = entity;
			loaded = false;
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing){
			return capability == PERMISSIONS;
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing){
			return capability == PERMISSIONS ? PERMISSIONS.<T>cast(this.instance) : null;
		}
		
		@Override
		public NBTBase serializeNBT(){
			instance.save(player.getGameProfile().getId());
			return PERMISSIONS.getStorage().writeNBT(PERMISSIONS, instance, null);
		}
		
		@Override
		public void deserializeNBT(NBTBase nbt){
			if(!loaded){
				instance.load(player.getGameProfile().getId());
				loaded = true;
			}
			PERMISSIONS.getStorage().readNBT(PERMISSIONS, instance, null, nbt);
		}
	}
	
	public static class Storage implements IStorage<IPlayerPerms> {

		@Override
		public NBTBase writeNBT(Capability<IPlayerPerms> capability, IPlayerPerms instance, EnumFacing side){
			return new NBTTagCompound();
			//
		}

		@Override
		public void readNBT(Capability<IPlayerPerms> capability, IPlayerPerms instance, EnumFacing side, NBTBase nbt){
			//
		}
		
	}
	
	public static class EventHandler {
		
		@SubscribeEvent
		public void attachCapabilities(AttachCapabilitiesEvent<Entity> event){
			if(event.getObject() instanceof EntityPlayer || event.getObject() instanceof EntityPlayerMP){
				event.addCapability(new ResourceLocation("fcl", "perms"), new Provider((EntityPlayer)event.getObject()));
			}
		}
		
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public void onJoin(PlayerLoggedInEvent event){
			event.player.getCapability(PERMISSIONS, null).load(event.player.getGameProfile().getId());
		}
		
		@SubscribeEvent
		public void clonePlayer(PlayerEvent.Clone event){;
			event.getEntityPlayer().getCapability(PERMISSIONS, null).copy(event.getOriginal().getCapability(PERMISSIONS, null).getInstance());
		}
	}
	
	public static PlayerPerms getPerms(Entity entity){
		return entity.getCapability(PERMISSIONS, null).getInstance();
    }
	
}