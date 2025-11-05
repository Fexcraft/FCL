package net.fexcraft.mod.fcl.util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class OnPlayerClone {

	@SubscribeEvent
	public static void onClone(PlayerEvent.Clone event){
		UniEntity old = UniEntity.get(event.original);
		UniEntity neo = UniEntity.get(event.entity);
		if(old != null && neo != null) neo.copy(old);
	}

}
