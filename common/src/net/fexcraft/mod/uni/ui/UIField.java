package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UIField extends UIElement {

	public static Class<? extends UIField> IMPLEMENTATION;
	public static NumberFormat nf = NumberFormat.getInstance(Locale.US);
	public static final DecimalFormat df = new DecimalFormat("#.####");
	static {
		nf.setMaximumFractionDigits(4);
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
	}
	//
	public String initial_value;
	public String value;
	//public String regex;
	public boolean background;
	public boolean number;
	//public float scale;
	public int color;
	public int maxlength;

	public UIField(UserInterface ui, JsonMap map) throws Exception {
		super(ui, map);
		initial_value = value = map.getString("value", "");
		//scale = map.getFloat("scale", 1);
		//if(map.getBoolean("autoscale", false)) scale = -1;
		background = map.getBoolean("background", false);
		color = Integer.parseInt(map.getString("color", "f0f0f0"), 16);
		if(map.has("numberfield")) number = true; //regex = "[^\\d\\-\\.\\,]";
		maxlength = map.getInteger("max-length", 32);
	}

	public boolean onclick(int mx, int my, int mb){
		return false;
	}

	public boolean keytyped(char c, int code){
		return false;
	}

	public void text(Number num){
		text(df.format(num));
	}

	public void text(String text){
		//
	}

	public void maxlength(int nl){
		//
	}

	public String text(){
		return value;
	}

	public float number(){
		return _float();
	}

	public int integer(){
		try{
			return nf.parse(text()).intValue();
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	public float _float(){
		try{
			return (float)nf.parse(text()).doubleValue();
		}
		catch(Exception e){
			e.printStackTrace();
			return 0f;
		}
	}

	public double _double(){
		try{
			return nf.parse(text()).doubleValue();
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

}
