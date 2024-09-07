package net.fexcraft.mod.fcl;

import com.mojang.logging.LogUtils;
import net.fexcraft.mod.fcl.util.*;
import net.fexcraft.mod.uni.UniChunk;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.impl.SWI;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.ui.UniCon;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import static net.minecraft.network.chat.Component.literal;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mod(FCL.MODID)
public class FCL {

	public static final String MODID = "fcl";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, MODID);
	public static final RegistryObject<MenuType<UniCon>> UNIVERSAL = CONTAINERS.register("universal", () -> IForgeMenuType.create(UniCon::new));
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("fcl", "channel"))
		.clientAcceptedVersions(pro -> true)
		.serverAcceptedVersions(pro -> true)
		.networkProtocolVersion(() -> "fcl")
		.simpleChannel();

	public FCL(){
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		FCL20.init(!FMLEnvironment.production, FMLLoader.getDist().isClient());
		StackWrapper.SUPPLIER = obj -> {
			if(obj instanceof ItemWrapper){
				return StackWrapper.SUPPLIER.apply(new ItemStack((Item)((ItemWrapper)obj).local()));
			}
			if(obj instanceof ItemStack){
				var v = ((ItemStack)obj).getCapability(StackWrapperProvider.CAPABILITY).resolve();
				if(v.isPresent()) return v.get();
			};
			return StackWrapper.EMPTY;
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
			NetworkHooks.openScreen((ServerPlayer)player, new MenuProvider() {
				@Override
				public Component getDisplayName(){
					return literal("Fexcraft Universal UI");
				}

				@Override
				public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player){
					return new UniCon(i, inventory, ui, null, pos);
				}
			}, buf -> {
				buf.writeInt(ui.length());
				buf.writeUtf(ui);
				buf.writeInt(pos.x);
				buf.writeInt(pos.y);
				buf.writeInt(pos.z);
			});
		};
		//UniEntity.GETTER
		//
		CONTAINERS.register(bus);
		bus.addListener(this::commonSetup);
	}

	private void commonSetup(FMLCommonSetupEvent event){
		StackWrapper.EMPTY = new SWI(ItemStack.EMPTY);
		CHANNEL.registerMessage(1, UIPacketF.class, (packet, buffer) -> buffer.writeNbt(packet.com()), buffer -> new UIPacketF(buffer.readNbt()), (packet, context) -> {
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
		ContainerInterface.SEND_TO_CLIENT = (com, player) -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> player.entity.local()), new UIPacketF(com.local()));
		ContainerInterface.SEND_TO_SERVER = com -> CHANNEL.sendToServer(new UIPacketF(com.local()));
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
			event.addCapability(new ResourceLocation("fcl:stack"), new StackWrapperProvider(event.getObject()));
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
			event.register(StackWrapper.class);
			event.register(UniChunk.class);
		}

	}

}
