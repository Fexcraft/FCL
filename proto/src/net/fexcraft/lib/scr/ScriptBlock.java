/*
 * Copyright (c) 2023, Ferdinand Calo'. All rights reserved.
 *
 *
 */
package net.fexcraft.lib.scr;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class ScriptBlock {

    public HashMap<String, ScriptElm> local = new LinkedHashMap<>();
    public final String[] parameters;

	public ScriptBlock(String[] params){
		parameters = params;
	}

	public String print(int depth){
        StringBuffer buffer = new StringBuffer();
        String tab = depth(depth);
        String tac = tab + "    ";
        buffer.append(tab + print_name());
        if(parameters.length > 0){
            buffer.append("(");
            for(int idx = 0; idx < parameters.length; idx++){
                buffer.append(parameters[idx]);
                if(idx < parameters.length - 1){
                    buffer.append(", ");
                }
            }
            buffer.append("){\n");
        }
        else{
            buffer.append("(){\n");
        }
        for(Map.Entry<String, ScriptElm> entry : local.entrySet()){
            buffer.append(tac + entry.getValue().scr_type() + " " + entry.getKey());
            buffer.append(entry.getValue().scr_str().length() == 0 ? "\n" : " = " + entry.getValue().scr_str() + "\n");
        }
        if(local.size() > 0) buffer.append("\n");
        //
        buffer.append(tab + "}\n");
        return buffer.toString();
    }

    public String print_name(){
        return "";
    }

    private String depth(int depth){
        StringBuffer buffer = new StringBuffer();
        while(depth > 0){
            buffer.append("    ");
            depth--;
        }
        return buffer.toString();
    }

    @Override
    public String toString(){
        return print(0);
    }

}
