package net.fexcraft.lib.mc.registry;

import net.minecraft.util.ResourceLocation;

/** 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class NamedResourceLocation extends ResourceLocation {
	
	private static final String defname = "Unnamed";
	private String name;

	public NamedResourceLocation(String name, String domain, String path){
		super(domain, path); if(name == null) name = defname; this.name = name;
    }

	public NamedResourceLocation(String name, ResourceLocation rs){
		this(name, rs.getResourceDomain(), rs.getResourcePath());
	}
	
	public NamedResourceLocation(String onestring){
		super(onestring.contains(";") ? onestring.split(";")[1] : onestring);
		name = onestring.contains(";") ? onestring.split(";")[0] : defname;
	}

	public String getName(){
		return name;
	}
	
	public NamedResourceLocation setName(String newname){
		this.name = newname; return this;
	}
	
}