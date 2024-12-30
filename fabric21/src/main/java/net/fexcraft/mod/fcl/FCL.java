package net.fexcraft.mod.fcl;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.AxisRotator;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.mixint.CWProvider;
import net.fexcraft.mod.fcl.mixint.EWProvider;
import net.fexcraft.mod.fcl.mixint.SWProvider;
import net.fexcraft.mod.fcl.util.*;
import net.fexcraft.mod.uni.*;
import net.fexcraft.mod.uni.impl.*;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.ui.*;
import net.fexcraft.mod.uni.world.StateWrapper;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;
import java.util.function.Supplier;

import static net.minecraft.commands.Commands.literal;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FCL implements ModInitializer {

	public static final String MODID = "fcl";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static File GAMEDIR;
	public static Supplier<MinecraftServer> SERVER = null;
	//
	public static final ResourceLocation UI_PACKET = ResourceLocation.parse("fcl:ui");
	public static final CustomPacketPayload.Type<UIPacket> UI_PACKET_TYPE = new CustomPacketPayload.Type<>(UI_PACKET);
	public static final StreamCodec<RegistryFriendlyByteBuf, UIPacket> UI_PACKET_CODEC = StreamCodec.of(UIPacket::encode, UIPacket::new);
	//
	public static final ResourceLocation UI_SYNC = ResourceLocation.parse("fcl:ui_sync");
	public static final CustomPacketPayload.Type<UISync> UI_SYNC_TYPE = new CustomPacketPayload.Type<>(UI_SYNC);
	public static final StreamCodec<RegistryFriendlyByteBuf, UISync> UI_SYNC_CODEC = StreamCodec.of(UISync::encode, UISync::new);
	//
	public static ExtendedScreenHandlerType<UniCon, UISync> UNIVERSAL;

	@Override
	public void onInitialize(){
		GAMEDIR = FabricLoader.getInstance().getGameDirectory();
		new ConfigBase(new File(GAMEDIR, "/config/fcl.json")) {
			@Override
			protected void fillInfo(JsonMap map){

			}

			@Override
			protected void fillEntries(){
				entries.add(new ConfigEntry(this, "1", "1111", 100000).rang(0, 1000)
					.info("2")
					.req(false, true)
				);
				entries.add(new ConfigEntry(this, "2", "2222", true)
					.info("1")
					.req(false, false)
				);
				entries.add(new ConfigEntry(this, "3", "3333", 5).rang(1, 60)
					.info("0")
					.req(true, true)
				);
			}

			@Override
			protected void onReload(JsonMap map){

			}
		};
		init(FabricLoader.getInstance().isDevelopmentEnvironment(), false);
		WrapperHolder.INSTANCE = new WrapperHolderImpl();
		StackWrapper.SUPPLIER = obj -> {
			if(obj instanceof ItemWrapper){
				return StackWrapper.SUPPLIER.apply(new ItemStack((Item)((ItemWrapper)obj).local()));
			}
			if(obj instanceof ItemStack){
				return ((SWProvider)obj).fcl_wrapper();
			};
			return StackWrapper.EMPTY;
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
						return new UniCon(0, inventory, key, pos, UniReg.getMenuJson(ui));
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