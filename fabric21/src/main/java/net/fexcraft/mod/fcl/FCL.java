package net.fexcraft.mod.fcl;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.lib.common.utils.CallbackContainer;
import net.fexcraft.mod.fcl.local.CraftingBlock;
import net.fexcraft.mod.fcl.local.CraftingEntity;
import net.fexcraft.mod.fcl.mixint.CWProvider;
import net.fexcraft.mod.fcl.mixint.EWProvider;
import net.fexcraft.mod.fcl.mixint.SWProvider;
import net.fexcraft.mod.fcl.util.*;
import net.fexcraft.mod.uni.*;
import net.fexcraft.mod.uni.impl.*;
import net.fexcraft.mod.uni.inv.*;
import net.fexcraft.mod.uni.packet.PacketFile;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.ui.*;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.StateWrapper;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.commands.Commands.literal;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FCL implements ModInitializer {

	public static final String MODID = "fcl";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static File GAMEDIR;
	public static Optional<MinecraftServer> SERVER = Optional.empty();
	public static UniFCL CONFIG;
	//
	public static CallbackContainer INIT_COMPLETE = new CallbackContainer();
	//
	public static final ResourceLocation UI_PACKET = ResourceLocation.parse("fcl:ui");
	public static final CustomPacketPayload.Type<UIPacket> UI_PACKET_TYPE = new CustomPacketPayload.Type<>(UI_PACKET);
	public static final StreamCodec<RegistryFriendlyByteBuf, UIPacket> UI_PACKET_CODEC = StreamCodec.of(UIPacket::encode, UIPacket::new);
	//
	public static final ResourceLocation TAG_PACKET = ResourceLocation.parse("fcl:tag");
	public static final CustomPacketPayload.Type<PacketTag21> TAG_PACKET_TYPE = new CustomPacketPayload.Type<>(TAG_PACKET);
	public static final StreamCodec<RegistryFriendlyByteBuf, PacketTag21> TAG_PACKET_CODEC = StreamCodec.of(PacketTag21::encode, PacketTag21::decode);
	//
	public static final ResourceLocation UI_SYNC = ResourceLocation.parse("fcl:ui_sync");
	public static final CustomPacketPayload.Type<UISync> UI_SYNC_TYPE = new CustomPacketPayload.Type<>(UI_SYNC);
	public static final StreamCodec<RegistryFriendlyByteBuf, UISync> UI_SYNC_CODEC = StreamCodec.of(UISync::encode, UISync::new);
	//
	public static final ResourceLocation IMG_PACKET = ResourceLocation.parse("fcl:img");
	public static final CustomPacketPayload.Type<FilePacket> IMG_PACKET_TYPE = new CustomPacketPayload.Type<>(IMG_PACKET);
	public static final StreamCodec<RegistryFriendlyByteBuf, FilePacket> IMG_PACKET_CODEC = StreamCodec.of(FilePacket::encode, FilePacket::new);
	//
	public static final Block CRAFTING_BLOCK = register("fcl:crafting", CraftingBlock::new, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.COLOR_LIGHT_GRAY));
	public static final BlockEntityType<CraftingEntity> CRAFTING_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "fcl:crafting",
			FabricBlockEntityTypeBuilder.create(CraftingEntity::new, CRAFTING_BLOCK).build());
	//
	public static final DataComponentType<CustomData> FCLTAG = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.parse("fcl:data"),
		DataComponentType.<CustomData>builder().persistent(CustomData.CODEC).build());
 	//
	public static ExtendedScreenHandlerType<UniCon, UISync> UNIVERSAL;
	private static boolean recipereg;

	@Override
	public void onInitialize(){
		GAMEDIR = FabricLoader.getInstance().getGameDirectory();
		CONFIG = new UniFCL(new File(GAMEDIR, "/config/"));
		init(FabricLoader.getInstance().isDevelopmentEnvironment());
		WrapperHolder.INSTANCE = new WrapperHolderImpl();
		UniStack.GETTER = obj -> {
			ItemStack stack = (ItemStack)(obj instanceof StackWrapper ? ((StackWrapper)obj).direct() : obj);
			return ((SWProvider)(Object)stack).fcl_wrapper();
		};
		UniEntity.GETTER = ent -> ((EWProvider)ent).fcl_wrapper();
		UniChunk.GETTER = ck -> ((CWProvider)ck).fcl_wrapper();
		//
		CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) ->
			dispatcher.register(literal("fcl").executes(com -> {
				if(com.getSource().isPlayer()) UniEntity.getEntity(com.getSource().getPlayer()).openUI(UniFCL.SELECT_CONFIG, V3I.NULL);
				return 1;
			}))
		);
		//
		PayloadTypeRegistry.playS2C().register(UI_PACKET_TYPE, UI_PACKET_CODEC);
		PayloadTypeRegistry.playC2S().register(UI_PACKET_TYPE, UI_PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(UI_PACKET_TYPE, (packet, context) -> {
			context.server().execute(() -> {
				((UniCon)context.player().containerMenu).onPacket(packet.com().local(), false);
			});
		});
		PayloadTypeRegistry.playS2C().register(TAG_PACKET_TYPE, TAG_PACKET_CODEC);
		PayloadTypeRegistry.playC2S().register(TAG_PACKET_TYPE, TAG_PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(TAG_PACKET_TYPE, (packet, context) -> {
			context.server().execute(() -> {
				ServerPlayer player = context.player();
				var cons = UniFCL.TAG_S.get(packet.lis);
				if(cons != null) cons.handle(packet.com, UniEntity.getEntity(player));
			});
		});
		PayloadTypeRegistry.playS2C().register(IMG_PACKET_TYPE, IMG_PACKET_CODEC);
		PayloadTypeRegistry.playC2S().register(IMG_PACKET_TYPE, IMG_PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(IMG_PACKET_TYPE, (packet, context) -> {
			context.server().execute(() -> {
				try{
					EntityW player = UniEntity.getEntity(context.player());
					if(!packet.lis.equals("def")){
						UniFCL.SFL_S.get(packet.lis).handle(packet.loc, null, player);
						return;
					}
					byte[] tex = UniFCL.getServerFile(packet.loc);
					FCL.sendServerFile(player, packet.lis, packet.loc, tex);
				}
				catch(Exception e){
					throw new RuntimeException(e);
				}
			});
		});
		UNIVERSAL = Registry.register(BuiltInRegistries.MENU, "fcl:universal", new ExtendedScreenHandlerType<UniCon, UISync>(new ExtendedScreenHandlerType.ExtendedFactory<UniCon, UISync>() {
			@Override
			public UniCon create(int i, Inventory inventory, UISync sync){
				return new UniCon(i, inventory, sync.key(), sync.pos(), sync.map());
			}
		}, UI_SYNC_CODEC));
		//
		EntityUtil.UI_OPENER = (player, ui, pos) -> {
			try{
				UIKey key = UIKey.find(ui);
				player.openMenu(new ExtendedScreenHandlerFactory<>() {
					@Override
					public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player){
						return new UniCon(i, inventory, key, pos, UniReg.getMenuJson(ui));
					}

					@Override
					public Component getDisplayName(){
						return Component.literal("Fexcraft Universal UI");
					}

					@Override
					public Object getScreenOpeningData(ServerPlayer serverPlayer){
						return new UISync(key, pos, UniReg.getMenuJson(ui));
					}
				});
			}
			catch(Exception e){
				e.printStackTrace();
			}
		};
		ContainerInterface.SEND_TO_CLIENT = (com, player) -> {
			ServerPlayNetworking.getSender((ServerPlayer)player.entity.direct()).sendPacket(new UIPacket(com));
		};
		UniFCL.registerFCLUI(this);
		FclRecipe.VALIDATE = comp -> {
			if(comp.tag){
				if(comp.key == null) comp.key = TagKey.create(Registries.ITEM, ResourceLocation.parse(comp.id));
				return comp.key != null;
			}
			else return !comp.stack.empty();
		};
		FclRecipe.GET_TAG_AS_LIST = (comp) -> {
			ArrayList<StackWrapper> list = new ArrayList<>();
			if(comp.key == null) comp.key = TagKey.create(Registries.ITEM, ResourceLocation.parse(comp.id));
			var tags = BuiltInRegistries.ITEM.getTagOrEmpty((TagKey<Item>) comp.key);
			for(Holder<Item> item : tags){
				list.add(UniStack.createStack(new ItemStack(item, comp.amount)));
			}
			return list;
		};
		CommonLifecycleEvents.TAGS_LOADED.register((ra, bool) -> {
			if(!recipereg && UniFCL.EXAMPLE_RECIPES){
				recipereg = true;
				FclRecipe.newBuilder("recipe.fcl.testing").add(new ItemStack(Blocks.COBBLESTONE, 4)).output(new ItemStack(Blocks.STONE_STAIRS, 5)).register();
				FclRecipe.newBuilder("recipe.fcl.testing").add("minecraft:logs", 9).output(new ItemStack(Blocks.TRAPPED_CHEST, 1)).register();
			}
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
			content.accept(new ItemStack(CRAFTING_BLOCK), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		});
		INIT_COMPLETE.complete();
	}

	private void init(boolean dev){
		EnvInfo.CLIENT = false;
		EnvInfo.DEV = dev;
		UniReg.LOADER_VERSION = "1.21";
		TagCW.WRAPPER[0] = com -> new TagCWI((CompoundTag)com);
		TagLW.WRAPPER[0] = com -> new TagLWI((ListTag)com);
		TagCW.SUPPLIER[0] = TagCWI::new;
		TagLW.SUPPLIER[0] = TagLWI::new;
		ItemWrapper.GETTER = id -> BuiltInRegistries.ITEM.get(ResourceLocation.parse(id)).get().value();
		ItemWrapper.SUPPLIER = item -> new IWI((Item)item);
		UniInventory.IMPL = UniInventory21.class;
		UniFluidTank.IMPL = UniFluidTank21.class;
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
		UniStack.STACK_GETTER = obj -> SWI.parse(obj);
		UniEntity.ENTITY_GETTER = ent -> EntityUtil.wrap((Entity)ent);
		UniChunk.CHUNK_GETTER = ck -> new ChunkWI((LevelChunk)ck);
		StackWrapper.EMPTY = SWI.parse(ItemStack.EMPTY);
		WrapperHolderImpl.LEVEL_PROVIDER = lvl -> new LevelW((Level)lvl);
		UISlot.GETTERS.put("default", args -> new Slot((Container)args[0], (Integer)args[1], (Integer)args[2], (Integer)args[3]));
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

	/** ref: wiki.fabricmc.net/tutorial:blocks#registering_blocks_in_1212 */
	public static Block register(String idl, Function<Block.Properties, Block> factory, Block.Properties props){
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(idl));
		Block block = Blocks.register(key, factory, props);
		Items.registerBlock(block);
		return block;
	}

	public static Block registerBlock(String idl, Function<Block.Properties, Block> factory){
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(idl));
		Block block = Blocks.register(key, factory, BlockBehaviour.Properties.of());
		Items.registerBlock(block);
		return block;
	}

	public static Item registerItem(String idl, Function<Item.Properties, Item> factory){
		return Items.registerItem(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(idl)), factory);
	}

	public static void bindTex(IDL tex){
		net.fexcraft.mod.fcl.util.FCLRenderTypes.setCutout(tex);
	}

	public static void sendServerFile(EntityW player, String lis, String loc, byte[] img){
		if(player.isOnClient()){
			FCLC.sendServerFile(lis, loc);
		}
		else{
			ServerPlayNetworking.getSender((ServerPlayer)player.direct()).sendPacket((CustomPacketPayload)new PacketFile().fill(lis, loc, img));
		}
	}

	public static IDL requestServerFile(String lis, String loc){
		FCLC.sendServerFile(lis, loc);
		return IDLManager.getIDLCached(loc);
	}

	public static void writeTag(ByteBuf buffer, TagCW com){
		((RegistryFriendlyByteBuf)buffer).writeNbt(com.local());
	}

	public static TagCW readTag(ByteBuf buffer){
		return TagCW.wrap(((RegistryFriendlyByteBuf)buffer).readNbt());
	}

}