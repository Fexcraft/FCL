package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.item.StackWrapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UserInterface {

	public ContainerInterface container;
	public LinkedHashMap<String, UIText> texts = new LinkedHashMap<>();
	public LinkedHashMap<String, UIButton> buttons = new LinkedHashMap<>();
	public LinkedHashMap<String, UIField> fields = new LinkedHashMap<>();
	public LinkedHashMap<String, UISlot> slots = new LinkedHashMap<>();
	public LinkedHashMap<String, UITab> tabs = new LinkedHashMap<>();
	public boolean background;
	public String returnto;
	public Drawer drawer;
	public int width;
	public int height;
	public int _fields;
	public int screen_width;
	public int screen_height;
	public int gLeft;
	public int gTop;
	public UniUI root;

	public UserInterface(JsonMap map, ContainerInterface container) throws Exception {
		this.container = container.set(this);
		if(map.has("texts")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("texts").entries()){
				texts.put(entry.getKey(), UIElement.create(UIText.IMPLEMENTATION, this, entry.getValue().asMap()));
			}
		}
		if(map.has("buttons")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("buttons").entries()){
				buttons.put(entry.getKey(), UIElement.create(UIButton.IMPLEMENTATION, this, entry.getValue().asMap()));
			}
		}
		if(map.has("fields")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("fields").entries()){
				fields.put(entry.getKey(), UIElement.create(UIField.IMPLEMENTATION, this, entry.getValue().asMap()));
			}
		}
		if(map.has("slots")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("slots").entries()){
				slots.put(entry.getKey(), new UISlot(this, entry.getValue().asMap()));
			}
		}
		if(map.has("tabs")){
			for(Entry<String, JsonValue<?>> entry : map.getMap("tabs").entries()){
				tabs.put(entry.getKey(), UIElement.create(UITab.IMPLEMENTATION, this, entry.getValue().asMap()));
			}
		}
		else{
			UITab main = UITab.IMPLEMENTATION.getConstructor(UserInterface.class, JsonMap.class).newInstance(this, map);
			main.texts.putAll(texts);
			main.buttons.putAll(buttons);
			main.fields.putAll(fields);
			main.slots.putAll(slots);
			tabs.put("main", main);
		}
		background = map.getBoolean("background", true);
		JsonArray arr = map.getArray("size");
		width = arr.get(0).integer_value();
		height = arr.get(1).integer_value();
		returnto = map.getString("return", null);
	}

	public boolean onClick(int mx, int my, int mb){
		UIButton button = null;
		for(UITab tab : tabs.values()){
			if(!tab.visible || !tab.enabled) continue;
			for(Entry<String, UIButton> entry : buttons.entrySet()){
				button = entry.getValue();
				if(!button.visible || !button.enabled) continue;
				if(!button.hovered(gLeft, gTop, mx, my)) continue;
				return button.onclick(gLeft, gTop, mx, my, mb) || onAction(button, entry.getKey(), mx, my, mb);
			}
			for(UIField field : fields.values()){
				if(!field.visible() /*|| !field.enabled()*/) continue;
				if(field.hovered(gLeft, gTop, mx, my) && field.onclick(mx, my, mb)) return true;
			}
		}
		return false;
	}

	public boolean onAction(UIButton button, String id, int x, int y, int b){
		return false;
	}

	public boolean onScroll(UIButton button, String id, int mx, int my, int am){
		return false;
	}

	public void getTooltip(int mx, int my, List<String> list){}

	public void predraw(float ticks, int mx, int my){}

	public void drawbackground(float ticks, int mx, int my){}

	public void postdraw(float ticks, int mx, int my){}

	public void scrollwheel(int am, int mx, int my){}

	public boolean keytyped(char c, int code){
		return false;
	}

	public void init(){}

	public static interface Drawer {

		public void draw(float x, float y, int u, int v, int w, int h);

		public void drawFull(float x, float y, int w, int h);

		public void draw(int x, int y, StackWrapper stack);

		public void bind(IDL texture);

		public default void bindTabTex(UserInterface ui, String tab){
			bind(ui.tabs.get(tab).texture);
		}

		public void apply(RGB color);

		public default void applyWhite(){
			apply(RGB.WHITE);
		}

		public String translate(String str, Object... args);

		public IDL loadExternal(String urltex);

	}

}
