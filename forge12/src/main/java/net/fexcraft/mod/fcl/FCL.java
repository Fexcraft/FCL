package net.fexcraft.mod.fcl;

import java.util.ArrayList;
import java.util.UUID;

import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.lib.mc.crafting.RecipeRegistry;
import net.fexcraft.mod.uni.*;
import net.fexcraft.mod.uni.inv.ItemWrapper;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniInventory;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.util.*;
import net.fexcraft.lib.mc.capabilities.sign.SignCapability;
import net.fexcraft.lib.mc.capabilities.sign.SignCapabilitySerializer;
import net.fexcraft.lib.mc.gui.GuiHandler;
import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.SimpleUpdateHandler;
import net.fexcraft.lib.mc.network.handlers.NBTTagCompoundPacketHandler;
import net.fexcraft.lib.mc.registry.CreativeTab;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.lib.mc.render.FCLBlockModel;
import net.fexcraft.lib.mc.utils.CapabilityEvents;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.uni.impl.*;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.ui.*;
import net.fexcraft.mod.uni.world.*;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	public static final String version = "12.xx";
	public static final String mcv = "1.12.2";
	public static final UUID[] authors = new UUID[]{ UUID.fromString("01e4af9b-2a30-471e-addf-f6338ffce04b") };
	public static final Logger LOGGER = LogManager.getLogger("FCL");
	@Mod.Instance("fcl")
	private static FCL instance;
	private static Side side;
	public static UniFCL CONFIG;
	
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception {
		EnvInfo.CLIENT = event.getSide().isClient();
		EnvInfo.DEV = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
		TagCW.SUPPLIER[0] = () -> new TagCWI();
		TagCW.WRAPPER[0] = obj -> new TagCWI(obj);
		TagLW.SUPPLIER[0] = () -> new TagLWI();
		TagLW.WRAPPER[0] = obj -> new TagLWI(obj);
		UniEntity.ENTITY_GETTER = ent -> new EntityWI((Entity)ent);
		UniChunk.CHUNK_GETTER = ck -> new ChunkWI((Chunk)ck);
		UniStack.STACK_GETTER = obj -> SWI.parse(obj);
		WrapperHolder.INSTANCE = new WrapperHolderImpl();
		WrapperHolder.LEVEL_PROVIDER = lvl -> new WorldWI((World)lvl);
		ItemWrapper.GETTER = id -> Item.REGISTRY.getObject(new ResourceLocation(id));
		ItemWrapper.SUPPLIER = item -> ItemWrapper.wrap(new IWI((Item)item));
		StackWrapper.ITEM_TYPES.put(StackWrapper.IT_LEAD, item -> item instanceof ItemLead);
		StackWrapper.ITEM_TYPES.put(StackWrapper.IT_FOOD, item -> item instanceof ItemFood);
		UniInventory.IMPL = UniInventory12.class;
		Static.setDevMode(EnvInfo.DEV);
		Static.setIsServer((side = event.getSide()).isServer());
		if(EnvInfo.CLIENT){
			UITab.IMPLEMENTATION = UUITab.class;
			UIButton.IMPLEMENTATION = UUIButton.class;
			UIText.IMPLEMENTATION = UUIText.class;
			UIField.IMPLEMENTATION = UUIField.class;
			ContainerInterface.TRANSLATOR = str -> Formatter.format(net.minecraft.client.resources.I18n.format(str));
			ContainerInterface.TRANSFORMAT = (str, objs) -> Formatter.format(net.minecraft.client.resources.I18n.format(str, objs));
		}
		CONFIG = new UniFCL(event.getModConfigurationDirectory());
		UISlot.GETTERS.put("default", args -> new Slot((IInventory)args[0], (Integer)args[1], (Integer)args[2], (Integer)args[3]){
			@Override
			public boolean isItemValid(ItemStack stack){
				if(args[0] instanceof UniInventory){
					return ((UniInventory12)args[0]).valid((Integer)args[1], UniStack.getStack(stack));
				}
				return super.isItemValid(stack);
			}
		});
		UniFCL.registerUI(instance);
		FclRecipe.VALIDATE = comp -> {
			if(comp.tag) return OreDictionary.doesOreNameExist(comp.id);
			else return !comp.stack.empty();
		};
		FclRecipe.GET_TAG_AS_LIST = (comp) -> {
			ArrayList<StackWrapper> list = new ArrayList<>();
			NonNullList<ItemStack> stacks = OreDictionary.getOres(comp.id);
			for(ItemStack stack : stacks){
				StackWrapper wrapper = UniStack.getStack(stack.copy());
				wrapper.count(comp.amount);
				list.add(wrapper);
			}
			return list;
		};
		FCLRegistry.prepare(event.getSide(), event.getAsmData());
		if(event.getSide().isClient()){
			net.fexcraft.lib.mc.render.LoaderReg.ister();
			net.fexcraft.lib.common.math.AxisRotator.DefHolder.DEF_IMPL = net.fexcraft.lib.mc.utils.Axis3DL.class;
			net.fexcraft.lib.mc.render.FCLBlockModelLoader.addBlockModel(new ResourceLocation("fcl:models/block/crafting"), (FCLBlockModel)((Class<?>)FCLRegistry.getModel("fcl:models/block/crafting")).newInstance());
		}
	}
	
	@Mod.EventHandler
    public void init(FMLInitializationEvent event) throws Exception{
		StackWrapper.EMPTY = new SWI(ItemStack.EMPTY);
		//MinecraftForge.EVENT_BUS.register(new SimpleUpdateHandler.EventHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		if(UniFCL.EXAMPLE_RECIPES){
			FclRecipe.newBuilder("recipe.fcl.testing").add(new ItemStack(Blocks.COBBLESTONE, 4)).output(new ItemStack(Blocks.STONE_STAIRS, 5)).register();
			FclRecipe.newBuilder("recipe.fcl.testing").add("ingotIron", 9).output(new ItemStack(Blocks.IRON_BLOCK, 1)).register();
		}
		RecipeRegistry.addShapedRecipe("fcl:crafting", null, new ItemStack(FCLRegistry.getBlock("fcl:crafting"), 1), 3, 2,
				Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)), Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)), Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)),
                Ingredient.fromStacks(new ItemStack(Blocks.LOG)), Ingredient.fromStacks(new ItemStack(Blocks.CRAFTING_TABLE)), Ingredient.fromStacks(new ItemStack(Blocks.LOG)));
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
		MinecraftForge.EVENT_BUS.register(new CapabilityEvents());
		CapabilityManager.INSTANCE.register(SignCapability.class, new SignCapabilitySerializer.Storage(), new SignCapabilitySerializer.Callable());
		CapabilityManager.INSTANCE.register(UniEntity.class, new UniPlayerStorage(), new UniPlayerCallable());
		CapabilityManager.INSTANCE.register(UniChunk.class, new UniChunkStorage(), new UniChunkCallable());
		CapabilityManager.INSTANCE.register(UniStack.class, new UniStackStorage(), new UniStackCallable());
		UniEntity.GETTER = ent -> ((Entity)ent).getCapability(FCLCapabilities.PLAYER, null);
		UniChunk.GETTER = ck -> ((Chunk)ck).getCapability(FCLCapabilities.CHUNK, null);
		UniStack.GETTER = stk -> {
			ItemStack stack = (ItemStack)(stk instanceof StackWrapper ? ((StackWrapper)stk).direct() : stk);
			return stack.getCapability(FCLCapabilities.STACK, null);
		};
		UIPacketListener.register();
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