package net.fexcraft.lib.mc.utils;

import net.fexcraft.mod.uni.util.UniPlayerSerializer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityEvents {

	public static final ResourceLocation REGNAME = new ResourceLocation("fcl:player");

	@SubscribeEvent
	public void onAttachEntityCapabilities(AttachCapabilitiesEvent<net.minecraft.entity.Entity> event){
		if(event.getObject() instanceof EntityLivingBase){
			event.addCapability(REGNAME, new UniPlayerSerializer(event.getObject()));
		}
	}

}
