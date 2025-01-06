package net.fexcraft.mod.fcl;

import net.fexcraft.mod.fcl.local.CraftingRenderer;
import net.fexcraft.mod.uni.ui.UniUI;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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

    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(FCL.CRAFTING_ENTITY.get(), con -> new CraftingRenderer());
    }

    @SubscribeEvent
    public static void register(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES || event.getTabKey() == CreativeModeTabs.INVENTORY){
            event.accept(FCL.CRAFTING_ITEM.get());
        }
    }

}
