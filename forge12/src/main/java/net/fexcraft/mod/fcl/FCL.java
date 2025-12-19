package net.fexcraft.mod.fcl;

import java.util.ArrayList;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.lib.mc.crafting.RecipeRegistry;
import net.fexcraft.mod.fcl.mixint.CWProvider;
import net.fexcraft.mod.fcl.mixint.EWProvider;
import net.fexcraft.mod.fcl.mixint.SWProvider;
import net.fexcraft.mod.uni.*;
import net.fexcraft.mod.uni.inv.*;
import net.fexcraft.mod.uni.util.*;
import net.fexcraft.lib.mc.gui.GuiHandler;
import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.SimpleUpdateHandler;
import net.fexcraft.lib.mc.registry.CreativeTab;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.lib.mc.render.FCLBlockModel;
import net.fexcraft.lib.mc.utils.Print;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
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
	public static UniFCL CONFIG;

	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception {
		EnvInfo.CLIENT = event.getSide().isClient();
		UniReg.LOADER_VERSION = "1.12";
		TagCW.SUPPLIER[0] = () -> new TagCWI();
		TagCW.WRAPPER[0] = obj -> new TagCWI(obj);
		TagLW.SUPPLIER[0] = () -> new TagLWI();
		TagLW.WRAPPER[0] = obj -> new TagLWI(obj);
		UniEntity.ENTITY_GETTER = ent -> new EntityWI((Entity)ent);
		UniChunk.CHUNK_GETTER = ck -> new ChunkWI((Chunk)ck);
		UniStack.STACK_GETTER = obj -> SWI.parse(obj);
		UniStack.TAG_GETTER = (key) -> {
			ArrayList<StackWrapper> list = new ArrayList<>();
			NonNullList<ItemStack> stacks = OreDictionary.getOres(key);
			for(ItemStack stack : stacks){
				list.add(UniStack.getStack(stack.copy()));
			}
			return list;
		};
		WrapperHolder.INSTANCE = new WrapperHolderImpl();
		WrapperHolder.LEVEL_PROVIDER = lvl -> new WorldWI((World)lvl);
		ItemWrapper.GETTER = id -> Item.REGISTRY.getObject(new ResourceLocation(id));
		ItemWrapper.SUPPLIER = item -> new IWI((Item)item);
		StackWrapper.ITEM_TYPES.put(StackWrapper.IT_LEAD, item -> item instanceof ItemLead);
		StackWrapper.ITEM_TYPES.put(StackWrapper.IT_FOOD, item -> item instanceof ItemFood);
		AABB.SUPPLIER = () -> new AABBI();
		AABB.WRAPPER = obj -> new AABBI((AxisAlignedBB)obj);
		UniInventory.IMPL = UniInventory12.class;
		UniFluidTank.IMPL = UniFluidTank12.class;
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
		UniFCL.registerFCLUI(instance);
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
		MinecraftForge.EVENT_BUS.register(new OnPlayerClone());
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
		UniEntity.GETTER = ent -> ((EWProvider)ent).fcl_wrapper();
		UniChunk.GETTER = ck -> ((CWProvider)ck).fcl_wrapper();
		UniStack.GETTER = stk -> {
			ItemStack stack = (ItemStack)(stk instanceof StackWrapper ? ((StackWrapper)stk).direct() : stk);
			return ((SWProvider)(Object)stack).fcl_wrapper();
		};
		UniFCL.regTagPacketListener("fcl:ui", false, new UIPacketListener.Server());
		UniFCL.regTagPacketListener("fcl:gui", false, new net.fexcraft.lib.mc.gui.ServerReceiver());
		if(event.getSide().isClient()){
			UniFCL.regTagPacketListener("fcl:ui", true, new UIPacketListener.Client());
			UniFCL.regTagPacketListener("fcl:gui", true, new net.fexcraft.lib.mc.gui.ClientReceiver());
		}
		Print.log("Loading complete.");
	}
	
	public static FCL getInstance(){
		return instance;
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

	public static void bindTex(IDL tex){
		net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(tex.local());
	}

	public static void sendServerFile(EntityW player, String lis, String loc, byte[] tex){
		if(player.isOnClient()){
			PacketHandler.getInstance().sendToServer((IMessage)new PacketFileHandler.I12_PacketImg().fill(lis, loc));
		}
		else{
			PacketHandler.getInstance().sendTo((IMessage)new PacketFileHandler.I12_PacketImg().fill(lis, loc, tex), player.local());
		}
	}

	public static IDL requestServerFile(String lis, String loc){
		PacketHandler.getInstance().sendToServer((IMessage)new PacketFileHandler.I12_PacketImg().fill(lis, loc));
		return IDLManager.getIDLCached(loc);
	}

	public static void writeTag(ByteBuf buffer, TagCW com){
		ByteBufUtils.writeTag(buffer, com.local());
	}

	public static TagCW readTag(ByteBuf buffer){
		try{
			return TagCW.wrap(ByteBufUtils.readTag(buffer));
		}
		catch(Exception e){
			e.printStackTrace();
			return TagCW.create();
		}
	}
	
}