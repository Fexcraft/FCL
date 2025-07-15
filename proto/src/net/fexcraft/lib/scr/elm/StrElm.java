/*
 * Copyright (c) 2023, Ferdinand Calo' / "Fexcraft". All rights reserved.
 *
 *
 */
package net.fexcraft.lib.scr.elm;

import net.fexcraft.lib.scr.ScriptElm;
import net.fexcraft.lib.scr.ScriptElmType;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class StrElm implements ScriptElm {
	
	private String value;

	public StrElm(String string){
		value = string;
	}

	@Override
	public String scr_str(){
		return value;
	}

	@Override
	public int scr_int(){
		if(NumberUtils.isParsable(value)){
			return Integer.parseInt(value);
		}
		return 0;
	}

	@Override
	public float scr_flt(){
		if(NumberUtils.isParsable(value)){
			return Float.parseFloat(value);
		}
		return 0;
	}

	@Override
	public boolean scr_bln(){
		return value.equals("true") || value.equals("1");
	}

	@Override
	public ScriptElmType scr_type(){
		return ScriptElmType.STRING;
	}

	@Override
	public void scr_set(String val){
		value = val;
	}

	@Override
	public void scr_set(int val){
		value = val + "";
	}

	@Override
	public void scr_set(float val){
		value = val + "";
	}

	@Override
	public void scr_set(boolean val){
		value = val + "";
	}

}
