package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class UIElement {

	protected UserInterface ui;
	public int ox, oy;
	public int x, y;
	public int otx, oty;
	public int tx, ty;
	public int owidth, width;
	public int oheight, height;
	public boolean absolute;
	protected boolean visible;
	protected boolean enabled;
	protected boolean hovered;

	public UIElement(UserInterface ui, JsonMap map) throws Exception {
		this.ui = ui;
		absolute = map.getBoolean("absolute", false);
		JsonArray arr = null;
		if(map.has("pos")){
			arr = map.getArray("pos");
			otx = tx = ox = x = arr.get(0).integer_value();
			oty = ty = oy = y = arr.get(1).integer_value();
		}
		if(map.has("uv")){
			arr = map.getArray("uv");
			otx = tx = arr.get(0).integer_value();
			oty = ty = arr.get(1).integer_value();
		}
		boolean slot = this instanceof UISlot;
		arr = map.has("size") ? map.getArray("size") : new JsonArray(slot ? 18 : 10, slot? 18 : 10);
		owidth = width = arr.get(0).integer_value();
		oheight = height = arr.get(1).integer_value();
		visible = map.getBoolean("visible", true);
		enabled = map.getBoolean("enabled", true);
	}

	public boolean hovered(int l, int t, int mx, int my){
		if(absolute){
			boolean xx = x < 0 ? mx >= ui.screen_width + x && mx <= ui.screen_width + x + width : mx >= x && mx <= x + width;
			boolean yy = y < 0 ? my >= ui.screen_height + y && my <= ui.screen_height + y + height : my >= y && my <= y + height;
			return hovered = xx && yy;
		}
		else return hovered = mx >= l + x && mx <= l + x + width && my >= t + y && my <= t + y + height;
	}

	public void draw(Object ui, UIElement root, float ticks, int gl, int gt, int mx, int my){}

	public boolean visible(){
		return visible;
	}

	public void visible(boolean bool){
		visible = bool;
	}

	public boolean enabled(){
		return enabled;
	}

	public void enabled(boolean bool){
		enabled = bool;
	}

	public boolean hovered(){
		return hovered;
	}

	public static <E extends UIElement> E create(Class<? extends UIElement> clazz, UserInterface ui, JsonMap map) throws Exception {
		return (E)clazz.getConstructor(UserInterface.class, JsonMap.class).newInstance(ui, map);
	}

}
