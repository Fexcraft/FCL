package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.mod.uni.ui.UIButton;
import net.fexcraft.mod.uni.ui.UIElement;
import net.fexcraft.mod.uni.ui.UniUI;
import net.fexcraft.mod.uni.ui.UserInterface;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UUIButton extends UIButton {

	protected static RGB rgb;
	public String name;

	public UUIButton(UserInterface ui, JsonMap map) throws Exception{
		super(ui, map);
	}

	public void draw(Object gui, UIElement root, float ticks, int gl, int gt, int mx, int my){
		if(!visible) return;
		UniUI uui = (UniUI)gui;
		if(texture != null) ui.drawer.bind(texture);
		int u = enabled ? (hovered ? htx : tx) : dtx;
		int v = enabled ? (hovered ? hty : ty) : dty;
		ui.drawer.applyWhite();
		if(palette != null){
			ResourceLocation resloc = (texture == null) ? uui.actex.local() : texture.local();
			for(int row = 0; row < palette.length; row++){
				for(int col = 0; col < palette[row].length; col++){
					int a = col * palsize[0];
					int b = row * palsize[1];
					int c = absolute ? x < 0 ? ui.screen_width + x : x : gl + x;
					int d = absolute ? y < 0 ? ui.screen_height + y : y : gt + y;
					rgb = palette[row][col];
					ui.drawer.apply(rgb);
					uui.matrix.blit(RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND, resloc, c + a, d + b, u, v, palsize[0], palsize[1], 256, 256, uui.packed);
				}
			}
			ui.drawer.applyWhite();
			return;
		}
		rgb = enabled ? (hovered ? hcolor : ecolor) : dcolor;
		ui.drawer.applyWhite();
		ui.drawer.apply(rgb);
		if(absolute){
			uui.matrix.blit(RenderPipelines.GUI_TEXTURED, (texture == null) ? uui.actex.local() : texture.local(), x < 0 ? ui.screen_width + x : x, y < 0 ? ui.screen_height + y : y, u, v, width, height, 256, 256, uui.packed);
		}
		else{
			uui.matrix.blit(RenderPipelines.GUI_TEXTURED, (texture == null) ? uui.actex.local() : texture.local(), gl + x, gt + y, u, v, width, height, 256, 256, uui.packed);
		}
		ui.drawer.applyWhite();
	}

}