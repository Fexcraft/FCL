package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.RGB;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UIText extends UIElement {

	public static Class<? extends UIText> IMPLEMENTATION;
	//
	public String initial_value;
	protected String value;
	public boolean shadow;
	public boolean translate;
	public boolean centered;
	public float scale;
	public RGB color = new RGB();
	public RGB hover = new RGB();

	public UIText(UserInterface ui, JsonMap map) throws Exception {
		super(ui, map);
		initial_value = value = map.getString("value", "");
		scale = map.getFloat("scale", 1);
		if(map.getBoolean("autoscale", false)) scale = -1;
		shadow = map.getBoolean("shadow", false);
		color.packed = Integer.parseInt(map.getString("color", "f0f0f0").replace("#", ""), 16);
		if(map.getBoolean("hoverable", true)){
			hover.packed = map.getInteger("hover", 0xcfb117);
		}
		translate = map.getBoolean("translate", false);
		if(translate) translate();
		centered = map.getBoolean("centered", false);
	}

	public void translate(){}

	public void translate(Object... objects){}

	public boolean onscroll(int l, int t, int x, int y, int am){
		return false;
	}

	public void value(String newval){
		value = newval;
	}

	public String value(){
		return value;
	}

}
