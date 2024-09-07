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
public class NullElm implements ScriptElm {

	public NullElm(){}

	@Override
	public String scr_str(){
		return "null";
	}

	@Override
	public ScriptElmType scr_type(){
		return ScriptElmType.NULL;
	}

}
