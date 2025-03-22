package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ResLoc implements IDL {

	private ResourceLocation resloc;

	public ResLoc(String name){
		resloc = ResourceLocation.parse(name);
	}

	public ResLoc(String space, String path){
		resloc = ResourceLocation.tryBuild(space, path);
	}

	@Override
	public String space(){
		return resloc.getNamespace();
	}

	@Override
	public String id(){
		return resloc.getPath();
	}

	@Override
	public <Q> Q local(){
		return (Q)resloc;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof IDL || o instanceof String) return colon().equals(o.toString());
		return resloc.equals(o);
	}

	@Override
	public String toString(){
		return colon();
	}

}