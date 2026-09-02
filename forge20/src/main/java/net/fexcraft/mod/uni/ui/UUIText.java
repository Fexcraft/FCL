package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;
import net.minecraft.client.resources.language.I18n;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UUIText extends UIText {

	public UUIText(UserInterface ui, JsonMap map) throws Exception {
		super(ui, map);
	}

	public void translate(){
		value = ContainerInterface.translate(value);
	}

	public void translate(Object... objects){
		value = ContainerInterface.transformat(value, objects);
	}

	public void draw(Object gui, UIElement root, float ticks, int gl, int gt, int mx, int my){
		if(!visible) return;
		UniUI uui = (UniUI)gui;
		int xx = 0, yy = 0, textwidth = uui.getMinecraft().font.width(value);
		if(root != null){
			if(centered){
				xx = root.x + root.width / 2 + x - textwidth / 2;
				yy = root.y + root.height / 2 + y - height / 2;
			}
			else{
				xx = x < 0 ? root.x + root.width + x - textwidth : (root.x + x);
				yy = y < 0 ? root.y + root.height + y - height : (root.y + y);
			}
			if(!root.absolute){
				xx += gl;
				yy += gt;
			}
			hovered = root.hovered();
		}
		else{
			hovered(gl, gt, mx, my);
			xx = absolute ? x < 0 ? ui.screen_width + x : x : gl + x;
			yy = absolute ? y < 0 ? ui.screen_height + y : y : gt + y;
			if(centered) xx += (width - textwidth) / 2;
		}
		if(scale == 0.0F || (scale < 0.0F && textwidth < width)){
			uui.matrix.drawString(uui.getMinecraft().font, value, xx, yy, hovered ? hover.packed : color.packed, shadow);
		}
		else{
			float scale = this.scale < 0.0F ? width / (float)textwidth : this.scale;
			uui.matrix.pose().pushPose();
			uui.matrix.pose().translate(xx, yy, 0.0F);
			uui.matrix.pose().scale(scale, scale, scale);
			uui.matrix.drawString(uui.getMinecraft().font, value, 0, 0, hovered ? hover.packed : color.packed, shadow);
			uui.matrix.pose().popPose();
		}
	}

}