package net.fexcraft.mod.lib.perms;

import java.math.BigDecimal;

import com.google.gson.JsonElement;

public class PermissionString extends PermissionNode {

	public PermissionString(String id, Type type, Object value){
		super(id, type, value);
	}

	public PermissionString(String id, JsonElement elm){
		super(id, elm);
	}

	@Override
	public boolean equals(PermissionNode node){
		if(!node.isBoolean()){
			if(node.isString()){
				return this.getStringValue().equals(node.getStringValue());
			}
			if(node.isNumber()){
				return this.getStringValue().equals(node.getNumberValue() + "");
			}
		}
		return this.get().equals(node.getValue() + "");
	}
	
	public String get(){
		return this.getStringValue();
	}

	@Override
	public String getStringValue(){
		return (String)value;
	}

	@Override
	public Number getNumberValue(){
		try{
			BigDecimal bd = new BigDecimal((String)value);
			return bd;
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Boolean getBooleanValue(){
		return Boolean.parseBoolean((String)value);
	}
	
}