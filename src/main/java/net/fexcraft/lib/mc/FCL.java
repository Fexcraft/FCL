package net.fexcraft.lib.mc;

import java.util.UUID;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fexcraft.lib.mc.crafting.WorkBench;
import net.fexcraft.lib.mc.network.Network;
import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.SimpleUpdateHandler;
import net.fexcraft.lib.mc.network.handlers.CompoundTagPacketHandler;
import net.fexcraft.lib.mc.signhook.ExampleImpl;
import net.fexcraft.lib.mc.signhook.SignInteractionHook;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.lib.mc.utils.Static;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 * 
 * @description This Library is usually delivered as a separate jar,
 * if you got it from anywhere else then Fexcraft.net or another <i>official</i> download site,
 * consider deleting it instantly due to security reasons!
 * 
 */
public class FCL implements ModInitializer {
	
	public static final String prefix = Formatting.BLACK + "[" + Formatting.DARK_AQUA + "FCL" + Formatting.BLACK + "]" + Formatting.GRAY + " ";
	public static final String version = "1.XIV.49";
	public static final String mcv = "1.14.4";
	public static final UUID[] authors = new UUID[]{ UUID.fromString("01e4af9b-2a30-471e-addf-f6338ffce04b") };
	public static FCL INSTANCE;
	//
	public static final Block WORKBENCH = new WorkBench();
	
	@Override
	public void onInitialize(){
		Print.log("[FCL] Starting FCL!"); INSTANCE = this; Static.setAsMcLib(true);
		Static.setDevmode(FabricLoader.getInstance().isDevelopmentEnvironment());
		Static.setIsServer(FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER);
		Registry.register(Registry.BLOCK, new Identifier("fcl:workbench"), WORKBENCH);
		Registry.register(Registry.ITEM, new Identifier("fcl:workbench"), new BlockItem(WORKBENCH, new Item.Settings().group(ItemGroup.TOOLS)));
		//
		if(Static.isClient()){
			//net.minecraftforge.client.model.ModelLoaderRegistry.registerLoader(net.fexcraft.lib.mc.render.FCLItemModelLoader.getInstance());
			//TODO see about custom models
		}
		ServerStartCallback.EVENT.register((server) -> {
			Static.SERVER = server; SimpleUpdateHandler.postInit(); Network.initializeValidator(Static.side());
		});
		//TODO see if server start is a good place for the update handler post init
		SimpleUpdateHandler.register("fcl", 1, version);
		SimpleUpdateHandler.setUpdateMessage("fcl", prefix + "Update available! (" + SimpleUpdateHandler.getLatestVersionOf("fcl") + ")");
		//
		//NetworkRegistry.INSTANCE.registerGuiHandler(instance, ghand = new GuiHandler());
		//GuiHandler.register("fcl", instance);
		//
		SignInteractionHook.KEYWORDS.put("[fcl-uc]", ExampleImpl.class);
		SignInteractionHook.REGISTRY.put(ExampleImpl.ID, ExampleImpl.class);
		Print.log("[FCL] Registering Server Packets"); PacketHandler.registerPackets(true);
		//
		CompoundTagPacketHandler.addListener(true, new net.fexcraft.lib.mc.gui.ServerReceiver());
		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT){
			CompoundTagPacketHandler.addListener(false, new net.fexcraft.lib.mc.gui.ClientReceiver());
		}
		Print.log("[FCL] Loading complete.");
	}
	
	/**
	 * Things to re-implement:
	 * 
	 * - Gui Lib
	 * - FCLItemModel (if applicable/necessary)
	 * - "BluePrintTable" (better than ever, tabbed)
	 * - ... I forgot? I forgot.
	 * 
	 */
	
}