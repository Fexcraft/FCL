package net.fexcraft.mod.fcl;

import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.lib.common.math.AxisRotator;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.lib.frl.GLO;
import net.fexcraft.lib.frl.GLObject;
import net.fexcraft.lib.frl.Renderer;
import net.fexcraft.mod.fcl.local.CraftingBlock;
import net.fexcraft.mod.fcl.local.CraftingEntity;
import net.fexcraft.mod.fcl.mixint.CWProvider;
import net.fexcraft.mod.fcl.mixint.EWProvider;
import net.fexcraft.mod.fcl.mixint.SWProvider;
import net.fexcraft.mod.fcl.util.*;
import net.fexcraft.mod.uni.*;
import net.fexcraft.mod.uni.impl.*;
import net.fexcraft.mod.uni.inv.ItemWrapper;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniInventory;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.packet.PacketFile;
import net.fexcraft.mod.uni.packet.PacketTag;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.ui.*;
import net.fexcraft.mod.uni.world.AABB;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.StateWrapper;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.network.chat.Component.literal;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mod(FCL.MODID)
public class FCL {

	public static final String MODID = "fcl";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, MODID);
	public static final RegistryObject<MenuType<UniCon>> UNIVERSAL = CONTAINERS.register("universal", () -> {
		try{
			return IForgeMenuType.create(UniCon::new);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	});
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("fcl", "channel"))
		.clientAcceptedVersions(pro -> true)
		.serverAcceptedVersions(pro -> true)
		.networkProtocolVersion(() -> "fcl")
		.simpleChannel();
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCKENTS = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
	//
	public static final RegistryObject<Block> CRAFTING_BLOCK = BLOCKS.register("crafting", () -> new CraftingBlock());
	public static final RegistryObject<Item> CRAFTING_ITEM = ITEMS.register("crafting", () -> new BlockItem(CRAFTING_BLOCK.get(), new Item.Properties()));
	public static final RegistryObject<BlockEntityType<CraftingEntity>> CRAFTING_ENTITY = BLOCKENTS.register("crafting", () ->
			BlockEntityType.Builder.of(CraftingEntity::new, CRAFTING_BLOCK.get()).build(null));
	//
	private static ConcurrentHashMap<String, TagKey<Item>> tagkeys = new ConcurrentHashMap<>();
	public static Optional<MinecraftServer> SERVER = Optional.empty();
	public static File MAINDIR;
	public static UniFCL CONFIG;

	public FCL(){
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MAINDIR = FMLPaths.GAMEDIR.get().toFile();
		init();
		if(FMLLoader.getDist().isClient()) initClient();
		CONFIG = new UniFCL(FMLPaths.CONFIGDIR.get().toFile());
		UniStack.TAG_GETTER = key -> {
			ArrayList<StackWrapper> list = new ArrayList<>();
			if(!tagkeys.containsKey(key)){
				tagkeys.put(key, ItemTags.create(new ResourceLocation(key)));
			}
			var tags = ForgeRegistries.ITEMS.tags().getTag(tagkeys.get(key));
			for(Item item : tags){
				list.add(UniStack.createStack(new ItemStack(item)));
			}
			return list;
		};
		EntityUtil.UI_OPENER = (player, ui, pos) -> {
			try{
				NetworkHooks.openScreen((ServerPlayer)player, new MenuProvider() {
					@Override
					public Component getDisplayName(){
						return literal("Fexcraft Universal UI");
					}

					@Override
					public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player){
						return new UniCon(i, inventory, ui, pos, UniReg.getMenuJson(ui));
					}
				}, buf -> {
					buf.writeInt(ui.length());
					buf.writeUtf(ui);
					buf.writeInt(pos.x);
					buf.writeInt(pos.y);
					buf.writeInt(pos.z);
					String str = JsonHandler.toString(UniReg.getMenuJson(ui), JsonHandler.PrintOption.FLAT);
					buf.writeInt(str.length());
					buf.writeUtf(str);//TODO send once and cache instead
				});
			}
			catch(Exception e){
				e.printStackTrace();
			}
		};
		UniFCL.registerFCLUI(this);
		FclRecipe.VALIDATE = comp -> {
			if(comp.tag){
				if(comp.key == null) comp.key = ItemTags.create(new ResourceLocation(comp.id));
				return ForgeRegistries.ITEMS.tags().getTag((TagKey<Item>)comp.key) != null;
			}
			else return !comp.stack.empty();
		};
		FclRecipe.GET_TAG_AS_LIST = (comp) -> {
			ArrayList<StackWrapper> list = new ArrayList<>();
			if(comp.key == null) comp.key = ItemTags.create(new ResourceLocation(comp.id));
			var tags = ForgeRegistries.ITEMS.tags().getTag((TagKey<Item>)comp.key);
			for(Item item : tags){
				list.add(UniStack.createStack(new ItemStack(item, comp.amount)));
			}
			return list;
		};
		//
		MinecraftForge.EVENT_BUS.register(this);
		ITEMS.register(bus);
		BLOCKS.register(bus);
		BLOCKENTS.register(bus);
		CONTAINERS.register(bus);
		bus.addListener(this::commonSetup);
	}

	private void init(){
		EnvInfo.CLIENT = false;
		EnvInfo.DEV = !FMLEnvironment.production || EnvInfo.DEV;
		UniReg.LOADER_VERSION = "1.20";
		WrapperHolder.INSTANCE = new WrapperHolderImpl();
		UniStack.GETTER = obj -> {
			ItemStack stack = (ItemStack)(obj instanceof StackWrapper ? ((StackWrapper)obj).direct() : obj);
			return ((SWProvider)(Object)stack).fcl_wrapper();
		};
		UniEntity.GETTER = ent -> ((EWProvider)ent).fcl_wrapper();
		UniChunk.GETTER = ck -> ((CWProvider)ck).fcl_wrapper();
		TagCW.WRAPPER[0] = com -> new TagCWI((CompoundTag)com);
		TagLW.WRAPPER[0] = com -> new TagLWI((ListTag)com);
		TagCW.SUPPLIER[0] = () -> new TagCWI();
		TagLW.SUPPLIER[0] = () -> new TagLWI();
		ItemWrapper.GETTER = id -> BuiltInRegistries.ITEM.get(new ResourceLocation(id));
		ItemWrapper.SUPPLIER = item -> new IWI((Item)item);
		AABB.SUPPLIER = () -> new AABBI();
		AABB.WRAPPER = obj -> new AABBI((net.minecraft.world.phys.AABB)obj);
		UniInventory.IMPL = UniInventory20.class;
		UniFluidTank20.IMPL = UniFluidTank20.class;
		StateWrapper.DEFAULT = new StateWrapperI(Blocks.AIR.defaultBlockState());
		StateWrapper.STATE_WRAPPER = state -> new StateWrapperI((BlockState)state);
		StateWrapper.COMMAND_WRAPPER = (blk, arg) -> {
			try{
				Block block = blk == null ? null : (Block)blk;
				if(block == null){
					String[] split = arg.split(" ");
					arg = split[1];
					block = BuiltInRegistries.BLOCK.get(new ResourceLocation(split[0]));
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
			try{
				Item item = stack.getItem().local();
				if(item instanceof BlockItem){
					Block block = ((BlockItem)item).getBlock();
					BlockPos pos = new BlockPos(ctx.pos.x, ctx.pos.y, ctx.pos.z);
					BlockHitResult res = new BlockHitResult(new Vec3(ctx.pos.x + ctx.off.x, ctx.pos.y + ctx.off.y, ctx.pos.z + ctx.off.z), Direction.UP, pos, true);
					BlockPlaceContext btx = new BlockPlaceContext(ctx.world.local(), ctx.placer.local(), ctx.main ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, stack.local(), res);
					return StateWrapper.of(block.getStateForPlacement(btx));
				}
			}
			catch(Exception e){
				//
			}
			return StateWrapper.DEFAULT;
		};
		StackWrapper.ITEM_TYPES.put(StackWrapper.IT_LEAD, item -> item instanceof LeadItem);
		StackWrapper.ITEM_TYPES.put(StackWrapper.IT_FOOD, item -> ((Item)item).isEdible());
		UniStack.STACK_GETTER = obj -> SWI.parse(obj);
		UniEntity.ENTITY_GETTER = ent -> EntityUtil.wrap((Entity)ent);
		UniChunk.CHUNK_GETTER = ck -> new ChunkWI((LevelChunk)ck);
		WrapperHolderImpl.LEVEL_PROVIDER = lvl -> new WorldWI((Level)lvl);
		UISlot.GETTERS.put("default", args -> new Slot((Container)args[0], (Integer)args[1], (Integer)args[2], (Integer)args[3]));
	}

	public static void initClient(){
		EnvInfo.CLIENT = true;
		Renderer.RENDERER = new Renderer20();
		GLO.SUPPLIER = (() -> new GLObject());
		AxisRotator.DefHolder.DEF_IMPL = Axis3DL.class;
		UITab.IMPLEMENTATION = UUITab.class;
		UIText.IMPLEMENTATION = UUIText.class;
		UIField.IMPLEMENTATION = UUIField.class;
		UIButton.IMPLEMENTATION = UUIButton.class;
		ContainerInterface.TRANSLATOR = str -> Formatter.format(I18n.get(str));
		ContainerInterface.TRANSFORMAT = (str, objs) -> Formatter.format(I18n.get(str, objs));
	}

	public static void bindTex(IDL tex){
		net.fexcraft.mod.fcl.util.FCLRenderTypes.setCutout(tex);
	}

	private void commonSetup(FMLCommonSetupEvent event){
		StackWrapper.EMPTY = SWI.parse(ItemStack.EMPTY);
		CHANNEL.registerMessage(1, UIPacket.class, (packet, buffer) -> buffer.writeNbt(packet.com()), buffer -> new UIPacket(buffer.readNbt()), (packet, context) -> {
			context.get().enqueueWork(() -> {
				if(context.get().getDirection().getOriginationSide().isClient()){
					ServerPlayer player = context.get().getSender();
					((UniCon)player.containerMenu).onPacket(packet.com(), false);
				}
				else{
					((UniCon)ClientPacketPlayer.get().containerMenu).onPacket(packet.com(), true);
				}
			});
			context.get().setPacketHandled(true);
		});
		CHANNEL.registerMessage(2, PacketTag.class, (packet, buffer) -> {
				buffer.writeInt(packet.lis.length());
				buffer.writeUtf(packet.lis);
				buffer.writeNbt(packet.com.local());
			}, buffer -> new PacketTag().fill(buffer.readUtf(buffer.readInt()), TagCW.wrap(buffer.readNbt())),
			(packet, context) -> {
				context.get().enqueueWork(() -> {
					if(context.get().getDirection().getOriginationSide().isClient()){
						ServerPlayer player = context.get().getSender();
						var cons = UniFCL.TAG_S.get(packet.lis);
						if(cons != null) cons.handle(packet.com, UniEntity.getEntity(player));
					}
					else{
						var cons = UniFCL.TAG_C.get(packet.lis);
						if(cons != null) cons.handle(packet.com, UniEntity.getEntity(ClientPacketPlayer.get()));
					}
				}
			);
			context.get().setPacketHandled(true);
		});
		CHANNEL.registerMessage(3, PacketFile.class, PacketFile::encode, buffer -> {
			PacketFile pkt = new PacketFile();
			pkt.decode(buffer);
			return pkt;
		}, (packet, context) -> {
			context.get().enqueueWork(() -> {
				if(context.get().getDirection().getOriginationSide().isClient()){
					try{
						EntityW player = UniEntity.getEntity(context.get().getSender());
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
				}
				else{
					try{
						if(!packet.lis.equals("def")){
							UniFCL.SFL_C.get(packet.lis).handle(packet.loc, packet.img, UniEntity.getEntity(ClientPacketPlayer.get()));
							return;
						}
						ExternalTextures.get(packet.loc, packet.img);
					}
					catch(IOException e){
						e.printStackTrace();
					}
				}
			});
			context.get().setPacketHandled(true);
		});
		ContainerInterface.SEND_TO_CLIENT = (com, player) -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> player.entity.local()), new UIPacket(com.local()));
		ContainerInterface.SEND_TO_SERVER = com -> CHANNEL.sendToServer(new UIPacket(com.local()));
		//
		if(UniFCL.EXAMPLE_RECIPES){
			FclRecipe.newBuilder("recipe.fcl.testing").add(new ItemStack(Blocks.COBBLESTONE, 4)).output(new ItemStack(Blocks.STONE_STAIRS, 5)).register();
			FclRecipe.newBuilder("recipe.fcl.testing").add("minecraft:logs", 9).output(new ItemStack(Blocks.TRAPPED_CHEST, 1)).register();
		}
	}

	@SubscribeEvent
	public void onServerStopped(ServerStoppedEvent event){
		FCL.SERVER = Optional.empty();
	}

	@SubscribeEvent
	public void onCmdReg(RegisterCommandsEvent event){
		event.getDispatcher().register(Commands.literal("fcl")
			.executes(cmd -> {
				if(cmd.getSource().isPlayer()) UniEntity.getEntity(cmd.getSource().getPlayer()).openUI(UniFCL.SELECT_CONFIG_CATEGORY, V3I.NULL);
				return 0;
			})
		);
	}

	public static void sendServerFile(EntityW player, String lis, String loc, byte[] img){
		if(player.isOnClient()){
			CHANNEL.sendToServer(new PacketFile().fill(lis, loc));
		}
		else{
			CHANNEL.send(PacketDistributor.PLAYER.with(() -> player.local()), new PacketFile().fill(lis, loc, img));
		}
	}

	public static IDL requestServerFile(String lis, String loc){
		CHANNEL.sendToServer(new PacketFile().fill(lis, loc));
		return IDLManager.getIDLCached(loc);
	}

	public static void writeTag(ByteBuf buffer, TagCW com){
		((FriendlyByteBuf)buffer).writeNbt(com.local());
	}

	public static TagCW readTag(ByteBuf buffer){
		return TagCW.wrap(((FriendlyByteBuf)buffer).readNbt());
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
