package net.fexcraft.mod.uni.impl;

import net.fexcraft.lib.mc.registry.NamedResourceLocation;
import net.fexcraft.mod.uni.IDL;
import net.minecraft.util.ResourceLocation;

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
        return getName();
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof IDL){
            return colon().equals(((IDL)obj).colon());
        }
        else return colon().equals(obj.toString());
    }

}
