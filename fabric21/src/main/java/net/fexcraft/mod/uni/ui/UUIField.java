package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.ui.UIElement;
import net.fexcraft.mod.uni.ui.UIField;
import net.fexcraft.mod.uni.ui.UniUI;
import net.fexcraft.mod.uni.ui.UserInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UUIField extends UIField {

	protected EditBox field;

	public UUIField(UserInterface ui, JsonMap map) throws Exception{
		super(ui, map);
	}

	@Override
	public void draw(Object gui, UIElement root, float ticks, int gl, int gt, int mx, int my){
		if(field == null || !visible()) return;
		field.setX(absolute ? x < 0 ? ui.screen_width + x : x : gl + x);
		field.setY(absolute ? y < 0 ? ui.screen_height + y : y : gt + y);
	}

	public void init(){
		field = new EditBox(Minecraft.getInstance().font, 0, 0, width, height, null){
			@Override
			public void setValue(String text){
				if(regex != null) text = text.replaceAll(regex, "");
				super.setValue(text);
			}
		};
		field.setBordered(background);
		field.setTextColor(color);
		field.setValue(value);
		field.active = enabled;
		field.setVisible(visible);
		field.setMaxLength(maxlength);
	}

	public boolean visible(){
		return field.isVisible();
	}


	public void visible(boolean bool){
		field.setVisible(visible = bool);
	}

	public void enabled(boolean bool){
		field.active = enabled = bool;
	}

	public boolean onclick(int mx, int my, int mb){
		boolean bool = field.mouseClicked(mx, my, mb);
		UniUI.INST.setFocused(field);
		return bool;
	}

	public boolean keytyped(char c, int code){
		return field.charTyped(c, code);
	}

	public void text(String text){
		field.setValue(value = text);
	}

	public String text(){
		return field.getValue();
	}

	public void maxlength(int nl){
		field.setMaxLength(maxlength = nl);
	}

}