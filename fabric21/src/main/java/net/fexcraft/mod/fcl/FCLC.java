package net.fexcraft.mod.fcl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fexcraft.lib.common.math.AxisRotator;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.lib.frl.GLO;
import net.fexcraft.lib.frl.GLObject;
import net.fexcraft.lib.frl.Renderer;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.fexcraft.mod.fcl.local.CraftingRenderer;
import net.fexcraft.mod.fcl.util.*;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.packet.PacketFile;
import net.fexcraft.mod.uni.ui.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.io.IOException;

import static net.fexcraft.mod.fcl.FCL.*;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FCLC implements ClientModInitializer {

	@Override
	public void onInitializeClient(){
		EnvInfo.CLIENT = true;
		MenuScreens.register(FCL.UNIVERSAL, UniUI::new);
		BlockEntityRenderers.register(CRAFTING_ENTITY, context -> new CraftingRenderer());
		ClientPlayNetworking.registerGlobalReceiver(UI_PACKET_TYPE, (packet, context) -> {
			context.client().execute(() -> {
				((UniCon)context.player().containerMenu).onPacket(packet.com().local(), true);
			});
		});
		ClientPlayNetworking.registerGlobalReceiver(TAG_PACKET_TYPE, (packet, context) -> {
			context.client().execute(() -> {
				var cons = UniFCL.TAG_C.get(packet.lis);
				if(cons != null) cons.handle(packet.com, UniEntity.getEntity(ClientPacketPlayer.get()));
			});
		});
		ClientPlayNetworking.registerGlobalReceiver(IMG_PACKET_TYPE, (packet, context) -> {
			context.client().execute(() -> {
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
			});
		});
		//
		ModelRendererTurbo.RENDERER = new Renderer21MRT();
		Renderer.RENDERER = new net.fexcraft.mod.fcl.util.Renderer21();
		GLO.SUPPLIER = (() -> new GLObject());
		AxisRotator.DefHolder.DEF_IMPL = Axis3DL.class;
		UITab.IMPLEMENTATION = UUITab.class;
		UIText.IMPLEMENTATION = UUIText.class;
		UIField.IMPLEMENTATION = UUIField.class;
		UIButton.IMPLEMENTATION = UUIButton.class;
		ContainerInterface.TRANSLATOR = str -> Formatter.format(I18n.get(str));
		ContainerInterface.TRANSFORMAT = (str, objs) -> Formatter.format(I18n.get(str, objs));
		ContainerInterface.SEND_TO_SERVER = com -> ClientPlayNetworking.send(new UIPacket(com));
	}

	public static void sendServerFile(String lis, String loc){
		ClientPlayNetworking.send((CustomPacketPayload)new PacketFile().fill(lis, loc));
	}

}