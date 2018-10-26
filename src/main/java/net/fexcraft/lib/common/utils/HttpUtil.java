package net.fexcraft.lib.common.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fexcraft.lib.common.json.JsonUtil;

/**
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class HttpUtil {
	
	/** Requests a JsonObject from the given adress and parameters, using the POST HTML method. */
	public static JsonObject request(String adress, String parameters){
		try{
			URL url = new URL(adress);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("User-Agent", "Mozilla/5.0");
				connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				connection.setConnectTimeout(5000);
				connection.setDoOutput(true);
				
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(parameters);
				wr.flush();
				wr.close();
			
			JsonObject obj = JsonUtil.getObjectFromInputStream(connection.getInputStream()).getAsJsonObject();	
			
			connection.disconnect();
			return obj;
		}
		catch(SocketTimeoutException e){
			e.printStackTrace();
			return null;
		}
		catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static JsonElement request(String adress){
		try{
			URL url = new URL(adress);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("User-Agent", "Mozilla/5.0");
				connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				connection.setConnectTimeout(5000);
			
			JsonElement obj = JsonUtil.getElementFromInputStream(connection.getInputStream());	
			
			connection.disconnect();
			return obj;
		}
		catch(SocketTimeoutException e){
			e.printStackTrace();
			return null;
		}
		catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
}