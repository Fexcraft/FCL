package net.fexcraft.lib.mc.utils;

import net.fexcraft.lib.mc.capabilities.FCLCapabilities;
import net.fexcraft.mod.uni.util.UniPlayerSerializer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CapabilityEvents {

	public static final ResourceLocation REGNAME = new ResourceLocation("fcl:player");

	@SubscribeEvent
	public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<net.minecraft.entity.Entity> event){
		if(FCLCapabilities.PLAYER != null && event.getObject() instanceof EntityPlayer){
			event.addCapability(REGNAME, new UniPlayerSerializer(event.getObject()));
		}
	}

}
