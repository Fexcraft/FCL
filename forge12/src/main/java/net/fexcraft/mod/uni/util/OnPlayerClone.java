package net.fexcraft.mod.uni.util;

import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OnPlayerClone {

	@SubscribeEvent
	public static void onClone(PlayerEvent.Clone event){
		UniEntity old = UniEntity.get(event.getOriginal());
		UniEntity neo = UniEntity.get(event.getEntityPlayer());
		if(old != null && neo != null) neo.copy(old);
	}

}
