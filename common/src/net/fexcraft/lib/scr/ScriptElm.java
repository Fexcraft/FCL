/*
 * Copyright (c) 2023, Ferdinand Calo' / "Fexcraft". All rights reserved.
 *
 *
 */
package net.fexcraft.lib.scr;

import net.fexcraft.lib.scr.elm.BoolElm;
import net.fexcraft.lib.scr.elm.BoolElm.Final;
import net.fexcraft.lib.scr.elm.NullElm;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public abstract interface ScriptElm {

	public static final NullElm NULL = new NullElm();
	public static final BoolElm TRUE = new Final(true);
	public static final BoolElm FALSE = new Final(false);

	public default String scr_str(){
		return this.getClass().getName();
	}

	public default int scr_int(){
		return 0;
	}

	public default float scr_flt(){
		return 0f;
	}

	public default boolean scr_bln(){
		return false;
	}

	public default ScriptElm scr_get(String sub){
		return NULL;
	}

	public default ScriptElm scr_get(int idx){
		return NULL;
	}

	public default ScriptElmType scr_type(){
		return ScriptElmType.OBJ;
	}

	//

	public default void scr_set(String val){}

	public default void scr_set(int val){}

	public default void scr_set(float val){}

	public default void scr_set(boolean val){}

	public default void scr_set(ScriptElm nex){}

	public default void scr_set(String sub, ScriptElm val, boolean ov){}

	public default void scr_set(int idx, ScriptElm val, boolean ov){}

	//

	public default ScriptElm exec(String str, ScriptElm... args){
		return NULL;
	}

	public default boolean overrides(){
		return false;
	}

	public default boolean replaceable(){
		return true;
	}
}
