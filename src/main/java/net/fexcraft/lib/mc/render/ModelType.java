package net.fexcraft.lib.mc.render;

public enum ModelType {
	
	JSON("json", "json"), JAVA("java", "class"), JTMT("jtmt", "jtmt"), TMT("tmt", "class"), OBJ("obj", "obj"), NONE("null", "");
	
	private String name, extension;
	
	ModelType(String s, String e){
		name = s;
		extension = e;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public static ModelType fromString(String s){
		for(ModelType type : values()){
			if(type.name.equals(s)){
				return type;
			}
		}
		return NONE;
	}
	
	public String getExtension(){
		return extension;
	}
	
}