/*
 * Copyright (c) 2023, Ferdinand Calo'. All rights reserved.
 *
 *
 */
package net.fexcraft.lib.scr;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.BiConsumer;

import net.fexcraft.lib.common.utils.Print;
import net.fexcraft.lib.scr.elm.*;

/**
 *
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class ScriptParser {

    public static final boolean LOG = true;

    public static Script parse(String scr_name, InputStream stream){
        Script scr = new Script(scr_name);
        ISW isw = new ISW(stream);
        try{
            isw.next();
            while(isw.has()){
                isw.skipw();
                if(isw.starts('r') && found(isw, "requires")){
                    scr.deps.add(isw.till_end());
                    continue;
                }
                if(isw.starts('i') && found(isw, "int")){
                    parseInt(isw, (key, elm) -> scr.global.put(key, elm));
                    continue;
                }
                if(isw.starts('f') && found(isw, "flt")){
                    parseFlt(isw, (key, elm) -> scr.global.put(key, elm));
                    continue;
                }
                if(isw.starts('s') && found(isw, "str")){
                    parseStr(isw, (key, elm) -> scr.global.put(key, elm));
                    continue;
                }
                if(isw.starts('b') && found(isw, "bln")){
                    parseBln(isw, (key, elm) -> scr.global.put(key, elm));
                    continue;
                }
                if(isw.starts('m') && found(isw, "map")){
                    parseMap(isw, (key, elm) -> scr.global.put(key, elm));
                    continue;
                }
                if(isw.starts('l') && found(isw, "lst")){
                    parseList(isw, (key, elm) -> scr.global.put(key, elm));
                    continue;
                }
                if(isw.starts('a') && found(isw, "act")){
                    String name = isw.till('(');
                    ArrayList<String> parms = new ArrayList<>();
                    int line = isw.linenum;
                    while(isw.linenum == line){
                        String par = isw.till(',', ')').trim();
                        if(par.length() > 0) parms.add(par);
                    }
                    scr.actions.put(name, new ScriptAction(name, parms.toArray(new String[0])));
                    parseBlock(isw, scr, null, scr.actions.get(name));
                    continue;
                }
                isw.nextline();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return scr;
    }

    private static void parseInt(ISW isw, BiConsumer<String, ScriptElm> cons) throws IOException {
        String key = isw.till_space();
        IntElm elm = null;
        isw.skipw();
        if(isw.starts('=')){
            isw.next();
            isw.skipw();
            boolean hex = isw.starts('#');
            if(hex) isw.next();
            int num = isw.linenum;
            String val = isw.till_end();
            try{
                elm = new IntElm(Integer.parseInt(val, hex ? 16 : 10));
            }
            catch(Exception e){
                Print.console(">>> Failed to parse integer element value at line '" + num + "'!");
                e.printStackTrace();
            }
        }
        if(elm == null) elm = new IntElm(0);
        cons.accept(key, elm);
    }

    private static void parseFlt(ISW isw, BiConsumer<String, ScriptElm> cons) throws IOException {
        String key = isw.till_space();
        FltElm elm = null;
        isw.skipw();
        if(isw.starts('=')){
            isw.next();
            isw.skipw();
            int num = isw.linenum;
            String val = isw.till_end();
            try{
                elm = new FltElm(Float.parseFloat(val));
            }
            catch(Exception e){
                Print.console(">>> Failed to parse float element value at line '" + num + "'!");
                e.printStackTrace();
            }
        }
        if(elm == null) elm = new FltElm(0);
        cons.accept(key, elm);
    }

    private static void parseStr(ISW isw, BiConsumer<String, ScriptElm> cons) throws IOException {
        String key = isw.till_space();
        StrElm elm = new StrElm("");
        isw.skipw();
        if(isw.starts('=')){
            isw.next();
            isw.skipw();
            elm.scr_set(isw.till_str().trim());
        }
        cons.accept(key, elm);
    }

    private static void parseBln(ISW isw, BiConsumer<String, ScriptElm> cons) throws IOException {
        String key = isw.till_space();
        BoolElm elm = new BoolElm(false);
        isw.skipw();
        if(isw.starts('=')){
            isw.next();
            isw.skipw();
            elm.scr_set(Boolean.parseBoolean(isw.till_end().trim()));
        }
        cons.accept(key, elm);
    }

    private static void parseMap(ISW isw, BiConsumer<String, ScriptElm> cons) throws IOException {
        String key = isw.till_space();
        MapElm elm = null;
        isw.skipw();
        if(isw.starts('=')){
            isw.next();
            isw.skipw();
            elm = new MapElm(isw.till_end().trim());
        }
        cons.accept(key, elm == null ? new MapElm() : elm);
    }

    private static void parseList(ISW isw, BiConsumer<String, ScriptElm> cons) throws IOException {
        String key = isw.till_space();
        isw.nextline();
        cons.accept(key, new ListElm());
    }

    private static void parseBlock(ISW isw, Script scr, ScriptBlock root, ScriptBlock block) throws IOException {
        while(isw.has()){
            int line = isw.linenum;
            isw.skipw();
            if(isw.starts(';') && found(isw, ";;")){
                break;
            }
            if(isw.starts('i')){
                isw.next();
                if(isw.starts('f')){
                    isw.next();
                    if(isw.starts('(')){
                        //parse cond
                        isw.nextline();
                    }
                    else{
                        Print.console(">>> Incomplete or invalid 'if' at line '" + line + "'!");
                        isw.nextline();
                    }
                }
                else if(isw.starts('n') && found(isw, "nt")){
                    parseInt(isw, (key, elm) -> block.local.put(key, elm));
                }
                else{
                    Print.console(">>> Unknown code at line '" + isw.linenum + "', expected 'if' or 'int'.");
                    Print.console(">>> Found: " + isw.line + " <-- ");
                    isw.nextline();
                }
                continue;
            }
            if(isw.starts('f') && found(isw, "flt")){
                parseFlt(isw, (key, elm) -> block.local.put(key, elm));
                continue;
            }
            if(isw.starts('s') && found(isw, "str")){
                parseStr(isw, (key, elm) -> block.local.put(key, elm));
                continue;
            }
            if(isw.starts('b') && found(isw, "bln")){
                parseBln(isw, (key, elm) -> block.local.put(key, elm));
                continue;
            }
            if(isw.starts('m') && found(isw, "map")){
                parseMap(isw, (key, elm) -> block.local.put(key, elm));
                continue;
            }
            if(isw.starts('l') && found(isw, "lst")){
                parseList(isw, (key, elm) -> block.local.put(key, elm));
                continue;
            }
            if(isw.starts('e') && found(isw, "else")){
                if(isw.starts('i') && found(isw, "if")){
                    if(isw.starts('(')){
                        //parse cond
                        isw.nextline();
                    }
                    else{
                        Print.console(">>> Incomplete or invalid 'elseif' at line '" + line + "'!");
                        isw.nextline();
                    }
                    continue;
                }
                isw.nextline();
                continue;
            }
            isw.nextline();
        }
    }

    private static boolean found(ISW isw, String str) throws IOException {
        if(isw.skip(str)) return true;
        else{
            Print.console(">>> Unknown code at line '" + isw.linenum + "', expected '" + str + "'.");
            Print.console(">>> Found: " + isw.line + " <-- ");
            isw.nextline();
            return false;
        }
    }

    public static class ISW {

        protected InputStreamReader stream;
        protected char cher;
        protected boolean has = true;
        protected int linenum;
        protected String line = new String();

        public ISW(InputStream stream) {
            this.stream = new InputStreamReader(stream, StandardCharsets.UTF_8);
        }

        public boolean starts(char c) throws IOException {
            return cher == c;
        }

        private boolean next() throws IOException {
            int i = stream.read();
            if(i < 0){
                has = false;
                return true;
            }
            cher = (char)i;
            if(cher == '\r') return next();
            if(cher == '\n'){
                if(LOG) System.out.println(linenum + ": " + line);
                linenum++;
                line = "";
                return true;
            }
            else{
                if(cher >= ' ') line += cher;
                return false;
            }
            //System.out.println(cher);
        }

        private void nextline() throws IOException {
            int line = linenum;
            while(has && line == linenum) next();
        }

        public boolean has(){
            return has;
        }

        /*public void skip() throws IOException {
            if(has && (cher <= ' ' || cher == '\n' || cher == '\r')) {
                next();
                skip();
            }
        }*/

        public void skipw() throws IOException {
            if(has && cher <= ' '){
                next();
                skipw();
            }
        }

        public boolean skip(String sec) throws IOException {
            char[] chars = sec.toCharArray();
            for(int i = 0; i < chars.length; i++){
                if(has && cher == chars[i]){
                    next();
                }
                else return false;
            }
            skipw();
            return true;
        }

        public String till_str() throws IOException {
            StringBuffer buffer = new StringBuffer();
            if(cher == '"') next();
            char last = ' ';
            while(has&& (cher != '"' || last == '\\')){
                buffer.append(last = cher);
                if(next()) break;
            }
            return buffer.toString();
        }

        /*public String till() throws IOException {
            StringBuffer buffer = new StringBuffer();
            while(has && cher != ',' && cher != '}' && cher != ']') {
                buffer.append(cher);
                next();
            }
            return buffer.toString();
        }*/

        public String till_space() throws IOException {
            StringBuffer buffer = new StringBuffer();
            while(has && cher > ' '){
                buffer.append(cher);
                if(next()) break;
            }
            return buffer.toString();
        }

        public String till_end() throws IOException {
            StringBuffer buffer = new StringBuffer();
            while(has){
                buffer.append(cher);
                if(next()) break;
            }
            return buffer.toString();
        }

        public String till(char c) throws IOException {
            StringBuffer buffer = new StringBuffer();
            while(has && cher != c){
                buffer.append(cher);
                next();
            }
            next();
            return buffer.toString();
        }

        public String till(char c, char h) throws IOException {
            StringBuffer buffer = new StringBuffer();
            while(has && cher != c && cher != h){
                buffer.append(cher);
                if(next()) break;
            }
            next();
            return buffer.toString();
        }

    }

}
