/*
 * Copyright (c) 2023, Ferdinand Calo'. All rights reserved.
 *
 *
 */
package net.fexcraft.lib.scr;

import net.fexcraft.lib.script.ScrElm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * "FEX Script" Version 2.0
 *
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class Script {

    public ArrayList<String> deps = new ArrayList<>();
    public HashMap<String, ScriptElm> global = new LinkedHashMap<>();
    public HashMap<String, ScriptAction> actions = new LinkedHashMap<>();
    public String name;

    public Script(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return print();
    }

    private String print(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(name + " {\n");
        for(String dep : deps){
            buffer.append("    requires " + dep + "\n");
        }
        if(deps.size() > 0) buffer.append("\n");
        for(Map.Entry<String, ScriptElm> entry : global.entrySet()){
            buffer.append("    " + entry.getValue().scr_type() + " " + entry.getKey());
            buffer.append(entry.getValue().scr_str().length() == 0 ? "\n" : " = " + entry.getValue().scr_str() + "\n");
        }
        if(global.size() > 0) buffer.append("\n");
        for(Map.Entry<String, ScriptAction> entry : actions.entrySet()){
            buffer.append(entry.getValue().print(1));
        }
        buffer.append("}\n");
        return buffer.toString();
    }

}
