package net.fexcraft.mod.uni.ui;

import java.util.LinkedHashMap;

import net.fexcraft.app.json.JsonMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class UITab extends UIButton {

	public static Class<? extends UITab> IMPLEMENTATION;
	//
	public LinkedHashMap<String, UIText> texts = new LinkedHashMap<>();
	public LinkedHashMap<String, UIButton> buttons = new LinkedHashMap<>();
	public LinkedHashMap<String, UIField> fields = new LinkedHashMap<>();
	public LinkedHashMap<String, UISlot> slots = new LinkedHashMap<>();

	public UITab(UserInterface ui, JsonMap map) throws Exception {
		super(ui, map);
		if(map.has("buttons")){
			map.getArray("buttons").value.forEach(json -> {
				buttons.put(json.string_value(), ui.buttons.get(json.string_value()));
			});
		}
		if(map.has("texts")){
			map.getArray("texts").value.forEach(json -> {
				texts.put(json.string_value(), ui.texts.get(json.string_value()));
			});
		}
		if(map.has("fields")){
			map.getArray("fields").value.forEach(json -> {
				fields.put(json.string_value(), ui.fields.get(json.string_value()));
			});
		}
		if(map.has("slots")){
			map.getArray("slots").value.forEach(json -> {
				slots.put(json.string_value(), ui.slots.get(json.string_value()));
			});
		}
	}

}
