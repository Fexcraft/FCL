/*
 * Copyright (c) 2023, Ferdinand Calo'. All rights reserved.
 *
 *
 */
package net.fexcraft.lib.scr;

import java.util.HashMap;

/**
 *
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class ScriptAction extends ScriptBlock {

    public final String name;

    public ScriptAction(String name, String... params){
        super(params);
        this.name = name;
    }

    @Override
    public String print_name(){
        return "act " + name;
    }

}
