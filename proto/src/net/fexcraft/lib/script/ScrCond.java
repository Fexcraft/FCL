/*
 * Copyright (c) 2022, Ferdinand Calo' / "Fexcraft". All rights reserved.
 *
 *
 */
package net.fexcraft.lib.script;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class ScrCond {

	private ScrBlock block;
	private ScrExpr cond;

	public ScrCond(ScrBlock root, String cond){
		this.block = root;
		this.cond = Script.parseExpression(cond, ScrExprType.EXT_COND);
	}

	public String print(){
		return cond.print();
	}

	public boolean isMet(){
		return cond.process(block.root, null, ScrElm.NULL).scr_bln();
	}

}
