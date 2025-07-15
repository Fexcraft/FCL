/*
 * Copyright (c) 2023, Ferdinand Calo' / "Fexcraft". All rights reserved.
 *
 *
 */
package net.fexcraft.lib.scr.elm;

import net.fexcraft.lib.scr.ScriptElm;
import net.fexcraft.lib.scr.ScriptElmType;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class FltElm implements ScriptElm {
	
	private float value;

	public FltElm(float flaot){
		value = flaot;
	}

	@Override
	public String scr_str(){
		return value + "";
	}

	@Override
	public int scr_int(){
		return (int)value;
	}

	@Override
	public float scr_flt(){
		return value;
	}

	@Override
	public boolean scr_bln(){
		return value > 0;
	}

	@Override
	public ScriptElmType scr_type(){
		return ScriptElmType.FLOAT;
	}

	@Override
	public void scr_set(String val){
		try{
			value = Float.parseFloat(val);
		}
		catch(Exception e){
			//
		}
	}

	@Override
	public void scr_set(int val){
		value = val;
	}

	@Override
	public void scr_set(float val){
		value = val;
	}

	@Override
	public void scr_set(boolean val){
		value = val ? 1 : 0;
	}

}
