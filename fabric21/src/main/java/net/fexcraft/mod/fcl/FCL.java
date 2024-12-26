package net.fexcraft.mod.fcl;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.fexcraft.lib.common.math.AxisRotator;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.fcl.mixint.SWProvider;
import net.fexcraft.mod.fcl.util.Axis3DL;
import net.fexcraft.mod.fcl.util.ChunkWI;
import net.fexcraft.mod.fcl.util.EntityUtil;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.UniChunk;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.impl.*;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.ui.*;
import net.fexcraft.mod.uni.world.StateWrapper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;
import java.util.function.Supplier;

import static net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FCL implements ModInitializer {

	public static final String MODID = "fcl";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static File GAMEDIR;
	public static Supplier<MinecraftServer> SERVER = null;
	//
	public static Optional<MenuType<UniCon>> UNIVERSAL;

	@Override
	public void onInitialize(){
		GAMEDIR = FabricLoader.getInstance().getGameDirectory();
		init(FabricLoader.getInstance().isDevelopmentEnvironment(), false);
		StackWrapper.SUPPLIER = obj -> {
			if(obj instanceof ItemWrapper){
				return StackWrapper.SUPPLIER.apply(new ItemStack((Item)((ItemWrapper)obj).local()));
			}
			if(obj instanceof ItemStack){
				return ((SWProvider)obj).fcl_wrapper();
			};
			return StackWrapper.EMPTY;
		};
		//
		MenuType<?> mt = Registry.register(BuiltInRegistries.MENU, "fcl:universal", (MenuType<?>)new MenuType((i, inv) -> new UniCon(i, inv, null), DEFAULT_FLAGS));
		UNIVERSAL = Optional.of((MenuType<UniCon>)mt);
		//
		UniFCL.registerUI(this);
	}

	private void init(boolean dev, boolean client){
		EnvInfo.CLIENT = client;
		EnvInfo.DEV = dev;
		UniReg.LOADER_VERSION = "1.20";
		if(client){
			AxisRotator.DefHolder.DEF_IMPL = Axis3DL.class;
		}
		TagCW.WRAPPER[0] = com -> new TagCWI((CompoundTag)com);
		TagLW.WRAPPER[0] = com -> new TagLWI((ListTag)com);
		TagCW.SUPPLIER[0] = () -> new TagCWI();
		TagLW.SUPPLIER[0] = () -> new TagLWI();
		ItemWrapper.GETTER = id -> BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
		ItemWrapper.SUPPLIER = item -> new IWI((Item)item);
		StateWrapper.DEFAULT = new StateWrapperI(Blocks.AIR.defaultBlockState());
		StateWrapper.STATE_WRAPPER = state -> new StateWrapperI((BlockState)state);
		StateWrapper.COMMAND_WRAPPER = (blk, arg) -> {
			try{
				Block block = blk == null ? null : (Block)blk;
				if(block == null){
					String[] split = arg.split(" ");
					arg = split[1];
					block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(split[0])).get().value();
				}
				BlockState state = block.defaultBlockState();
				String[] pairs = arg.split(",");
				for(String pair : pairs){
					String[] sp = pair.split("=");
					Property<?> prop = getProperty(state, sp[0]);
					if(prop != null) state = setPropValue(state, prop, sp[1]);
				}
				return new StateWrapperI(state);//BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), arg, false).blockState());
			}
			catch(Exception e){
				e.printStackTrace();
				return StateWrapper.DEFAULT;
			}
		};
		StateWrapper.STACK_WRAPPER = (stack, ctx) ->{
			Item item = stack.getItem().local();
			if(item instanceof BlockItem){
				Block block = ((BlockItem)item).getBlock();
				BlockPos pos = new BlockPos(ctx.pos.x, ctx.pos.y, ctx.pos.z);
				BlockHitResult res = new BlockHitResult(new Vec3(ctx.pos.x, ctx.pos.y, ctx.pos.z), Direction.UP, pos, true);
				BlockPlaceContext btx = new BlockPlaceContext(ctx.world.local(), ctx.placer.local(), ctx.main ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, stack.local(), res);
				return StateWrapper.of(block.getStateForPlacement(btx));
			}
			else return StateWrapper.DEFAULT;
		};
		UniEntity.ENTITY_GETTER = ent -> EntityUtil.wrap((Entity)ent);
		UniChunk.CHUNK_GETTER = ck -> new ChunkWI((LevelChunk)ck);
		WrapperHolderImpl.LEVEL_PROVIDER = lvl -> new LevelW((Level)lvl);
		UISlot.GETTERS.put("default", args -> new Slot((Container)args[0], (Integer)args[1], (Integer)args[2], (Integer)args[3]));
		if(EnvInfo.CLIENT){
			UITab.IMPLEMENTATION = UUITab.class;
			UIText.IMPLEMENTATION = UUIText.class;
			UIField.IMPLEMENTATION = UUIField.class;
			UIButton.IMPLEMENTATION = UUIButton.class;
			ContainerInterface.TRANSLATOR = str -> Formatter.format(I18n.get(str));
			ContainerInterface.TRANSFORMAT = (str, objs) -> Formatter.format(I18n.get(str, objs));
		}
	}

	private static Property<?> getProperty(BlockState state, String str){
		for(Property<?> prop : state.getProperties()){
			if(prop.getName().equals(str)) return prop;
		}
		return null;
	}

	private static <C extends Comparable<C>> BlockState setPropValue(BlockState state, Property<C> prop, String str){
		Optional<C> opt = prop.getValue(str);
		//FCL.LOGGER.info(opt.toString());
		if(opt.isPresent()) return state.setValue(prop, opt.get());
		return state;
	}

}