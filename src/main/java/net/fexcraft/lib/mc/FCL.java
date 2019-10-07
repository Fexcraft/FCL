package net.fexcraft.lib.mc;

import java.util.UUID;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fexcraft.lib.mc.utils.Static;
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
 * consider deleting it instantly for security reasons!
 * 
 */
public class FCL implements ModInitializer {
	
	public static final String prefix = Formatting.BLACK + "[" + Formatting.DARK_AQUA + "FCL" + Formatting.BLACK + "]" + Formatting.GRAY + " ";
	public static final String version = "1.XIV.49";
	public static final String mcv = "1.14.4";
	public static final UUID[] authors = new UUID[]{ UUID.fromString("01e4af9b-2a30-471e-addf-f6338ffce04b") };
	public static FCL INSTANCE;
	//
	public static final Item FEXCRAFT_PROFILE = new Item(new Item.Settings().group(ItemGroup.MISC));
	
	@Override
	public void onInitialize(){
		System.out.println("Starting FCL!"); INSTANCE = this; Static.setAsMcLib(true);
		Static.setDevmode(FabricLoader.getInstance().isDevelopmentEnvironment());
		//
		Registry.register(Registry.ITEM, new Identifier("fcl", "fnprf"), FEXCRAFT_PROFILE);
		
	}
	
}

/*@Mod(modid = "fcl", name = "Fexcraft Common Library", version = FCL.version, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*", updateJSON = "http://fexcraft.net/minecraft/fcl/request?mode=getForgeUpdateJson&modid=fcl")
public class FCL {
	
	public static final String prefix = TextFormatting.BLACK + "[" + TextFormatting.DARK_AQUA + "FCL" + TextFormatting.BLACK + "]" + TextFormatting.GRAY + " ";
	public static final String version = "1.XII.48";
	public static final String mcv = "1.12.2";
	public static final UUID[] authors = new UUID[]{ UUID.fromString("01e4af9b-2a30-471e-addf-f6338ffce04b") };
	@Mod.Instance("fcl")
	private static FCL instance;
	private static Side side;
	private static File configdir;
	private static GuiHandler ghand;
	
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception{
		Static.setAsMcLib(true); Static.setDevmode((Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment")); Static.setIsServer((side = event.getSide()).isServer());
		configdir = new File(event.getSuggestedConfigurationFile().getParentFile(), "/fcl/");
		FCLRegistry.prepare(event.getAsmData());
		if(event.getSide().isClient()){
			net.minecraftforge.client.model.ModelLoaderRegistry.registerLoader(net.fexcraft.lib.mc.render.FCLItemModelLoader.getInstance());
		}
	}
	
	@Mod.EventHandler
    public void init(FMLInitializationEvent event) throws Exception{
		MinecraftForge.EVENT_BUS.register(new SimpleUpdateHandler.EventHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, ghand = new GuiHandler());
		GuiHandler.register("fcl", instance);
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
		Network.initializeValidator(event.getSide());
		FCLRegistry.clear(event);
		PacketHandler.init();
		CreativeTab.getIcons();
		//
		MinecraftForge.EVENT_BUS.register(new SignCapabilitySerializer.EventHandler());
		CapabilityManager.INSTANCE.register(SignCapability.class, new SignCapabilitySerializer.Storage(), new SignCapabilitySerializer.Callable());
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
	
	public File getConfigDirectory(){
		return configdir;
	}

	public static final String getVersion(){
		return version;
	}

	public static final String getMinecraftVersion(){
		return mcv;
	}

	public static GuiHandler getGuiHandler(){
		return ghand;
	}
	
}*/