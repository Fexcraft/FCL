package net.fexcraft.mod.lib.perms;

import com.google.gson.JsonElement;

public class PermissionBool extends PermissionNode {

	public PermissionBool(String id, Type type, Object def_value){
		super(id, type, def_value);
	}

	public PermissionBool(String id, JsonElement elm){
		super(id, elm);
	}

	@Override
	public boolean equals(PermissionNode node){
		if(node.isBoolean()){
			return this.getBooleanValue().equals(node.getBooleanValue());
		}
		if(node.isString()){
			return this.getBooleanValue().equals(Boolean.parseBoolean(node.getStringValue()));
		}
		if(node.isNumber()){
			try{
				return this.getBooleanValue() == (node.getNumberValue().intValue() == 1);
			}
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public boolean get(){
		return this.getBooleanValue();
	}

	@Override
	public String getStringValue(){
		return (boolean)value + "";
	}

	@Override
	public Number getNumberValue(){
		return (boolean)value == true ? 1 : 0;
	}

	@Override
	public Boolean getBooleanValue(){
		return (boolean)value;
	}
	
}