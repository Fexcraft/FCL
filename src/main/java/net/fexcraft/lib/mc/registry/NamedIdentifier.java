package net.fexcraft.lib.mc.registry;

import net.minecraft.util.Identifier;

/** 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class NamedIdentifier extends Identifier {
	
	private static final String defname = "Unnamed";
	private String name;

	public NamedIdentifier(String name, String domain, String path){
		super(domain, path); if(name == null) name = defname; this.name = name;
    }

	public NamedIdentifier(String name, Identifier id){
		this(name, id.getNamespace(), id.getPath());
	}
	
	public NamedIdentifier(String onestring){
		super(onestring.contains(";") ? onestring.split(";")[1] : onestring);
		name = onestring.contains(";") ? onestring.split(";")[0] : defname;
	}

	public String getName(){
		return name;
	}
	
	public NamedIdentifier setName(String newname){
		this.name = newname; return this;
	}
	
}