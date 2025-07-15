/*
 * Copyright (c) 2022, Ferdinand Calo' / "Fexcraft". All rights reserved.
 *
 *
 */
package net.fexcraft.lib.script.elm;

import net.fexcraft.lib.script.ScrElm;
import net.fexcraft.lib.script.ScrElmType;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class NullElm implements ScrElm {

	public NullElm(){}

	@Override
	public String scr_str(){
		return "null";
	}

	@Override
	public ScrElmType scr_type(){
		return ScrElmType.NULL;
	}

}
