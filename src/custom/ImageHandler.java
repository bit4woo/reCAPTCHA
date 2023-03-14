package custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import burp.BurpExtender;
import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IResponseInfo;
import httpbase.DoRequest;
import httpbase.Response;

public class ImageHandler {
	
	public static final String TYPE_JPG = "jpg";
	public static final String TYPE_GIF = "gif";
	public static final String TYPE_PNG = "png";
	public static final String TYPE_BMP = "bmp";
	public static final String TYPE_UNKNOWN = "unknown";

	public static void main(String[] args) {
		String httpservice = "https://oms.meizu.com:8443";
		String raws2 = "GET /cas/captcha.htm HTTP/1.1\r\n" + 
				"Host: oms.meizu.com:8443\r\n" + 
				"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0\r\n" + 
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" + 
				"Accept-Language: en-US,en;q=0.5\r\n" + 
				"Accept-Encoding: gzip, deflate\r\n" + 
				"Connection: close\r\n" + 
				"Upgrade-Insecure-Requests: 1\r\n" + 
				"\r\n" + 
				"";
		String proxy = "http://127.0.0.1:8080";
		try {
			Response resp = DoRequest.makeRequest(httpservice.toString(), raws2.getBytes(),proxy);
			
			byte[] byte_body = resp.getBody();
			List<String> headers = resp.getHeaders();
			
		    String fileType = getPicTypeByHeader(headers);
	    	if (fileType == null) {
	            fileType = getPicType(byte_body);
	    	}
	    	String imgName = System.currentTimeMillis()+".self"+"."+fileType;
		    
		    writeImageToDisk(byte_body,imgName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * byte数组转换成16进制字符串
	 * @param src
	 * @return
	 */
	private static String bytesToHexString(byte[] src){    
		StringBuilder stringBuilder = new StringBuilder();    
		if (src == null || src.length <= 0) {    
			return null;    
		}    
		for (int i = 0; i < src.length; i++) {    
			int v = src[i] & 0xFF;    
			String hv = Integer.toHexString(v);    
			if (hv.length() < 2) {    
				stringBuilder.append(0);    
			}    
			stringBuilder.append(hv);    
		}    
		return stringBuilder.toString();    
	}


	/**
	 * 根据文件流判断图片类型
	 * @param fis
	 * @return jpg/png/gif/bmp
	 */
	public static String getPicType(String imgpath) {
		byte[] b = new byte[4];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(imgpath));
			fis.read(b, 0, b.length);
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return getPicType(b);
	}
	
	public static String getPicType(byte[] inputByte) {
		try {
			//读取文件的前几个字节来判断图片格式
			byte[] head = Arrays.copyOfRange(inputByte, 0, 4);
			String type = bytesToHexString(head).toUpperCase();
			if (type.contains("FFD8FF")) {
				return TYPE_JPG;
			} else if (type.contains("89504E47")) {
				return TYPE_PNG;
			} else if (type.contains("47494638")) {
				return TYPE_GIF;
			} else if (type.contains("424D")) {
				return TYPE_BMP;
			}else{
				return TYPE_JPG;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getPicTypeByHeader(List<String> headers) {
		String fileType = null;
		for(String header:headers) {
			if(header.toLowerCase().startsWith("content-type")) {
				try {
					fileType= header.substring(header.indexOf("/")+1, header.indexOf(";"));
				}catch(Exception e) {
					fileType= header.substring(header.indexOf("/")+1, header.length());
				}
			}
		}
		return fileType;
	}
	
	public static String download(IHttpService httpService,byte[] request,String proxy) throws Exception {
		
		Response resp = DoRequest.makeRequest(httpService.toString(), request,proxy);
		
		byte[] byte_body = resp.getBody();
		List<String> headers = resp.getHeaders();
		
	    String fileType = getPicTypeByHeader(headers);
    	if (fileType == null) {
            fileType = getPicType(byte_body);
    	}
    	String imgName = System.currentTimeMillis()+httpService.getHost()+".self-api"+"."+fileType;
	    
	    return writeImageToDisk(byte_body,imgName);
	}

	public static String downloadWithBurpMethod(IHttpService httpService,byte[] request) {

		IBurpExtenderCallbacks callbacks = BurpExtender.getBurpCallbacks();
		IExtensionHelpers helpers = callbacks.getHelpers();
	    IHttpRequestResponse message =  callbacks.makeHttpRequest(httpService,request);
	    IResponseInfo response =  helpers.analyzeResponse(message.getResponse());
	    
	    List<String> headers = response.getHeaders();
	    
	    int bodyOffset = response.getBodyOffset();
	    int length = message.getResponse().length;
	    byte[] byte_body = Arrays.copyOfRange(message.getResponse(), bodyOffset, length-1);
		
	    String fileType = getPicTypeByHeader(headers);
    	if (fileType == null) {
            fileType = getPicType(byte_body);
    	}
    	String imgName = System.currentTimeMillis()+httpService.getHost()+".burp-api"+"."+fileType;
	    
	    return writeImageToDisk(byte_body,imgName);
	    
	}
	
	public static String writeImageToDisk(byte[] img,String imgName){
        try {
            File file = new File(imgName);
            FileOutputStream fops = new FileOutputStream(file);  
            fops.write(img);  
            //fops.flush();  
            fops.close();
		    
            return file.getAbsolutePath();
        } catch (Exception e) {  
            e.printStackTrace();
            return null;
        }  
    }  
}
