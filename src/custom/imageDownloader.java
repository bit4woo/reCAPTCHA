package custom;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IResponseInfo;

public class imageDownloader {
	byte[] byte_image =null;
    String fileType = null;
	public imageDownloader(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers, IHttpService httpService,byte[] request) {
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
	    byte_image = byte_body;

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
