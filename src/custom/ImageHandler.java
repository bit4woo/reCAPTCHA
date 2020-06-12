package custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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


public class ImageHandler {
	
	public static final String TYPE_JPG = "jpg";
	public static final String TYPE_GIF = "gif";
	public static final String TYPE_PNG = "png";
	public static final String TYPE_BMP = "bmp";
	public static final String TYPE_UNKNOWN = "unknown";

	public static void main(String[] args) {
		System.out.println("图片格式1： " + getPicType("E:\\wolaidai\\==wininit==\\work\\www.cnhww.com1509529014421.jpg"));
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
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(imgpath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		//读取文件的前几个字节来判断图片格式
		byte[] b = new byte[4];
		try {
			fis.read(b, 0, b.length);
			String type = bytesToHexString(b).toUpperCase();
			if (type.contains("FFD8FF")) {
				return TYPE_JPG;
			} else if (type.contains("89504E47")) {
				return TYPE_PNG;
			} else if (type.contains("47494638")) {
				return TYPE_GIF;
			} else if (type.contains("424D")) {
				return TYPE_BMP;
			}else{
				return TYPE_UNKNOWN;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	public static String download(String imageUrl) {
		return imageUrl;
		
	}

	public static String downloadWithBurpMethod(String imageUrl) {
		return imageUrl;
		
	}

	public getBodyByte(IHttpService httpService,byte[] request) {
		
		IBurpExtenderCallbacks callbacks = BurpExtender.getBurpCallbacks();
		IExtensionHelpers helpers = callbacks.getHelpers();
	    IHttpRequestResponse message =  callbacks.makeHttpRequest(httpService,request);
	    IResponseInfo response =  helpers.analyzeResponse(message.getResponse());
	    
	    List<String> headers = response.getHeaders();

	    for(String header:headers) {
	    	if(header.toLowerCase().startsWith("content-type:")) {
	    		fileType= header.substring(header.indexOf("/")+1, header.indexOf(";"));
	    	}
	    }
	    
	    int bodyOffset = response.getBodyOffset();
	    int length = message.getResponse().length;
	    byte[] byte_body = Arrays.copyOfRange(message.getResponse(), bodyOffset, length-1);
	    
	    return byte_body;
	}
	
	public static String download(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers, IHttpService httpService,byte[] request) {
		imageDownloader x = new imageDownloader(callbacks,helpers,httpService,request);
		return x.writeImageToDisk(x.byte_image);
	}
	
	public String writeImageToDisk(byte[] img){
        try {
        	String imgName = ""+System.currentTimeMillis();
            File file = new File(imgName);
            FileOutputStream fops = new FileOutputStream(file);  
            fops.write(img);  
            //fops.flush();  
            fops.close();
            if(fileType == null) {
            	fileType = imageType.getPicType(imgName);
            }
		    
		    String newName = null;
		    if(fileType.equals("unknown")) {
		    	newName =imgName +".jpg";
		    }else {
		    	newName = imgName +"."+fileType;
		    }
		    System.out.println(newName);
		    File oldfile = new File(imgName);
		    File newfile = new File(newName);
		    oldfile.renameTo(newfile);
		    //String newFileName = newfile.getName();
		    
            return newName;
        } catch (Exception e) {  
            e.printStackTrace();
        }  
	    return null;
    }  
}
