package net.fexcraft.mod.fcl;

import net.fexcraft.mod.uni.ui.UniUI;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(modid = "fcl", bus = MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event){
        MenuScreens.register(FCL.UNIVERSAL.get(), UniUI::new);
    }

}
