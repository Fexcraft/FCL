package net.fexcraft.lib.mc.render;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;

public class ExternalTextureHelper {
	
	private static final Map<String, ResourceLocation> map = new HashMap<>();
	
	public static ResourceLocation get(String s){
		if(map.containsKey(s)) return map.get(s);
		ResourceLocation texture = new ResourceLocation("fcl-url", s);
		ITextureObject object = Minecraft.getMinecraft().renderEngine.getTexture(texture);
        if(object == null){
        	SimplerThreadImageDownloader thid = new SimplerThreadImageDownloader(s, texture);
        	Minecraft.getMinecraft().renderEngine.loadTexture(texture, (object = thid));
        }
        map.put(s, texture);
        return texture;
	}

	public static ResourceLocation get(String s, byte[] arr){
		if(map.containsKey(s)){
			if(arr != null) map.remove(s);
			else return map.get(s);
		}
		if(arr == null) return new ResourceLocation(s);
		ResourceLocation tex = new ResourceLocation(s);
		ITextureObject object = Minecraft.getMinecraft().renderEngine.getTexture(tex);
		if(object != null) Minecraft.getMinecraft().renderEngine.deleteTexture(tex);
		if(object == null){
			BufferedTextureLoader thid = new BufferedTextureLoader(tex, arr);
			Minecraft.getMinecraft().renderEngine.loadTexture(tex, thid);
		}
		map.put(s, tex);
		return tex;
	}
	
	@SideOnly(Side.CLIENT)
	public static class SimplerThreadImageDownloader extends SimpleTexture {
		
	    private static final AtomicInteger THRID = new AtomicInteger(0);
	    private final String url;
	    private BufferedImage image;
	    private Thread thread;
	    private boolean uploaded;

	    public SimplerThreadImageDownloader(String imgurl, ResourceLocation resloc) {
	        super(resloc);
	        url = imgurl;
	    }

	    private void checkIfUploaded(){
	    	if(uploaded || image == null) return;
            if(textureLocation != null) deleteGlTexture();
            TextureUtil.uploadTextureImage(super.getGlTextureId(), image);
            uploaded = true;
	    }

	    @Override
	    public int getGlTextureId(){
	        checkIfUploaded();
	        return super.getGlTextureId();
	    }

	    @Override
	    public void loadTexture(IResourceManager resman) throws IOException {
	        if(thread != null) return;
			thread = new Thread(() -> {
				HttpURLConnection conn = null;
				Print.log("Attempting download of texture '" + url + "'.");
				try{
					conn = (HttpURLConnection)new URL(url).openConnection(Minecraft.getMinecraft().getProxy());
					conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
					conn.setDoInput(true);
					conn.setDoOutput(false);
					conn.connect();
					if(conn.getResponseCode() == 200){
						image = TextureUtil.readBufferedImage(conn.getInputStream());
						return;
					}
					else{
						Print.log("Received response code '" + conn.getResponseCode() + "'.");
					}
				}
				catch(Exception e) {
					Print.log("Errors during download of texture.");
					e.printStackTrace();
					return;
				}
				finally{
					if(conn != null) conn.disconnect();
				}
			});
			thread.setName("FCL TexDL #" + THRID.incrementAndGet());
			thread.setDaemon(true);
			thread.start();
	    }
	    
	}

	@SideOnly(Side.CLIENT)
	public static class BufferedTextureLoader extends SimpleTexture {

		private BufferedImage image;
		private byte[] array;
		private boolean uploaded;

		public BufferedTextureLoader(ResourceLocation resloc, byte[] arr) {
			super(resloc);
			array = arr;
		}

		private void checkIfUploaded(){
			if(uploaded || image == null) return;
			if(textureLocation != null) deleteGlTexture();
			TextureUtil.uploadTextureImage(super.getGlTextureId(), image);
			uploaded = true;
		}

		@Override
		public int getGlTextureId(){
			checkIfUploaded();
			return super.getGlTextureId();
		}

		@Override
		public void loadTexture(IResourceManager resman) throws IOException {
			image = ImageIO.read(new ByteArrayInputStream(array));
		}

	}
	
}