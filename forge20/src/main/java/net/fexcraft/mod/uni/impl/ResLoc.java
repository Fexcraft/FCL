package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ResLoc extends ResourceLocation implements IDL {

	public ResLoc(String name){
		super(name);
	}

	public ResLoc(String space, String path){
		super(space, path);
	}

	public String space(){
		return getNamespace();
	}

	public String id(){
		return getPath();
	}

}