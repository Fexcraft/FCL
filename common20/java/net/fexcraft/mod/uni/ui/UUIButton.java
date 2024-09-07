package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.RGB;
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
		uui.matrix.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		if(palette != null){
			ResourceLocation resloc = (texture == null) ? (ResourceLocation)uui.actex : (ResourceLocation)texture;
			for(int row = 0; row < palette.length; row++){
				for(int col = 0; col < palette[row].length; col++){
					int a = col * palsize[0];
					int b = row * palsize[1];
					int c = absolute ? x < 0 ? uui.width + x : x : gl + x;
					int d = absolute ? y < 0 ? uui.height + y : y : gt + y;
					rgb = palette[row][col];
					uui.matrix.setColor((rgb.packed >> 16 & 0xFF) / 255.0F, (rgb.packed >> 8 & 0xFF) / 255.0F, (rgb.packed & 0xFF) / 255.0F, rgb.alpha);
					uui.matrix.blit(resloc, c + a, d + b, u, v, palsize[0], palsize[1]);
				}
			}
			uui.matrix.setColor(1.0F, 1.0F, 1.0F, 1.0F);
			return;
		}
		rgb = enabled ? (hovered ? hcolor : ecolor) : dcolor;
		uui.matrix.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		uui.matrix.setColor((rgb.packed >> 16 & 0xFF) / 255.0F, (rgb.packed >> 8 & 0xFF) / 255.0F, (rgb.packed & 0xFF) / 255.0F, rgb.alpha);
		if(absolute){
			uui.matrix.blit((texture == null) ? (ResourceLocation)uui.actex : (ResourceLocation)texture, (x < 0) ? (uui.getGuiLeft() + x) : x, (y < 0) ? (uui.getGuiTop() + y) : y, u, v, width, height);
		}
		else{
			uui.matrix.blit((texture == null) ? (ResourceLocation)uui.actex : (ResourceLocation)texture, gl + x, gt + y, u, v, width, height);
		}
		uui.matrix.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

}