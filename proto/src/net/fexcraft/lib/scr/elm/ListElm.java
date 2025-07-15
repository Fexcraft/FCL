/*
 * Copyright (c) 2023, Ferdinand Calo' / "Fexcraft". All rights reserved.
 *
 *
 */
package net.fexcraft.lib.scr.elm;

import java.util.ArrayList;

import net.fexcraft.lib.scr.ScriptElm;
import net.fexcraft.lib.scr.ScriptElmType;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class ListElm implements ScriptElm {
	
	private ArrayList<ScriptElm> value = new ArrayList<>();

	public ListElm(){}

	@Override
	public String scr_str(){
		return value.toString();
	}

	@Override
	public int scr_int(){
		return value.size();
	}

	@Override
	public float scr_flt(){
		return value.size();
	}

	@Override
	public boolean scr_bln(){
		return value.size() > 1;
	}

	@Override
	public ScriptElmType scr_type(){
		return ScriptElmType.LIST;
	}

	public ArrayList<ScriptElm> value(){
		return value;
	}

	@Override
	public ScriptElm scr_get(int idx){
		if(idx < 0 || idx >= value.size()) return NULL;
		ScriptElm elm = value.get(idx);
		return elm == null ? NULL : elm;
	}

	@Override
	public void scr_set(int idx, ScriptElm val, boolean ov){
		ScriptElm elm = value.get(idx);
		if(elm == null || (ov && elm.replaceable())) value.set(idx, val);
		else elm.scr_set(val);
	}

	@Override
	public ScriptElm exec(String str, ScriptElm... args){
		return NULL;
	}

}
