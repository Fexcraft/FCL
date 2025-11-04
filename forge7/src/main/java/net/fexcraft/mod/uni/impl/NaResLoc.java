package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.minecraft.util.ResourceLocation;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class NaResLoc extends ResourceLocation implements IDL {

	private String name;

    public NaResLoc(String name, String domain, String path){
        super(domain, path);
		this.name = name;
    }

    public NaResLoc(String name, ResourceLocation rs){
        super(rs.toString());
		this.name = name;
    }

    public NaResLoc(String onestring){
        super(onestring);
    }

    @Override
    public String space(){
        return getResourceDomain();
    }

    @Override
    public String id(){
        return getResourcePath();
    }

    @Override
    public String name(){
        return name;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof IDL){
            return colon().equals(((IDL)obj).colon());
        }
        else return colon().equals(obj.toString());
    }

}
