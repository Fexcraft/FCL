package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class NaResLoc extends ResLoc implements IDL {

	private static final String defname = "Unnamed";
	private String name;

	public NaResLoc(String str){
		super(str.contains(";") ? str.split(";")[1] : str);
		name = str.contains(";") ? str.split(";")[0] : defname;
	}

	@Override
	public String name(){
		return name;
	}

}