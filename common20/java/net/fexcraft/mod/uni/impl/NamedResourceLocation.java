package net.fexcraft.mod.uni.impl;

import net.minecraft.resources.ResourceLocation;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class NamedResourceLocation extends ResourceLocation {

	private static final String defname = "Unnamed";
	private String name;

	public NamedResourceLocation(String name, String domain, String path){
		super(domain, path);
		if(name == null) name = "Unnamed";
		this.name = name;
	}

	public NamedResourceLocation(String name, ResourceLocation rs){
		this(name, rs.getNamespace(), rs.getPath());
	}

	public NamedResourceLocation(String onestring){
		super(onestring.contains(";") ? onestring.split(";")[1] : onestring);
		name = onestring.contains(";") ? onestring.split(";")[0] : "Unnamed";
	}

	public String getName(){
		return name;
	}

	public NamedResourceLocation setName(String newname){
		name = newname;
		return this;
	}

}