package custom;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.io.IOUtils;

import burp.IHttpRequestResponse;
import burp.IHttpService;

public class RequestHelper {
	
	public String httpservice = null;
	public String raws = null;
	
	public String host = null;
	public String method = null; //GET POST
	public String strurl =null;
	public HashMap<String,String> headers = new HashMap<String,String>();
	
	
	public static void main(String[] args) {
		try {
			String httpservice = "https://ecss.pingan.com";
			String raws ="GET /createImageCode?t=1509343555818 HTTP/1.1\r\n" + 
					"Host: ecss.pingan.com\r\n" + 
					"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0\r\n" + 
					"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" + 
					"Accept-Language: en-US,en;q=0.5\r\n" + 
					"Referer: https://ecss.pingan.com/login\r\n" + 
					"Cookie: WLS_HTTP_BRIDGE_ECPUB=IJpr6OOnqckcftgpo95Dviotd67OMbZ51XlrfF9GFMM8uVOLI8V4!-1635147988; BIGipServerGroup-GMSS_PrdPool=2586057900.847.0000; SSOPWSLOGIN=0\r\n" + 
					"Connection: close\r\n" + 
					"Upgrade-Insecure-Requests: 1\r\n" + 
					"Pragma: no-cache\r\n" + 
					"Cache-Control: no-cache\r\n" + 
					"\r\n" + 
					"";
			RequestHelper x = new RequestHelper();
			x.httpservice = httpservice;
			x.raws =raws;
			x.parser();
			byte[] bytes =x.dorequest();
			System.out.println(x.writeImageToDisk(bytes));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] readStream(InputStream inStream) throws Exception { //这个方法有点问题，图片只有一半
	    int count = 0; 
	    while (count == 0) {  
	        count = inStream.available();  
	    }
	    byte[] b = new byte[count];  
	    inStream.read(b); 
	    return b;  
	} 
	
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1 ){  
            outStream.write(buffer, 0, len);  
        }  
        inStream.close();  
        return outStream.toByteArray();  
    }  
    
    public String writeImageToDisk(byte[] img){
        try {
        	String imgName = this.host+System.currentTimeMillis()+".jpg";
            File file = new File(imgName);
            FileOutputStream fops = new FileOutputStream(file);  
            fops.write(img);  
            //fops.flush();  
            fops.close();
            return imgName;
        } catch (Exception e) {  
            e.printStackTrace();
        }  
	    return null;
    }  

    
    public void parser() {
    	
    	this.host = this.httpservice.split("://")[1].split(":")[0];
    	
    	String[] rawsArray = this.raws.split("\r\n");
    	for(int i=0;i< rawsArray.length;i++) {
    		if(i==0) {
    			String url = rawsArray[0].split(" ")[1];
    			System.out.println(url);
    			this.strurl = this.httpservice + url;
    			this.method = rawsArray[i].split(" ")[0];
    		}
    		else {
    			String key = rawsArray[i].split(": ")[0];
    			String value = rawsArray[i].split(": ")[1];
    			this.headers.put(key, value);
    		}
    	}
    }
    
   public byte[] dorequest() throws Exception {
	   
	   
		try {  
            URL url = new URL(this.strurl);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            	conn.addRequestProperty(entry.getKey(),entry.getValue());
            	//conn.addRequestProperty("User-Agent","Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:54.0) Gecko/20100101 Firefox/54.0");
            }
            conn.setRequestMethod(this.method);  
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(8*1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
            return btImg;  
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }  
        return null;
   }
}