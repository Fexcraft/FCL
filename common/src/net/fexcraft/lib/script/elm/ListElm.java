/*
 * Copyright (c) 2022, Ferdinand Calo' / "Fexcraft". All rights reserved.
 *
 *
 */
package net.fexcraft.lib.script.elm;

import java.util.ArrayList;

import net.fexcraft.lib.script.ScrElm;
import net.fexcraft.lib.script.ScrElmType;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class ListElm implements ScrElm {
	
	private ArrayList<ScrElm> value = new ArrayList<>();

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
	public ScrElmType scr_type(){
		return ScrElmType.LIST;
	}

	public ArrayList<ScrElm> value(){
		return value;
	}

}
