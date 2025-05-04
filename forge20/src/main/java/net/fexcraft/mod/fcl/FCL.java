package net.fexcraft.mod.fcl;

import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.local.CraftingBlock;
import net.fexcraft.mod.fcl.local.CraftingEntity;
import net.fexcraft.mod.fcl.util.*;
import net.fexcraft.mod.uni.*;
import net.fexcraft.mod.uni.impl.SWI;
import net.fexcraft.mod.uni.impl.WrapperHolderImpl;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.packet.PacketFile;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UniCon;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

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
	public static UniFCL CONFIG;

	public FCL(){
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		FCL20.MAINDIR = FMLPaths.GAMEDIR.get().toFile();
		FCL20.init(!FMLEnvironment.production, FMLLoader.getDist().isClient());
		CONFIG = new UniFCL(FMLPaths.CONFIGDIR.get().toFile());
		WrapperHolder.INSTANCE = new WrapperHolderImpl();
		UniStack.GETTER = obj -> {
			ItemStack stack = (ItemStack)(obj instanceof StackWrapper ? ((StackWrapper)obj).direct() : obj);
			var v = stack.getCapability(UniStackProvider.CAPABILITY).resolve();
			return v.isPresent() ? v.get() : null;
		};
		UniEntity.GETTER = ent -> {
			var v = ((Entity)ent).getCapability(UniEntityProvider.CAPABILITY).resolve();
			return v.isPresent() ? v.get() : null;
		};
		UniChunk.GETTER = ck -> {
			var v = ((LevelChunk)ck).getCapability(UniChunkProvider.CAPABILITY).resolve();
			return v.isPresent() ? v.get() : null;
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
		CHANNEL.registerMessage(2, TagPacket.class, (packet, buffer) -> {
				buffer.writeInt(packet.key().length());
				buffer.writeUtf(packet.key());
				buffer.writeNbt(packet.com().local());
			}, buffer -> new TagPacket(buffer.readUtf(buffer.readInt()), TagCW.wrap(buffer.readNbt())),
			(packet, context) -> {
				context.get().enqueueWork(() -> {
					if(context.get().getDirection().getOriginationSide().isClient()){
						ServerPlayer player = context.get().getSender();
						var cons = UniFCL.TAG_S.get(packet.key());
						if(cons != null) cons.handle(packet.com(), UniEntity.getEntity(player));
					}
					else{
						var cons = UniFCL.TAG_C.get(packet.key());
						if(cons != null) cons.handle(packet.com(), UniEntity.getEntity(ClientPacketPlayer.get()));
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

	@Mod.EventBusSubscriber(modid = "fcl", bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeBusEvents {

		@SubscribeEvent
		public static void onAttachEntityCaps(AttachCapabilitiesEvent<Entity> event){
			if(event.getObject() instanceof LivingEntity){
				event.addCapability(new ResourceLocation("fcl:entity"), new UniEntityProvider(event.getObject()));
			}
		}

		@SubscribeEvent
		public static void onAttachStackCaps(AttachCapabilitiesEvent<ItemStack> event){
			event.addCapability(new ResourceLocation("fcl:stack"), new UniStackProvider(event.getObject()));
		}

		@SubscribeEvent
		public static void onAttachChunkCaps(AttachCapabilitiesEvent<LevelChunk> event){
			event.addCapability(new ResourceLocation("fcl:chunk"), new UniChunkProvider(event.getObject()));
		}

	}

	@Mod.EventBusSubscriber(modid = "fcl", bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBusEvents {

		@SubscribeEvent
		public void registerCaps(RegisterCapabilitiesEvent event){
			event.register(UniEntity.class);
			event.register(UniStack.class);
			event.register(UniChunk.class);
		}

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

}
