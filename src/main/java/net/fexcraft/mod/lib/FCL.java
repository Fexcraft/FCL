package net.fexcraft.mod.lib;

import java.io.File;
import java.util.UUID;

import net.fexcraft.mod.lib.crafting.RecipeRegistry;
import net.fexcraft.mod.lib.network.Network;
import net.fexcraft.mod.lib.network.PacketHandler;
import net.fexcraft.mod.lib.network.SimpleUpdateHandler;
import net.fexcraft.mod.lib.perms.PermManager;
import net.fexcraft.mod.lib.util.common.FclConfig;
import net.fexcraft.mod.lib.util.common.Print;
import net.fexcraft.mod.lib.util.registry.CreativeTab;
import net.fexcraft.mod.lib.util.registry.RegistryUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 * 
 * @description This Library is usually delivered as a separate jar,
 * if you got it from anywhere else then Fexcraft.net or another <i>official</i> download site,
 * consider deleting it instantly for security reasons!
 * 
 */
@Mod(modid = "fcl", name = "Fexcraft Common Library", version = FCL.version, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*", updateJSON = "http://fexcraft.net/minecraft/fcl/request?mode=getForgeUpdateJson&modid=fcl",
guiFactory = "net.fexcraft.mod.lib.util.common.GuiFactory")
public class FCL {
	
	public static final String prefix = TextFormatting.BLACK + "[" + TextFormatting.DARK_AQUA + "FCL" + TextFormatting.BLACK + "]" + TextFormatting.GRAY + " ";
	public static final String version = "XII.32";
	public static final String mcv = "1.12.2";
	public static final UUID[] authors = new UUID[]{UUID.fromString("01e4af9b-2a30-471e-addf-f6338ffce04b")};
	@Mod.Instance("fcl")
	private static FCL instance;
	private static Side side;
	private static File configdir;
	
	@Mod.EventHandler
    public void init(FMLPreInitializationEvent event) throws Exception{
		side = event.getSide();
		configdir = new File(event.getSuggestedConfigurationFile().getParentFile(), "/fcl/");
		FclConfig.initalize(event, event.getSuggestedConfigurationFile());
		RegistryUtil.prepare(event.getAsmData());
		if(!FclConfig.serverSideOnly){
			RecipeRegistry.initialize();
		}
	}
	
	@Mod.EventHandler
    public void init(FMLInitializationEvent event) throws Exception{
		MinecraftForge.EVENT_BUS.register(new SimpleUpdateHandler.EventHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new RecipeRegistry.GuiHandler());
	}
	
	@Mod.EventHandler
	public void init(FMLServerStartingEvent event){
		RegistryUtil.registerCommands(event);
	}
	
	@Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) throws Exception{
		SimpleUpdateHandler.register("fcl", 1, version);
		SimpleUpdateHandler.setUpdateMessage("fcl", prefix + "Update available! (" + SimpleUpdateHandler.getLatestVersionOf("fcl") + ")");
		SimpleUpdateHandler.postInit();
		Network.initializeValidator(event.getSide());
		RegistryUtil.clear(event);
		PermManager.initialize();
		PacketHandler.init();
		CreativeTab.getIcons();
		//RecipeRegistry.importVanillaRecipes();
		Print.log("Loading complete.");
	}
	
	public static FCL getInstance(){
		return instance;
	}
	
	public static Side getSide(){
		return side;
	}
	
	public File getConfigDirectory(){
		return configdir;
	}
	
}