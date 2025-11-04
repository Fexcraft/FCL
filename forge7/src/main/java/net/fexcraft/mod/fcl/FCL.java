package net.fexcraft.mod.fcl;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.netty.buffer.ByteBuf;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.uni.*;
import net.fexcraft.mod.uni.impl.EntityWI;
import net.fexcraft.mod.uni.impl.TagCWI;
import net.fexcraft.mod.uni.impl.TagLWI;
import net.fexcraft.mod.uni.inv.*;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.ui.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mod(modid = FCL.MODID, version = FCL.VERSION)
public class FCL {

	public static final Logger LOGGER = FMLLog.getLogger();
    public static final String MODID = "fcl";
    public static final String VERSION = "7.x";
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
		/*UniChunk.CHUNK_GETTER = ck -> new ChunkWI((Chunk)ck);
		UniStack.STACK_GETTER = obj -> SWI.parse(obj);*/
		UniStack.TAG_GETTER = (key) -> {
			ArrayList<StackWrapper> list = new ArrayList<>();
			ArrayList<ItemStack> stacks = OreDictionary.getOres(key);
			for(ItemStack stack : stacks){
				list.add(UniStack.getStack(stack.copy()));
			}
			return list;
		};
		/*WrapperHolder.INSTANCE = new WrapperHolderImpl();
		WrapperHolder.LEVEL_PROVIDER = lvl -> new WorldWI((World)lvl);
		ItemWrapper.GETTER = id -> Item.REGISTRY.getObject(new ResourceLocation(id));
		ItemWrapper.SUPPLIER = item -> new IWI((Item)item);*/
		StackWrapper.ITEM_TYPES.put(StackWrapper.IT_LEAD, item -> item instanceof ItemLead);
		StackWrapper.ITEM_TYPES.put(StackWrapper.IT_FOOD, item -> item instanceof ItemFood);
		/*UniInventory.IMPL = UniInventory12.class;
		UniFluidTank.IMPL = UniFluidTank12.class;*/
		if(EnvInfo.CLIENT){
			/*UITab.IMPLEMENTATION = UUITab.class;
			UIButton.IMPLEMENTATION = UUIButton.class;
			UIText.IMPLEMENTATION = UUIText.class;
			UIField.IMPLEMENTATION = UUIField.class;*/
			ContainerInterface.TRANSLATOR = str -> Formatter.format(net.minecraft.client.resources.I18n.format(str));
			ContainerInterface.TRANSFORMAT = (str, objs) -> Formatter.format(net.minecraft.client.resources.I18n.format(str, objs));
		}
		CONFIG = new UniFCL(event.getModConfigurationDirectory());
    }

	public static void bindTex(IDL tex){

	}

	public static IDL requestServerFile(String lis, String loc){
		return null;
	}

	public static void writeTag(ByteBuf buffer, TagCW com){

	}

	public static TagCW readTag(ByteBuf buffer){
		return null;
	}

}
