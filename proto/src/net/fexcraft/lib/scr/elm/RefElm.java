/*
 * Copyright (c) 2023, Ferdinand Calo' / "Fexcraft". All rights reserved.
 *
 *
 */
package net.fexcraft.lib.scr.elm;

import net.fexcraft.lib.scr.ScriptElm;
import net.fexcraft.lib.scr.ScriptElmType;
import net.fexcraft.lib.script.ScrBlock;
import net.fexcraft.lib.script.ScrElm;
import net.fexcraft.lib.script.ScrExpr;
import net.fexcraft.lib.script.ScrExprType;
import net.fexcraft.lib.script.Script;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class RefElm implements ScriptElm {
	
	private String value;
	private boolean negative;
	private ScrExpr expr;

	public RefElm(String string, boolean neg){
		value = string;
		negative = neg;
		if(value.contains(".")){
			expr = Script.parseExpression(string, ScrExprType.EXT_RIGHT);
		}
	}

	@Override
	public String scr_str(){
		return value;
	}

	@Override
	public ScriptElmType scr_type(){
		return ScriptElmType.REF;
	}

	//@Override
	public String scr_print(){
		return expr == null ? "{" + value + "}" : "{" + expr.print() +  "}";
	}

	public boolean negative(){
		return negative;
	}

	public ScrElm getElm(ScrBlock block){
		//if(expr != null) return expr.process(block, null, NULL);
		return block.getElm(value, null);
	}

}
