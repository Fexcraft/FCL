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
public class BoolElm implements ScriptElm {
	
	private boolean value;

	public BoolElm(boolean bool){
		value = bool;
	}

	@Override
	public String scr_str(){
		return value + "";
	}

	@Override
	public int scr_int(){
		return value ? 0 : 1;
	}

	@Override
	public float scr_flt(){
		return value ? 0f : 1f;
	}

	@Override
	public boolean scr_bln(){
		return value;
	}

	@Override
	public ScriptElmType scr_type(){
		return ScriptElmType.BOOLEAN;
	}

	@Override
	public void scr_set(String val){
		value = val.toLowerCase().equals("true");
	}

	@Override
	public void scr_set(int val){
		value = val > 0;
	}

	@Override
	public void scr_set(float val){
		value = val > 0;
	}

	@Override
	public void scr_set(boolean val){
		value = val;
	}
	
	public static class Final extends BoolElm {
		
		public Final(boolean bool){
			super(bool);
		}

		@Override
		public void scr_set(String val){
			//
		}

		@Override
		public void scr_set(int val){
			//
		}

		@Override
		public void scr_set(float val){
			//
		}

		@Override
		public void scr_set(boolean val){
			//
		}
		
	}

}
