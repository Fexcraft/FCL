package net.fexcraft.lib.common.utils;

import java.io.DataOutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fexcraft.lib.common.json.JsonUtil;

/**
 * @author Ferdinand Calo' (FEX___96)
 * 
 * NOTE: "https" version, usually only used to connect to the data server, as such "ignoring" certificates since java doesn't process this like a browser.
 * Otherwise if there would be a dedicated way to insert the data server's cert into java it will be added here.
 *
 */
public class HttpsUtil {

	private static SSLSocketFactory temp_off, old;
	static {
		try{
			TrustManager[] temp_toggle_off = new TrustManager[]{
				new X509TrustManager(){
					public java.security.cert.X509Certificate[] getAcceptedIssuers(){
						return null;
					}
					public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType){}
					public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType){}
				}
			};
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, temp_toggle_off, new java.security.SecureRandom());
			temp_off = sc.getSocketFactory();
		}
		catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		catch(KeyManagementException e){
			e.printStackTrace();
		}
	}

	/** Requests a JsonObject from the given adress and parameters, using the POST HTML method. */
	public static JsonObject request(String adress, String parameters, String[] cookies, int timeout){
		try{
			@SuppressWarnings("restriction")
			URL url = new URL(null, adress, new sun.net.www.protocol.https.Handler());
			old = HttpsURLConnection.getDefaultSSLSocketFactory();
			HttpsURLConnection.setDefaultSSLSocketFactory(temp_off);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			if(cookies != null && cookies.length > 0){
				String str = "";
				for(int i = 0; i < cookies.length; i++){
					str += cookies[i];
					if(i != cookies.length - 1){
						str += "; ";
					}
				}
				connection.setRequestProperty("Cookie", str);
			}
			connection.setConnectTimeout(timeout);
			connection.setDoOutput(true);
			//
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();
			//
			JsonObject cook = new JsonObject();
			for(int i = 0;; i++){
				String name = connection.getHeaderFieldKey(i), val = connection.getHeaderField(i);
				if(name == null && val == null){
					break;
				}
				if("Set-Cookie".equalsIgnoreCase(name)){
					String[] fields = val.split(";\\s*"), split;
					for(String str : fields){
						if((split = str.split("=")).length >= 2){
							cook.addProperty(split[0], split[1]);
						}
					}
				}
			}
			JsonObject obj = JsonUtil.getObjectFromInputStream(connection.getInputStream());
			if(cook.entrySet().size() > 0) obj.add(obj.has("cookies") ? "%http:cookies%" : "cookies", cook);
			connection.disconnect();
			HttpsURLConnection.setDefaultSSLSocketFactory(old);
			return obj;
		}
		catch(Exception e){
			e.printStackTrace();
			if(old != null) HttpsURLConnection.setDefaultSSLSocketFactory(old);
			return null;
		}
	}

	public static JsonObject request(String adress, String parameters){
		return request(adress, parameters, null, 5000);
	}

	public static JsonObject request(String adress, String parameters, int timeout){
		return request(adress, parameters, null, timeout);
	}

	public static JsonObject request(String adress, String parameters, String[] cookies){
		return request(adress, parameters, cookies, 5000);
	}

	public static JsonElement request(String adress, String[] cookies){
		return request(adress, cookies, 5000);
	}

	public static JsonElement request(String adress, String[] cookies, int timeout){
		try{
			@SuppressWarnings("restriction")
			URL url = new URL(null, adress, new sun.net.www.protocol.https.Handler());
			old = HttpsURLConnection.getDefaultSSLSocketFactory();
			HttpsURLConnection.setDefaultSSLSocketFactory(temp_off);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			if(cookies != null && cookies.length > 0){
				String str = "";
				for(int i = 0; i < cookies.length; i++){
					str += cookies[i];
					if(i != cookies.length - 1){
						str += "; ";
					}
				}
				connection.setRequestProperty("Cookie", str);
			}
			connection.setConnectTimeout(timeout);
			//
			JsonObject cook = new JsonObject();
			for(int i = 0;; i++){
				String name = connection.getHeaderFieldKey(i), val = connection.getHeaderField(i);
				if(name == null && val == null){
					break;
				}
				if("Set-Cookie".equalsIgnoreCase(name)){
					String[] fields = val.split(";\\s*"), split;
					for(String str : fields){
						if((split = str.split("=")).length >= 2){
							cook.addProperty(split[0], split[1]);
						}
					}
				}
			}
			//
			JsonElement obj = JsonUtil.getElementFromInputStream(connection.getInputStream());
			if(obj.isJsonObject() && cook.entrySet().size() > 0) obj.getAsJsonObject().add(obj.getAsJsonObject().has("cookies") ? "%http:cookies%" : "cookies", cook);
			connection.disconnect();
		    HttpsURLConnection.setDefaultSSLSocketFactory(old);
			return obj;
		}
		catch(Exception e){
			e.printStackTrace();
			if(old != null) HttpsURLConnection.setDefaultSSLSocketFactory(old);
			return null;
		}
	}

	public static JsonElement request(String adress){
		return request(adress, (String[])null);
	}

}