package net.fexcraft.lib.mc;

import java.util.UUID;

import net.fexcraft.lib.mc.capabilities.paint.Paintable;
import net.fexcraft.lib.mc.capabilities.paint.PaintableSerializer;
import net.fexcraft.lib.mc.capabilities.sign.SignCapability;
import net.fexcraft.lib.mc.capabilities.sign.SignCapabilitySerializer;
import net.fexcraft.lib.mc.gui.GuiHandler;
import net.fexcraft.lib.mc.network.Network;
import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.SimpleUpdateHandler;
import net.fexcraft.lib.mc.network.handlers.NBTTagCompoundPacketHandler;
import net.fexcraft.lib.mc.registry.CreativeTab;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.lib.mc.utils.Static;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
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
@Mod(modid = "fcl", name = "Fexcraft Common Library", version = FCL.version, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*", updateJSON = "http://fexcraft.net/minecraft/fcl/request?mode=getForgeUpdateJson&modid=fcl")
public class FCL {
	
	public static final String prefix = TextFormatting.BLACK + "[" + TextFormatting.DARK_AQUA + "FCL" + TextFormatting.BLACK + "]" + TextFormatting.GRAY + " ";
	public static final String version = "1.12.68";
	public static final String mcv = "1.12.2";
	public static final UUID[] authors = new UUID[]{ UUID.fromString("01e4af9b-2a30-471e-addf-f6338ffce04b") };
	@Mod.Instance("fcl")
	private static FCL instance;
	private static Side side;
	//private static File configdir;
	
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception{
		Static.setAsMcLib(true);
		Static.setDevmode((Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment"));
		Static.setIsServer((side = event.getSide()).isServer());
		//configdir = new File(event.getSuggestedConfigurationFile().getParentFile(), "/fcl/");
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		Network.checkConfig(config);
		config.save();
		FCLRegistry.prepare(event.getSide(), event.getAsmData());
		if(event.getSide().isClient()){
			net.fexcraft.lib.mc.render.LoaderReg.ister();
		}
	}
	
	@Mod.EventHandler
    public void init(FMLInitializationEvent event) throws Exception{
		MinecraftForge.EVENT_BUS.register(new SimpleUpdateHandler.EventHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}
	
	@Mod.EventHandler
	public void init(FMLServerStartingEvent event){
		FCLRegistry.registerCommands(event);
	}
	
	@Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) throws Exception{
		SimpleUpdateHandler.register("fcl", 1, version);
		SimpleUpdateHandler.setUpdateMessage("fcl", prefix + "Update available! (" + SimpleUpdateHandler.getLatestVersionOf("fcl") + ")");
		SimpleUpdateHandler.postInit();
		FCLRegistry.clear(event);
		PacketHandler.init();
		CreativeTab.getIcons();
		//
		MinecraftForge.EVENT_BUS.register(new SignCapabilitySerializer.EventHandler());
		CapabilityManager.INSTANCE.register(SignCapability.class, new SignCapabilitySerializer.Storage(), new SignCapabilitySerializer.Callable());
		CapabilityManager.INSTANCE.register(Paintable.class, new PaintableSerializer.Storage(), new PaintableSerializer.Callable());
		SignCapabilitySerializer.addListener(net.fexcraft.lib.mc.capabilities.sign.ExampleListener.class);
		//RecipeRegistry.importVanillaRecipes();
		NBTTagCompoundPacketHandler.addListener(Side.SERVER, new net.fexcraft.lib.mc.gui.ServerReceiver());
		if(event.getSide().isClient()){
			NBTTagCompoundPacketHandler.addListener(Side.CLIENT, new net.fexcraft.lib.mc.gui.ClientReceiver());
		}
		Print.log("Loading complete.");
	}
	
	public static FCL getInstance(){
		return instance;
	}
	
	public static Side getSide(){
		return side;
	}
	
	/*public File getConfigDirectory(){
		return configdir;
	}*/

	public static final String getVersion(){
		return version;
	}

	public static final String getMinecraftVersion(){
		return mcv;
	}
	
}