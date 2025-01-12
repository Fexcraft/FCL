package net.fexcraft.mod.fcl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fexcraft.lib.common.math.AxisRotator;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.fexcraft.mod.fcl.local.CraftingRenderer;
import net.fexcraft.mod.fcl.util.Axis3DL;
import net.fexcraft.mod.fcl.util.Renderer120;
import net.fexcraft.mod.fcl.util.UIPacket;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.ui.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.language.I18n;

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
		//
		ModelRendererTurbo.RENDERER = new Renderer120();
		AxisRotator.DefHolder.DEF_IMPL = Axis3DL.class;
		UITab.IMPLEMENTATION = UUITab.class;
		UIText.IMPLEMENTATION = UUIText.class;
		UIField.IMPLEMENTATION = UUIField.class;
		UIButton.IMPLEMENTATION = UUIButton.class;
		ContainerInterface.TRANSLATOR = str -> Formatter.format(I18n.get(str));
		ContainerInterface.TRANSFORMAT = (str, objs) -> Formatter.format(I18n.get(str, objs));
		ContainerInterface.SEND_TO_SERVER = com -> ClientPlayNetworking.send(new UIPacket(com));
	}

}