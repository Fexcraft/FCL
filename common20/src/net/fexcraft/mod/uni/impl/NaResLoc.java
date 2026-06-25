package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class NaResLoc extends ResourceLocation implements IDL {

	private String name;

	public NaResLoc(String name, String domain, String path){
		super(domain, path);
		if(name == null) name = "Unnamed";
		this.name = name;
	}

	public NaResLoc(String onestring){
		super(onestring.contains(";") ? onestring.split(";")[1] : onestring);
		name = onestring.contains(";") ? onestring.split(";")[0] : "Unnamed";
	}

	@Override
	public String space(){
		return getNamespace();
	}

	@Override
	public String id(){
		return getPath();
	}

	@Override
	public String name(){
		return name;
	}

}