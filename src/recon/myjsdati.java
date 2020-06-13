package recon;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import httpbase.DoRequest;
import httpbase.Response;

public class myjsdati {
	
	//https://l-files.cn-gd.ufileos.com/api/%E8%81%94%E4%BC%97%E8%AF%86%E5%88%ABV2-API%E6%8E%A5%E5%8F%A3%E5%8D%8F%E8%AE%AE.pdf
	//https://www.jsdati.com/docs/api
	public static String header ="POST /upload HTTP/1.1\r\n" + 
			"Host: v2-api.jsdama.com\r\n" + 
			"Connection: keep-alive\r\n" +
			"Accept: application/json, text/javascript, */*; q=0.01\r\n" + 
			"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0\r\n" + 
			"Content-Type: text/json";
	
	public static void main(String[] args) {
		String imgPath = "D:/github/reCAPTCHA/src/deprecated/testImage.jpeg";
		String code;
		try {
			code = getCode(imgPath,"username=bit4woo&password=password&captchaType=4","http://127.0.0.1:8080");
			System.out.print(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getCode(String imgPath,String paraString,String proxyUrl) throws Exception {
		String request = header+"\r\n\r\n"+genBody(imgPath,paraString);
		Response resp = DoRequest.makeRequest("https://v2-api.jsdama.com/upload", request.getBytes(), proxyUrl);
		String code = grepResult(new String(resp.getBody()));
		return code;
	}
	
	public static String genBody(String imgPath,String paraString) {
		
		//username=bit4woo&password=password&captchaType=1001
		HashMap<String, String> paras = getConfig(paraString);
		String username = paras.get("username");
		String password = paras.get("password");
		int captchaType = Integer.parseInt(paras.get("captchaType"));
		
		//{"softwareId":100008,"softwareSecret":"HQVvQhd81CHcnhVp7aQ6Lc777vBNC6D16ASdcXCz","username":"admin","password":"123456","captchaData":"base64结果","captchaType":1,"captchaMinLength":0,"captchaMaxLength":0}
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("softwareId", 10706);
		jsonParam.put("softwareSecret", "umdNcjK6PWp17uT6BWUY96PmFVtSPS5Pd4Nhir45");
		jsonParam.put("username", username);
		jsonParam.put("password", password);
		jsonParam.put("captchaData", readImage(imgPath));
		jsonParam.put("captchaType", captchaType);
		
		return jsonParam.toString();
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
