package net.fexcraft.mod.fcl;

import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.UniCon;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.io.File;
import java.util.stream.Collectors;

@Mod("fcl")
public class FCL {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static UniFCL CONFIG;
    public static String MODID = "fcl";
    //
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
    public static final RegistryObject<MenuType<UniCon>> UNIVERSAL = CONTAINERS.register("universal", () -> {
        try{
            return IForgeMenuType.create(UniCon::new);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    });

    public FCL(){
        CONFIG = new UniFCL(FMLPaths.CONFIGDIR.get().toFile());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static IDL requestServerFile(String lis, String loc){
        return null;
    }

    private void setup(final FMLCommonSetupEvent event){

    }

    public static void bindTex(IDL tex){
        //TODO net.fexcraft.mod.fcl.util.FCLRenderTypes.setCutout(tex);
    }

    public static void writeTag(ByteBuf buffer, TagCW com){
        ((FriendlyByteBuf)buffer).writeNbt(com.local());
    }

    public static TagCW readTag(ByteBuf buffer){
        return TagCW.wrap(((FriendlyByteBuf)buffer).readNbt());
    }

}
