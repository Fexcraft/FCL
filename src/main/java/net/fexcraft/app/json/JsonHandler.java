package net.fexcraft.app.json;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * 
 * Fex's Json Lib
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class JsonHandler {
	
	private static String NUMBER = "^\\-?\\d+$";
	private static String FLOATN = "^\\-?\\d+\\.\\d+$";
	
	public static JsonObject<?> parse(String str, boolean defmap){
		return parse(new CharList(str), defmap);
	}
	
	public static JsonObject<?> parse(CharList str, boolean defmap){
		JsonObject<?> root = null;
		Parser parser = new Parser();
		str = parser.trim(str);
		if(str.starts('{')){
			root = parser.parseMap(new JsonMap(), str).obj;
		}
		else if(str.starts('[')){
			root = parser.parseArray(new JsonArray(), str).obj;
		}
		else return defmap ? new JsonMap() : new JsonArray();
		return root;
	}

	public static JsonObject<?> parse(File file, boolean defmap){
		try{
			return parse(new CharList(new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8)), defmap);
		}
		catch(IOException e){
			e.printStackTrace();
			return defmap ? new JsonMap() : new JsonArray();
		}
	}

	public static JsonMap parse(File file){
		return parse(file, true).asMap();
	}

	public static JsonObject<?> parse(InputStream stream, boolean defmap) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(stream);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		for(int res = bis.read(); res != -1; res = bis.read()) buf.write((byte)res);
		String str = buf.toString(StandardCharsets.UTF_8.name());
		bis.close(); buf.close();
		return parse(new CharList(str), defmap);
	}

	public static JsonMap parse(InputStream stream) throws IOException {
		return parse(stream, true).asMap();
	}
	
	private static class Parser {
		
		private final Ret RET = new Ret();
		private final CharList COPY = new CharList();
		private CharList key = null, val = null;
		private String tkey = null;
		private char s = ' ';
		private int index = 0;

		private Ret parseMap(JsonMap root, CharList str){
			if(str.starts('{')) str = sub(str, 1);
			while(str.size() > 0){
				str = trim(str);
				s = str.get(0);
				if(s == '"'){
					key = scanTill(str, '"');
					str = sub(str, key.size() + 1);
					key = sub(key, 1);
					str = trim(sub(trim(str), 1));//removing colon
					s = str.get(0);
					if(s == '{'){
						tkey = key.asString();
						parseMap(new JsonMap(), str);
						root.add(tkey, RET.obj);
						str = RET.str;
					}
					else if(s == '['){
						parseArray(new JsonArray(), str);
						root.add(key.asString(), RET.obj);
						str = RET.str;
					}
					else if(s == '"'){
						val = scanTill(str, '"');
						str = sub(str, val.size() + 1);
						root.add(key.asString(), parseValue(sub(val, 1).asString()));
					}
					else{
						val = scanTill(str, ',');
						str = sub(str, val.size());
						root.add(key.asString(), parseValue(val.asString()));
					}
				}
				else if(s == ',') str = sub(str, 1);
				else if(s == '}') break;
				else str = sub(str, 1);
			}
			if(str.starts('}')) str = sub(str, 1);
			return RET.set(root, str);
		}

		private Ret parseArray(JsonArray root, CharList str){
			if(str.starts('[')) str = sub(str, 1);
			while(str.size() > 0){
				str = trim(str);
				s = str.get(0);
				if(s == '"'){
					val = scanTill(str, '"');
					str = sub(str, val.size() + 1);
					root.add(parseValue(sub(val, 1).asString()));
				}
				else if(s == '{'){
					parseMap(new JsonMap(), str);
					root.add(RET.obj);
					str = RET.str;
				}
				else if(s == '['){
					parseArray(new JsonArray(), str);
					root.add(RET.obj);
					str = RET.str;
				}
				else if(s == ',') str = sub(str, 1);
				else if(s == ']') break;
				else {
					val = scanTill(str, ',');
					str = sub(str, val.size());
					root.add(parseValue(val.asString()));
				}
			}
			if(str.starts(']')) str = sub(str, 1);
			return RET.set(root, str);
		}

		private static JsonObject<?> parseValue(String val){
			val = val.trim();
			if(val.equals("null")){
				return new JsonObject<String>(val);//new JsonObject<Object>(null);
			}
			else if(Pattern.matches(NUMBER, val)){
				long leng = Long.parseLong(val);
				if(leng < Integer.MAX_VALUE){
					return new JsonObject<>((int)leng);
				}
				else return new JsonObject<>(leng);
			}
			else if(Pattern.matches(FLOATN, val)){
				return new JsonObject<>(Float.parseFloat(val));
			}
			else if(val.equals("true")) return new JsonObject<>(true);
			else if(val.equals("false")) return new JsonObject<>(false);
			else return new JsonObject<>(val);
		}

		private CharList scanTill(CharList str, char c){
			index = 1;
			while(index < str.size() && end(str.get(index), c) /*&& str.get(index - 1) != '\\'*/) index++;
			return sub(str, 0, index);
		}

		private static boolean end(char e, char c){
			return e != c && e != ']' && e != '}';
		}
		
		private CharList sub(CharList str, int b){
			while(b > 0){
				str.remove(0);
				b--;
			}
			return str;
		}
		
		private CharList sub(CharList str, int b, int e){
			CharList list = new CharList();
			for(int i = b; i < e; i++) list.add(str.get(i));
			return list;
		}
		
		private CharList trim(CharList str){
			int s = 0, z = str.size();
			while(s < z && str.get(s) <= ' ') s++;
			while(s < z && str.get(z - 1) <= ' ') z--;
			if(s > 0 || z < str.size()){
				COPY.addAll(str);
				str.clear();
				for(int i = s; i < z; i++) str.add(COPY.get(i));
				COPY.clear();
				return str;
			}
			else return str;
		}
		
	}
	
	public static class CharList extends ArrayList<Character> {

		public CharList(){}

		public CharList(String str){
			for(char c : str.toCharArray()) add(c);
		}

		public String asString(){
			StringBuffer buffer = new StringBuffer();
			for(char c : this) buffer.append(c);
			return buffer.toString();
		}

		public boolean starts(char c){
			return get(0) == c;
		}
		
	}

	public static String toString(JsonObject<?> obj){
		return toString(obj, 0, false, PrintOption.DEFAULT);
	}

	public static String toString(JsonObject<?> obj, PrintOption opt){
		return toString(obj, 0, false, opt);
	}

	public static String toString(JsonObject<?> obj, int depth, boolean append, PrintOption opt){
		String ret = "", tab = "", tabo = "    ", space = opt.spaced ? " " : "", colspace = !opt.flat || opt.spaced ? " " : "";
		String app = append ? "," + space : "", n = opt.flat ? "" : "\n";
		if(!opt.flat){
			for(int j = 0; j < depth; j++){
				tab += tabo;
			}
		}
		else tabo = "";
		if(obj == null){
			ret += "[ \"null\" ]";
		}
		else if(obj.isMap()){
			if(obj.asMap().empty()){
				ret += "{}" + app + n;
			}
			else{
				ret += "{" + space + n;
				Iterator<Entry<String, JsonObject<?>>> it = obj.asMap().value.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String, JsonObject<?>> entry = it.next();
					ret += tab + tabo + '"' + entry.getKey() + '"' + ":" + colspace + toString(entry.getValue(), depth + 1, it.hasNext(), opt);
				}
				ret += tab + space + "}" + app + n;
			}
		}
		else if(obj.isArray()){
			if(obj.asArray().empty()){
				ret += "[]" + app + n;
			}
			else{
				ret += "[" + space + n;
				Iterator<JsonObject<?>> it = obj.asArray().value.iterator();
				while(it.hasNext()){
					ret += tab + tabo + toString(it.next(), depth + 1, it.hasNext(), opt);
				}
				ret += tab + space + "]" + app + n;
			}
		}
		else{
			ret += (obj.value instanceof String ? '"' + obj.value.toString() + '"' : obj.value) + app + n;
		}
		return ret;
	}
	
	public static class PrintOption {
		
		public static final PrintOption FLAT = new PrintOption().flat(true).spaced(false);
		public static final PrintOption SPACED = new PrintOption().flat(false).spaced(true);
		public static final PrintOption DEFAULT = SPACED;
		
		boolean flat, spaced;
		
		public PrintOption(){}
		
		public PrintOption flat(boolean bool){
			flat = bool;
			return this;
		}
		
		public PrintOption spaced(boolean bool){
			spaced = bool;
			return this;
		}
		
	}
	
	private static class Ret {
		
		private JsonObject<?> obj;
		private CharList str;
		
		public Ret set(JsonObject<?> obj, CharList str){
			this.obj = obj;
			this.str = str;
			return this;
		}
		
	}

	public static void print(File file, JsonObject<?> obj, PrintOption opt){
		try{
			Files.write(file.toPath(), toString(obj, opt).getBytes(StandardCharsets.UTF_8));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public static JsonMap parseURL(String... adr){
		try{
			URL url = new URL(adr[0]);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(adr.length > 1 ? "POST" : "GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			connection.setConnectTimeout(10000);
			connection.setDoOutput(adr.length > 1);
			if(adr.length > 1){
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(adr[1]);
				wr.flush();
				wr.close();
			}
			//
			JsonMap obj = parse(connection.getInputStream());
			connection.disconnect();
			return obj;
		}
		catch(IOException e){
			e.printStackTrace();
			return new JsonMap();
		}
	}

	public static JsonMap wrap(Map<String, Object> map, JsonMap json){
		if(json == null) json = new JsonMap();
		for(Entry<String, Object> entry : map.entrySet()){
			if(entry.getValue() instanceof Collection){
				json.add(entry.getKey(), wrap((Collection<?>)entry.getValue(), null));
			}
			else if(entry.getValue() instanceof Map){
				json.add(entry.getKey(), wrap((Map<String, Object>)entry.getValue(), null));
			}
			else if(entry.getValue() instanceof String){
				json.add(entry.getKey(), entry.getValue() + "");
			}
			else json.add(entry.getKey(), Parser.parseValue(entry.getValue() + ""));
		}
		return json;
	}

	public static JsonArray wrap(Collection<?> collection, JsonArray json){
		if(json == null) json = new JsonArray();
		for(Object obj : collection){
			if(obj instanceof Collection){
				json.add(wrap((Collection<?>)obj, null));
			}
			else if(obj instanceof Map){
				json.add(wrap((Map<String, Object>)obj, null));
			}
			else if(obj instanceof String){
				json.add(obj + "");
			}
			else json.add(Parser.parseValue(obj + ""));
		}
		return json;
	}

	public static Object dewrap(String obj){
		return dewrap(parse(new CharList(obj), true).asMap());
	}

	public static HashMap<String, Object> dewrap(JsonMap map){
		HashMap<String, Object> hashmap = new HashMap<>();
		for(Entry<String, JsonObject<?>> entry : map.entries()){
			if(entry.getValue().isMap()){
				hashmap.put(entry.getKey(), dewrap(entry.getValue().asMap()));
			}
			else if(entry.getValue().isArray()){
				hashmap.put(entry.getKey(), dewrap(entry.getValue().asArray()));
			}
			else{
				hashmap.put(entry.getKey(), entry.getValue().value);
			}
		}
		return hashmap;
	}

	public static ArrayList<Object> dewrap(JsonArray array){
		ArrayList<Object> list = new ArrayList<>();
		for(JsonObject<?> obj : array.value){
			if(obj.isMap()){
				list.add(dewrap(obj.asMap()));
			}
			else if(obj.isArray()){
				list.add(dewrap(obj.asArray()));
			}
			else{
				list.add(obj.value);
			}
		}
		return list;
	}

	public static <T> ArrayList<T> dewrapc(JsonArray array){
		ArrayList<Object> list = new ArrayList<>();
		for(JsonObject<?> obj : array.value){
			if(obj.isMap()){
				list.add(dewrap(obj.asMap()));
			}
			else if(obj.isArray()){
				list.add(dewrap(obj.asArray()));
			}
			else{
				list.add(obj.value);
			}
		}
		return (ArrayList<T>)list;
	}

}
