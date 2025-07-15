package net.fexcraft.lib.scr;

/**
 *
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public enum ScriptElmType {

    NULL, STRING, INTEGER, FLOAT, BOOLEAN, LIST, MAP, OBJ, REF;

    public boolean primitive(){
        return !list() && !map();
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

    public boolean map(){
        return this == MAP || this == OBJ;
    }

    public boolean list(){
        return this == LIST;
    }

}
