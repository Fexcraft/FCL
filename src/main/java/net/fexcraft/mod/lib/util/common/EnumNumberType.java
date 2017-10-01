package net.fexcraft.mod.lib.util.common;

public enum EnumNumberType{
	
	FLOAT, INT, DOUBLE, LONG, SHORT, BYTE;
	
	EnumNumberType(){}
	
	public String toString(){
		switch(this){
		case BYTE:   return "byte";
		case DOUBLE: return "double";
		case FLOAT:  return "float";
		case INT:    return "int";
		case LONG:   return "long";
		case SHORT:  return "short";
		default:     return "null";
		}
	}
	
	public EnumNumberType fromString(String s){
		switch(s){
			case "byte":   return BYTE;
			case "double": return DOUBLE;
			case "float":  return FLOAT;
			case "int":    return INT;
			case "long":   return LONG;
			case "short":  return SHORT;
			default: return null;
		}
	}
	
}