package net.fexcraft.mod.lib.api.entity;

import net.fexcraft.mod.lib.util.common.EnumColor;
import net.minecraft.item.EnumDyeColor;

public enum RoboType{
	RED("red", EnumColor.RED, 'R'),
	BLUE("blue", EnumColor.BLUE, 'B'),
	GREEN("green", EnumColor.GREEN, 'G'),
	YELLOW("yellow", EnumColor.YELLOW, 'Y'),
	GUARD("guard", EnumColor.WHITE, 'P'),
	NEUTRAL("neutral", EnumColor.LIGHT_GRAY, 'N'),
	HOSTILE("hostile", EnumColor.BLACK, 'H');
	
	private String name;
	private EnumColor color;
	private char sn;
	
	RoboType(String name, EnumColor color, char sn){
		this.name = name;
		this.color = color;
		this.sn = sn;
	}
	
	public String getName(){
		return name;
	}
	
	public EnumColor getColor(){
		return color;
	}
	
	public char getShortName(){
		return sn;
	}
	
	public char getChar(){
		return sn;
	}
	
	public static RoboType fromString(String string){
		for(RoboType type : RoboType.values()){
			if(type.getName().equals(string)){
				return type;
			}
		}
		return NEUTRAL;
	}
	
	public static RoboType fromChar(char cr){
		for(RoboType type : RoboType.values()){
			if(type.getShortName() == cr){
				return type;
			}
		}
		return NEUTRAL;
	}
	
	public static RoboType fromColor(EnumColor color){
		for(RoboType type : RoboType.values()){
			if(type.getColor().equals(color)){
				return type;
			}
		}
		return NEUTRAL;
	}
	
	public static RoboType fromDyeColor(EnumDyeColor color){
		return fromColor(EnumColor.fromDyeColor(color));
	}
}