package net.fexcraft.mod.fcl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fexcraft.lib.common.math.AxisRotator;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.fexcraft.mod.fcl.util.*;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.ui.*;
import net.minecraft.client.resources.language.I18n;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FCLC implements ClientModInitializer {

	@Override
	public void onInitializeClient(){
		EnvInfo.CLIENT = true;
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