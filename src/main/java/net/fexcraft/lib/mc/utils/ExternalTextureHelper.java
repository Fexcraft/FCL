package net.fexcraft.lib.mc.utils;

public class ExternalTextureHelper {
	
	/*private static final Map<String, UCResourceLocation> map = new HashMap<String, UCResourceLocation>();
	
	public static UCResourceLocation get(String s){
		if(map.containsKey(s)){ return map.get(s); }
		UCResourceLocation texture = new UCResourceLocation("fcl:remote/", s);
		ITextureObject object = Minecraft.getMinecraft().renderEngine.getTexture(texture);
        if(object == null){ File file = new File(s);
    		ThreadDownloadImageData tdid = new ThreadDownloadImageData(file.exists() ? file : null, s, null, null);
        	Minecraft.getMinecraft().renderEngine.loadTexture(texture, (object = tdid));
        }
        map.put(s, texture);
        return texture;
	}*/
	
}