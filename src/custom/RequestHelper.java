package custom;
import java.awt.Image;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
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
import javax.swing.ImageIcon;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.imageio.ImageIO;
import javax.net.ssl.*;

import burp.IHttpRequestResponse;
import burp.IHttpService;

public class RequestHelper {
	
	public String httpservice = null;
	public String raws = null;
	
	public String host = null;
	public String method = null; //GET POST
	public String strurl =null;
	public HashMap<String,String> headers = new HashMap<String,String>();
	public String fileType = null;
	
	
	public static void main(String[] args) {
		try {
			//String httpservice = "http://www.faithfulfitnessforlife.com";
			String raws = "GET /fitness_blog/Captcha.aspx HTTP/1.1\r\n" + 
					"Cookie: ASP.NET_SessionId=xisp1kz0ttyhet55dhdjbe55\r\n" + 
					"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" + 
					"Upgrade-Insecure-Requests: 1\r\n" + 
					"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:60.0) Gecko/20100101 Firefox/60.0\r\n" + 
					"Connection: close\r\n" + 
					"Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\r\n" + 
					"Accept-Encoding: gzip, deflate\r\n" + 
					"Host: www.faithfulfitnessforlife.com\r\n" + 
					"\r\n" + 
					"";
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
			String httpservice3 = "https://180.76.176.167";
			String raws3 ="GET /common/imgCode HTTP/1.1\r\n" + 
					"Host: 180.76.176.167\r\n" + 
					"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0\r\n" + 
					"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" + 
					"Accept-Language: en-US,en;q=0.5\r\n" + 
					"Accept-Encoding: gzip, deflate\r\n" + 
					"Cookie: JSESSIONID=8101DC75B8291044D12D1E1BEE1C8C9F; _jfinal_captcha=4b286d068bff44b2a88026199883ea7c\r\n" + 
					"Connection: close\r\n" + 
					"Upgrade-Insecure-Requests: 1\r\n" + 
					"Cache-Control: max-age=0\r\n" + 
					"\r\n" + 
					"";
			System.out.println(download(httpservice3,raws3));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String download(String httpService,String raws) {
		RequestHelper x = new RequestHelper();
		x.httpservice = httpService;
		x.raws =raws;
		x.parser();
		byte[] bytes =null;
		try {
			bytes = x.dorequest();
		} catch (Exception e1) {
			e1.getMessage();
		}
		String path = x.writeImageToDisk(bytes);
		return path;
	}
	
	public byte[] readStream(InputStream inStream) throws Exception { //this method has problem, image only display half
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
            if(fileType == null) {
		    	fileType ="jpg";
            }
        	String imgName =  System.currentTimeMillis()+this.host+"."+fileType;
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
	   
       HostnameVerifier allHostsValid = new HostnameVerifier() {
           public boolean verify(String hostname, SSLSession session) {
               return true;
           }
       };
		try {  
            if(this.strurl.startsWith("https:")) {
            	URL url = new URL(this.strurl);
                
                TrustManager[] tm = new TrustManager[]{myX509TrustManager};
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");    
                sslContext.init(null, tm, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);//do not check certification
                
                //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
                //HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(proxy);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();   //Connection reset error when certification is not match
                
                for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                	conn.addRequestProperty(entry.getKey(),entry.getValue());
                	//conn.addRequestProperty("User-Agent","Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:54.0) Gecko/20100101 Firefox/54.0");
                }
                conn.setRequestMethod(this.method);
                conn.setConnectTimeout(5 * 1000);
                conn.setReadTimeout(10*1000);
                InputStream inStream = conn.getInputStream();
                try {
                	fileType= conn.getContentType().substring(conn.getContentType().indexOf("/")+1, conn.getContentType().indexOf(";"));
                }catch(Exception e) {
                	fileType= conn.getContentType().substring(conn.getContentType().indexOf("/")+1, conn.getContentType().length());
                }
                
                byte[] btImg = readInputStream(inStream);
                return btImg;   
            }
            else {
            	URL url = new URL(this.strurl);
            	//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
            	//HttpURLConnection conn = (HttpURLConnection)url.openConnection(proxy);
            	HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                	conn.addRequestProperty(entry.getKey(),entry.getValue());
                	//conn.addRequestProperty("User-Agent","Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:54.0) Gecko/20100101 Firefox/54.0");
                }
                conn.setRequestMethod(this.method);  
                conn.setConnectTimeout(5 * 1000);
                conn.setReadTimeout(10*1000);
                InputStream inStream = conn.getInputStream();
                try {
                	fileType= conn.getContentType().substring(conn.getContentType().indexOf("/")+1, conn.getContentType().indexOf(";"));
                }catch(Exception e) {
                	fileType= conn.getContentType().substring(conn.getContentType().indexOf("/")+1, conn.getContentType().length());
                }
                byte[] btImg = readInputStream(inStream);
                return btImg;  
            }
            
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return (e.toString()).getBytes();
        }  
        
   }
}