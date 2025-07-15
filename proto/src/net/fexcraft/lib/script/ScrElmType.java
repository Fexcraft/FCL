package net.fexcraft.lib.script;

/**
 *
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public enum ScrElmType {

    NULL, STRING, INTEGER, FLOAT, BOOLEAN, LIST, OBJ, REF;

    public boolean primitive(){
        return !(this == LIST || this == OBJ);
    }

    public static ScrElmType from(String str){
        switch(str){
            case "str": return STRING;
            case "int": return INTEGER;
            case "flt": return FLOAT;
            case "bln": return BOOLEAN;
            case "lis": return LIST;
            case "obj": return OBJ;
            case "ref": return REF;
        }
        return NULL;
    }

    public boolean decimal(){
        return this == FLOAT;
    }

    public boolean bool(){
        return this == BOOLEAN;
    }

    public boolean integer(){
        return this == INTEGER;
    }

    public boolean string(){
        return this == STRING;
    }

    public boolean reference(){
        return this == REF;
    }

    public boolean number(){
        return this == INTEGER || this == FLOAT;
    }

}
