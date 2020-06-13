package deprecated;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

@Deprecated
public class myjsdatiOld {
	public static void main(String[] args) {
		String url = "https://v2-api.jsdama.com/upload";
		String imgPath = "D:/github/reCAPTCHA/src/deprecated/testImage.jpeg";
		String code;
		try {
			code = PostImage(url,imgPath,"bit4woo","xxxxx",1001,"http://127.0.0.1:8080");
			System.out.print(code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getCode(String imgPath,String paraString,String proxyUrl) {
		String url = "https://v2-api.jsdama.com/upload";
		HashMap<String, String> paras = getConfig(paraString);
		String username = paras.get("username");
		String password = paras.get("password");
		int captchaType = Integer.parseInt(paras.get("captchaType"));
		try {
			String code = PostImage(url,imgPath,username,password,captchaType,proxyUrl);
			return code;
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	public static String PostImage(String url,String imgPath,String username,String password,int typeid,String proxyUrl) throws IOException {

		URL u = new URL(url);
		
		HttpsURLConnection con;
		if (proxyUrl == null) {
			con = (HttpsURLConnection) u.openConnection();
		}else {
			URL Purl = new URL(proxyUrl);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Purl.getHost(), Purl.getPort()));
			con = (HttpsURLConnection) u.openConnection(proxy);
		}
		
		con.setRequestMethod("POST");
		con.setReadTimeout(10*1000);
		con.setConnectTimeout(10*1000); //5min
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setUseCaches(true);
		con.setRequestProperty("Content-Type","text/json");
		con.setRequestProperty("Connection", "keep-alive");
		OutputStream out = con.getOutputStream();

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("softwareId", 10706);
		jsonParam.put("softwareSecret", "umdNcjK6PWp17uT6BWUY96PmFVtSPS5Pd4Nhir45");
		jsonParam.put("username", username);
		jsonParam.put("password", password);
		jsonParam.put("captchaData", readImage(imgPath));
		jsonParam.put("captchaType", typeid);


		out.write("\r\n\r\n".getBytes());
		out.write(jsonParam.toString().getBytes());
		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(con
				.getInputStream(), "UTF-8"));
		String temp;
		while ((temp = br.readLine()) != null) {
			buffer.append(temp);
			buffer.append("\n");
		}
		String jsonResponse = buffer.toString();
		return grepResult(jsonResponse);
	}

	public static String grepResult(String jsonstr)
	{	 JSONObject obj = new JSONObject(jsonstr);
	int code = obj.getInt("code");
	if (code==0) {
		return obj.getJSONObject("data").getString("recognition");
	}else {
		return obj.getString("message");
	}

	}

	public static String readImage(String imgPath) {
		try {
			File f = new File(imgPath);
			if (null != f) {
				int size = (int) f.length();
				byte[] data = new byte[size];
				FileInputStream fis = new FileInputStream(f);
				fis.read(data, 0, size);
				if(null != fis) fis.close();

				if (data.length > 0)
					return Base64.encodeBase64String(data);
			}
			return null;
		} catch(Exception e) {
			return null;
		}
	}

	public static HashMap<String,String> getConfig(String paraString) {
		HashMap<String,String> paraMap = new HashMap<String,String>();
		String[] tmp = paraString.trim().split("&");
		for(int i=0;i < tmp.length;i++) {
			String key = tmp[i].split("=")[0];
			String value = tmp[i].split("=")[1];
			paraMap.put(key, value);
		}
		return paraMap;
	}
}
