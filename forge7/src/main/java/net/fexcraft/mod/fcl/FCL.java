package net.fexcraft.mod.fcl;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.registry.GameRegistry;
import io.netty.buffer.ByteBuf;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.fcl.mixint.CWProvider;
import net.fexcraft.mod.fcl.mixint.EWProvider;
import net.fexcraft.mod.fcl.mixint.SWProvider;
import net.fexcraft.mod.fcl.packet.PacketFileHandler;
import net.fexcraft.mod.fcl.packet.PacketHandler;
import net.fexcraft.mod.fcl.util.CraftingTable;
import net.fexcraft.mod.fcl.util.FclCmd;
import net.fexcraft.mod.fcl.util.OnPlayerClone;
import net.fexcraft.mod.uni.*;
import net.fexcraft.mod.uni.impl.*;
import net.fexcraft.mod.uni.inv.*;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.ui.*;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mod(modid = FCL.MODID, version = FCL.VERSION)
public class FCL {

	public static final Logger LOGGER = FMLLog.getLogger();
    public static final String MODID = "fcl";
    public static final String VERSION = "7.x";
	@Mod.Instance
	public static FCL INSTANCE;
	public static UniFCL CONFIG;

	@EventHandler
    public void init(FMLPreInitializationEvent event){
		EnvInfo.CLIENT = event.getSide().isClient();
		EnvInfo.DEV = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
		UniReg.LOADER_VERSION = "1.7";
		TagCW.SUPPLIER[0] = () -> new TagCWI();
		TagCW.WRAPPER[0] = obj -> new TagCWI(obj);
		TagLW.SUPPLIER[0] = () -> new TagLWI();
		TagLW.WRAPPER[0] = obj -> new TagLWI(obj);
		UniEntity.ENTITY_GETTER = ent -> new EntityWI((Entity)ent);
		UniChunk.CHUNK_GETTER = ck -> new ChunkWI((Chunk)ck);
		UniStack.STACK_GETTER = obj -> SWI.parse(obj);
		UniStack.TAG_GETTER = (key) -> {
			ArrayList<StackWrapper> list = new ArrayList<>();
			ArrayList<ItemStack> stacks = OreDictionary.getOres(key);
			for(ItemStack stack : stacks){
				list.add(UniStack.getStack(stack.copy()));
			}
			return list;
		};
		WrapperHolder.INSTANCE = new WrapperHolderImpl();
		WrapperHolder.LEVEL_PROVIDER = lvl -> new WorldWI((World)lvl);
		ItemWrapper.GETTER = Item.itemRegistry::getObject;
		ItemWrapper.SUPPLIER = item -> new IWI((Item)item);
		StackWrapper.ITEM_TYPES.put(StackWrapper.IT_LEAD, item -> item instanceof ItemLead);
		StackWrapper.ITEM_TYPES.put(StackWrapper.IT_FOOD, item -> item instanceof ItemFood);
		UniEntity.GETTER = ent -> ((EWProvider)ent).fcl_wrapper();
		UniChunk.GETTER = ck -> ((CWProvider)ck).fcl_wrapper();
		UniStack.GETTER = stk -> {
			ItemStack stack = (ItemStack)(stk instanceof StackWrapper ? ((StackWrapper)stk).direct() : stk);
			return ((SWProvider)(Object)stack).fcl_wrapper();
		};
		UniInventory.IMPL = UniInventory7.class;
		UniFluidTank.IMPL = UniFluidTank7.class;
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
					return ((UniInventory7)args[0]).valid((Integer)args[1], UniStack.getStack(stack));
				}
				return super.isItemValid(stack);
			}
		});
		UniFCL.registerFCLUI(INSTANCE = this);
		FclRecipe.VALIDATE = comp -> {
			if(comp.tag) return OreDictionary.doesOreNameExist(comp.id);
			else return !comp.stack.empty();
		};
		FclRecipe.GET_TAG_AS_LIST = (comp) -> {
			ArrayList<StackWrapper> list = new ArrayList<>();
			List<ItemStack> stacks = OreDictionary.getOres(comp.id);
			for(ItemStack stack : stacks){
				StackWrapper wrapper = UniStack.getStack(stack.copy());
				wrapper.count(comp.amount);
				list.add(wrapper);
			}
			return list;
		};
		MinecraftForge.EVENT_BUS.register(new OnPlayerClone());
		//
		GameRegistry.registerBlock(new CraftingTable(), "crafting");
    }

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) throws Exception{
		StackWrapper.EMPTY = null;//TODO
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
		if(UniFCL.EXAMPLE_RECIPES){
			FclRecipe.newBuilder("recipe.fcl.testing").add(new ItemStack(Blocks.cobblestone, 4)).output(new ItemStack(Blocks.stone_stairs, 5)).register();
			FclRecipe.newBuilder("recipe.fcl.testing").add("ingotIron", 9).output(new ItemStack(Blocks.iron_block, 1)).register();
		}
		//TODO crafting block recipe
	}

	@Mod.EventHandler
	public void init(FMLServerStartingEvent event){
		event.registerServerCommand(new FclCmd());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event){
		PacketHandler.init();
		//
		UniFCL.regTagPacketListener("fcl:ui", false, new UIPacketListener.Server());
		if(event.getSide().isClient()){
			UniFCL.regTagPacketListener("fcl:ui", true, new UIPacketListener.Client());
		}
	}

	public static void bindTex(IDL tex){
		net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(tex.local());
	}

	public static IDL requestServerFile(String lis, String loc){
		PacketHandler.getInstance().sendToServer((IMessage)new PacketFileHandler.I12_PacketImg().fill(lis, loc));
		return IDLManager.getIDLCached(loc);
	}

	public static void sendServerFile(EntityW player, String lis, String loc, byte[] tex){
		if(player.isOnClient()){
			PacketHandler.getInstance().sendToServer((IMessage)new PacketFileHandler.I12_PacketImg().fill(lis, loc));
		}
		else{
			PacketHandler.getInstance().sendTo((IMessage)new PacketFileHandler.I12_PacketImg().fill(lis, loc, tex), player.local());
		}
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
