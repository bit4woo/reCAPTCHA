package custom;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;


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
			//String httpservice = "https://ecss.pingan.com";
			String raws1 ="GET /createImageCode?t=1509343555818 HTTP/1.1\r\n" + 
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
			String httpservice = "http://www.cnhww.com";
			String raws = "GET /demo5/GetCode.asp HTTP/1.1\r\n" + 
					"Host: www.cnhww.com\r\n" + 
					"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0\r\n" + 
					"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" + 
					"Accept-Language: en-US,en;q=0.5\r\n" + 
					"Connection: close\r\n" + 
					"Upgrade-Insecure-Requests: 1\r\n" + 
					"";
			//String httpservice = "https://oms.meizu.com:8443";
			String raws2 = "GET /cas/captcha.htm HTTP/1.1\r\n" + 
					"Host: oms.meizu.com:8443\r\n" + 
					"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0\r\n" + 
					"Accept: */*\r\n" + 
					"Accept-Language: en-US,en;q=0.5\r\n" + 
					"Referer: https://oms.meizu.com:8443/cas/login?service=http%3A%2F%2Foms.meizu.com%2Flogin.action\r\n" + 
					"Cookie: JSESSIONID=9CA93BDD402AD7AA41962C577874B105; MZ_STORE_UUID=7508ed10-fa01-473f-9c1e-20fb05abe416; tj_coid=6391ea46eb6c0d45226b205940b0f353; CSRF_ID=3f7c2d42-8fe1-47e8-95b5-d2a129d9727d; MEIZUSTORECARTCOUNT=%7B%22c%22%3A0%2C%22t%22%3A1509602002356%2C%22s%22%3Afalse%7D\r\n" + 
					"Connection: close\r\n" + 
					"Cache-Control: max-age=0\r\n" + 
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
	
	public byte[] readStream(InputStream inStream) throws Exception { //��������е����⣬ͼƬֻ��һ��
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
        	String imgName = this.host+System.currentTimeMillis();
            File file = new File(imgName);
            FileOutputStream fops = new FileOutputStream(file);  
            fops.write(img);  
            //fops.flush();  
            fops.close();
            
		    String type = imageType.getPicType(imgName);
		    String newName = null;
		    if(type.equals("unknown")) {
		    	newName =imgName +".jpg";
		    }else {
		    	newName = imgName +"."+type;
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
    
    private static TrustManager myX509TrustManager = new X509TrustManager() { 

        @Override 
        public X509Certificate[] getAcceptedIssuers() { 
            return null; 
        } 

        @Override 
        public void checkServerTrusted(X509Certificate[] chain, String authType) 
        throws CertificateException { 
        } 

        @Override 
        public void checkClientTrusted(X509Certificate[] chain, String authType) 
        throws CertificateException { 
        } 
    };
    
   public byte[] dorequest() throws Exception {
	   
	   
		try {  
            if(this.strurl.startsWith("https:")) {
            	URL url = new URL(this.strurl);
                // 创建SSLContext对象，并使用我们指定的信任管理器初始化    
                TrustManager[] tm = new TrustManager[]{myX509TrustManager};    
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");    
                sslContext.init(null, tm, new java.security.SecureRandom());    
                // 从上述SSLContext对象中得到SSLSocketFactory对象    
                SSLSocketFactory ssf = sslContext.getSocketFactory();      
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();    
                conn.setSSLSocketFactory(ssf);  
                for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                	conn.addRequestProperty(entry.getKey(),entry.getValue());
                	//conn.addRequestProperty("User-Agent","Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:54.0) Gecko/20100101 Firefox/54.0");
                }
                conn.setRequestMethod(this.method);  
                conn.setConnectTimeout(5 * 1000);
                conn.setReadTimeout(8*1000);
                InputStream inStream = conn.getInputStream();
                byte[] btImg = readInputStream(inStream);
                return btImg;   
            }
            else {
            	URL url = new URL(this.strurl);
            	HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                	conn.addRequestProperty(entry.getKey(),entry.getValue());
                	//conn.addRequestProperty("User-Agent","Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:54.0) Gecko/20100101 Firefox/54.0");
                }
                conn.setRequestMethod(this.method);  
                conn.setConnectTimeout(5 * 1000);
                conn.setReadTimeout(8*1000);
                InputStream inStream = conn.getInputStream();
                byte[] btImg = readInputStream(inStream);
                return btImg;  
            }
            
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return (e.toString()).getBytes();
        }  
        
   }
}