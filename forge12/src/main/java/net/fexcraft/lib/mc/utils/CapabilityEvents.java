package net.fexcraft.lib.mc.utils;

import net.fexcraft.mod.uni.util.UniChunkSerializer;
import net.fexcraft.mod.uni.util.UniPlayerSerializer;
import net.fexcraft.mod.uni.util.UniStackSerializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityEvents {

	public static final ResourceLocation ENT_CAP = new ResourceLocation("fcl:entity");
	public static final ResourceLocation CK_CAP = new ResourceLocation("fcl:chunk");
	public static final ResourceLocation IS_CAP = new ResourceLocation("fcl:stack");

	@SubscribeEvent
	public void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event){
		if(event.getObject() instanceof EntityLivingBase){
			event.addCapability(ENT_CAP, new UniPlayerSerializer(event.getObject()));
		}
	}

	@SubscribeEvent
	public void onAttachChunkCapabilities(AttachCapabilitiesEvent<Chunk> event){
		event.addCapability(CK_CAP, new UniChunkSerializer(event.getObject()));
	}

	@SubscribeEvent
	public void onAttachStackCapabilities(AttachCapabilitiesEvent<ItemStack> event){
		event.addCapability(IS_CAP, new UniStackSerializer(event.getObject()));
	}

}
