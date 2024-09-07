package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class NaResLoc extends NamedResourceLocation implements IDL {

	public NaResLoc(String name, String domain, String path){
		super(name, domain, path);
	}

	public NaResLoc(String name, ResourceLocation rs){
		super(name, rs);
	}

	public NaResLoc(String onestring){
		super(onestring);
	}

	public String space(){
		return getNamespace();
	}

	public String id(){
		return getPath();
	}

	public String name(){
		return getName();
	}

}