package net.fexcraft.mod.lib.util.render;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.google.gson.JsonObject;

import net.fexcraft.mod.lib.util.common.Static;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RGB {
	
	public byte red, green, blue;
	
	public static final RGB RED   = new RGB(255,   0,   0);
	public static final RGB GREEN = new RGB(  0, 255,   0);
	public static final RGB BLUE  = new RGB(  0,   0, 255);
	public static final RGB BLACK = new RGB(  0,   0,   0);
	public static final RGB WHITE = new RGB(255, 255, 255);
	
	public RGB(){
		copyFrom(WHITE);
	}
	
	public RGB(byte r, byte g, byte b){
		this.red = r;
		this.green = g;
		this.blue = b;
	}
	
	public RGB(int r, int g, int b){
		r -= 128; g -= 128; b -= 128;
		this.red   = con(r);
		this.green = con(g);
		this.blue  = con(b);
	}
	
	public RGB(byte[] i){
		this.red   = i.length >= 1 ? i[0] : 0;
		this.green = i.length >= 2 ? i[1] : 0;
		this.blue  = i.length >= 3 ? i[2] : 0;
	}
	
	public RGB(int[] i){
		this.red   = con(i.length >= 1 ? i[0] - 128 : 0);
		this.green = con(i.length >= 2 ? i[1] - 128 : 0);
		this.blue  = con(i.length >= 3 ? i[2] - 128 : 0);
	}
	
	public RGB(String x, String y, String z){
		try{ this.red   = con(Integer.parseInt(x) - 128); } catch(Exception e){ this.red   = 0; }
		try{ this.green = con(Integer.parseInt(y) - 128); } catch(Exception e){ this.green = 0; }
		try{ this.blue  = con(Integer.parseInt(z) - 128); } catch(Exception e){ this.blue  = 0; }
	}
	
	public RGB(String[] s){
		if(s.length >= 1){ try{ this.red   = con(Integer.parseInt(s[0]) - 128); } catch(Exception e){ this.red   = 0; } } else {this.red   = 0; }
		if(s.length >= 2){ try{ this.green = con(Integer.parseInt(s[1]) - 128); } catch(Exception e){ this.green = 0; } } else {this.green = 0; }
		if(s.length >= 3){ try{ this.blue  = con(Integer.parseInt(s[2]) - 128); } catch(Exception e){ this.blue  = 0; } } else {this.blue  = 0; }
	}
	
	public RGB(RGB rgb){
		this.red = rgb.red;
		this.blue = rgb.blue;
		this.green = rgb.green;
	}
	
	private static final byte con(int i){
		return (byte)((i > 127 ? 127 : i < -128 ? -128 : i) & 0xFF);
	}

	public final void copyFrom(RGB color){
		red = color.red; green = color.green; blue = color.blue;
	}
	
	@SideOnly(Side.CLIENT)
	public void glColorApply(){
		org.lwjgl.opengl.GL11.glColor3b(red, green, blue);
	}
	
	@SideOnly(Side.CLIENT)
	public final static void glColorReset(){
		org.lwjgl.opengl.GL11.glColor3b(Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE);
	}
	
	public final void fromDyeColor(EnumDyeColor e){
		int c = Static.side().isClient() ? e.getColorValue() : get(e);
		blue  = con(c & 255);
		green = con((c >> 8) & 255);
		red   = con((c >> 16) & 255);
	}
	
	private static final int get(EnumDyeColor e){
		switch(e){
			case WHITE: return 16383998;
			case ORANGE: return 16351261;
			case MAGENTA: return 13061821;
			case LIGHT_BLUE: return 3847130;
			case YELLOW: return 16701501;
			case LIME: return 8439583;
			case PINK: return 15961002;
			case GRAY: return 4673362;
			case SILVER: return 10329495;
			case CYAN: return 1481884;
			case PURPLE: return 8991416;
			case BLUE: return 3949738;
			case BROWN: return 8606770;
			case GREEN: return 6192150;
			case RED: return 11546150;
			case BLACK: default: return 1908001;
		}
	}

	public static RGB fromSingleInt(int i){
		return new RGB(con((i >> 16) & 255), con((i >> 8) & 255), con(i & 255));
	}

	/**
	 * @param a additional name data to append into the nbt tag key
	 */
	public final NBTTagCompound writeToNBT(NBTTagCompound tag, String a){
		try{
			String s = a == null ? "" : "_" + a;
			tag.setByte("RGB_Red" + s, red);
			tag.setByte("RGB_Green" + s, green);
			tag.setByte("RGB_Blue" + s, blue);
			return tag;
		}
		catch(Exception e){
			e.printStackTrace();
			return tag;
		}
	}
	
	/**
	 * @param tag additional name data to retrieve the right nbt key.
	 */
	public final void readFromNBT(NBTTagCompound tag, String a){
		try{
			String s = a == null ? "" : "_" + a;
			red = tag.getByte("RGB_Red" + s);
			green = tag.getByte("RGB_Green" + s);
			blue = tag.getByte("RGB_Blue" + s);
		}
		catch(Exception e){
			e.printStackTrace();
			copyFrom(WHITE);
		}
	}
	
	@Override
	public final String toString(){
		return "[" + red + ":" + green + ":" + blue + "]";
	}

	public final int toSingleInteger(){
		return (65536 * red) + (256 * green) + blue;
	}

	public final JsonObject toJsonObject(){
		JsonObject obj = new JsonObject();
		obj.addProperty("Red", red + 128);
		obj.addProperty("Blue", blue + 128);
		obj.addProperty("Green", green + 128);
		return obj;
	}
	
	/// JSON ///
	
	public RGB(JsonObject object){
		this(object, false);
	}

	public RGB(JsonObject object, boolean write){
		String[] red = {"Red", "red", "r", "R"};
		String[] blue = {"Blue", "blue", "b", "B"};
		String[] green = {"Green", "green", "g", "G"};
		this.red = getFJO(red, object, write, 0);
		this.blue = getFJO(blue, object, write, 0);
		this.green = getFJO(green, object, write, 0);
	}
	
	private static final byte getFJO(String[] strings, JsonObject obj, boolean write, int i){
		for(String s : strings){
			if(obj.has(s)){
				return con(obj.get(s).getAsInt() - 128);
			}
		}
		if(write){
			obj.addProperty(strings[i], 0);
		}
		return 0;
	}
	
	/// Math ///

	public final void add(int i, int j){
		switch(i){
			case 0: { red   = con(red   + j); break;}
			case 1: { green = con(green + j); break;}
			case 2: { blue  = con(blue  + j); break;}
		}
	}
	
	public final void addAll(int j){
		add(0, j); add(1, j); add(2, j);
	}
	
	public final void set(int i, int j){
		switch(i){
			case 0: { red   = con(j); break;}
			case 1: { green = con(j); break;}
			case 2: { blue  = con(j); break;}
		}
	}
	
	public final void setAll(int j){
		set(0, j); set(1, j); set(2, j);
	}
	
	/// OTHER ///
	
	private static final DecimalFormat df = new DecimalFormat("##.#####");
	static { df.setRoundingMode(RoundingMode.DOWN); }
	
	public static String format(double d){
		return df.format(d);
	}
	
}