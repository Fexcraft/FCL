package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.mod.uni.IDL;
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
		if(texture != null) uui.bindTexture(texture);
		int u = enabled ? (hovered ? htx : tx) : dtx;
		int v = enabled ? (hovered ? hty : ty) : dty;
		//TODO uui.matrix.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		uui.bindTexture(texture == null ? uui.actex : texture);
		if(palette != null){
			for(int row = 0; row < palette.length; row++){
				for(int col = 0; col < palette[row].length; col++){
					int a = col * palsize[0];
					int b = row * palsize[1];
					int c = absolute ? x < 0 ? ui.screen_width + x : x : gl + x;
					int d = absolute ? y < 0 ? ui.screen_height + y : y : gt + y;
					rgb = palette[row][col];
					//TODO setColor((rgb.packed >> 16 & 0xFF) / 255.0F, (rgb.packed >> 8 & 0xFF) / 255.0F, (rgb.packed & 0xFF) / 255.0F, rgb.alpha);
					uui.blit(uui.matrix, c + a, d + b, u, v, palsize[0], palsize[1]);
				}
			}
			//TODO uui.matrix.setColor(1.0F, 1.0F, 1.0F, 1.0F);
			return;
		}
		rgb = enabled ? (hovered ? hcolor : ecolor) : dcolor;
		//TODO uui.matrix.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		//TODO uui.matrix.setColor((rgb.packed >> 16 & 0xFF) / 255.0F, (rgb.packed >> 8 & 0xFF) / 255.0F, (rgb.packed & 0xFF) / 255.0F, rgb.alpha);
		if(absolute){
			uui.blit(uui.matrix, x < 0 ? ui.screen_width + x : x, y < 0 ? ui.screen_height + y : y, u, v, width, height);
		}
		else{
			uui.blit(uui.matrix, gl + x, gt + y, u, v, width, height);
		}
		//TODO uui.matrix.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

}