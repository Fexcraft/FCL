package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class UIButton extends UIElement {

	public static Class<? extends UIButton> IMPLEMENTATION;
	//
	public UIText text;
	public String action;
	public String target;
	public String tooltip;
	public IDL texture;
	public boolean colorbased;
	public int htx, hty;
	public int dtx, dty;
	public RGB hcolor = new RGB(0xf5da42);
	public RGB ecolor = new RGB(0xffffff);
	public RGB dcolor = new RGB(0xa3a3a3);
	public RGB[][] palette = null;
	public int[] palsize = null;

	public UIButton(UserInterface ui, JsonMap map) throws Exception {
		super(ui, map);
		action = map.getString("action", null);
		target = map.getString("target", null);
		tooltip = map.getString("tooltip", null);
		if(tooltip != null) tooltip = ContainerInterface.TRANSLATOR.apply(tooltip);
		if(map.has("text")){
			text = UIText.IMPLEMENTATION.getConstructor(UserInterface.class, JsonMap.class).newInstance(ui, map.getMap("text"));
		}
		if(map.getBoolean("colorbased", true)){
			if(map.has("color")){
				ecolor.packed = hcolor.packed = dcolor.packed = Integer.parseInt(map.getString("color", "0"), 16);
			}
			ecolor.packed = Integer.parseInt(map.getString("e_color", Integer.toHexString(ecolor.packed)), 16);
			dcolor.packed = Integer.parseInt(map.getString("d_color", Integer.toHexString(dcolor.packed)), 16);
			hcolor.packed = Integer.parseInt(map.getString("h_color", Integer.toHexString(hcolor.packed)), 16);
			if(map.has("alpha")){
				ecolor.alpha = hcolor.alpha = dcolor.alpha = map.get("alpha").float_value();
			}
			ecolor.alpha = map.getFloat("e_alpha", ecolor.alpha);
			hcolor.alpha = map.getFloat("h_alpha", hcolor.alpha);
			dcolor.alpha = map.getFloat("d_alpha", dcolor.alpha);
			if(map.has("palette")){
				JsonArray pal = map.getArray("palette");
				palette = new RGB[pal.get(0).integer_value()][pal.get(1).integer_value()];
				palsize = new int[]{ pal.get(2).integer_value(), pal.get(3).integer_value() };
				for(int i = 0; i < palette.length; i++)
					for(int j = 0; j < palette[i].length; j++)
						palette[i][j] = RGB.WHITE.copy();//RGB.random();
			}
		}
		dtx = htx = tx;
		dty = hty = ty;
		if(map.has("uv")){
			JsonArray arr = map.getArray("uv");
			if(arr.size() > 2){
				htx = arr.get(2).integer_value();
				hty = arr.get(3).integer_value();
			}
			if(arr.size() > 4){
				dtx = arr.get(4).integer_value();
				dty = arr.get(5).integer_value();
			}
		}
		texture = map.has("texture") ? IDLManager.getIDLCached(map.get("texture").string_value()) : null;
	}

	public boolean clicked(int l, int t, int mx, int my){
		return enabled && visible && hovered(l, t, x, y);
	}

	public boolean onclick(int l, int t, int mx, int my, int mb){
		return false;
	}

	public boolean onscroll(int l, int t, int x, int y, int am){
		return false;
	}

}
