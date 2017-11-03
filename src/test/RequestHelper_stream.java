package test;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.io.IOUtils;

import burp.IHttpRequestResponse;
import burp.IHttpService;

public class RequestHelper_stream {
	
	public IHttpRequestResponse MessageInfo;
	
	public static void main(String[] args) {
		try {
			String httpservice = "https://xxx.xxx.com";
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
			//dorequest(httpservice,raws);
			String path = getImage(httpservice,raws);
			System.out.println(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static byte[] readStream(InputStream inStream) throws Exception {  
	    int count = 0; 
	    while (count == 0) {  
	        count = inStream.available();  
	    }
	    byte[] b = new byte[count];  
	    inStream.read(b); 
	    return b;  
	} 
	
   public static String getImage(String httpservice,String httpraws) throws Exception {
	   Socket socket = null;
	   byte[] raws = httpraws.getBytes();
		
	   String host = httpservice.split("://")[1].split(":")[0];
		String protocol = httpservice.split("://")[0];
		int port = 0;
		if (httpservice.split("://")[1].split(":").length ==2) {
			port = Integer.parseInt(httpservice.split("://")[1].split(":")[1]);
		}else if (protocol.equals("https")) {
			port =443;
		}else if (protocol.equals("http")) {
			port = 80;
		}
		System.out.println(host);
		System.out.println(protocol);
		System.out.println(port);
		
	   if(protocol.equals("https")) {
		   socket = SSLSocketFactory.getDefault().createSocket(host, port);
	   }
	   else if(protocol.equals("http")) {
		   socket = new Socket(host, port); 
	   }
	   try {
		   /*
		   Writer out = new OutputStreamWriter(
			          socket.getOutputStream(), "ISO-8859-1");
		   out.write(raws);
		   out.flush(); 
		   */
		   OutputStream test = socket.getOutputStream(); //发送数据包，以二进制格式
		   test.write(raws);
		   test.flush();
		   
		   InputStream In = socket.getInputStream(); //获取返回包，以二进制格式
		   byte[] bytes = IOUtils.toByteArray(In);
		   
		   
		    String imgName = host+System.currentTimeMillis()+".jpg";
		    //stdout.println(imgName);
	    	File imageFile = new File(imgName);
	        //创建输出流  
	        FileOutputStream outStream = new FileOutputStream(imageFile);  
	        //写入数据  
			outStream.write(bytes);
			outStream.close();
		    return imgName;
	   }catch(Exception e) {
		   e.printStackTrace();
		   return null;

	 } finally {
	   socket.close(); 
	 }
   }
   
   public static void dorequest(String httpservice, String raws) throws Exception {
	   
	   
	   Socket socket =null;
	   String host = httpservice.split("://")[1].split(":")[0];
		String protocol = httpservice.split("://")[0];
		int port = 0;
		if (httpservice.split("://")[1].split(":").length ==2) {
			port = Integer.parseInt(httpservice.split("://")[1].split(":")[1]);
		}else if (protocol.equals("https")) {
			port =443;
		}else if (protocol.equals("http")) {
			port = 80;
		}
		System.out.println(host);
		System.out.println(protocol);
		System.out.println(port);
		
      if(protocol.equals("https")) {
		   socket = SSLSocketFactory.getDefault().createSocket(host, port);
	   }
	   else if(protocol.equals("http")) {
		   socket = new Socket(host, port); 
	   }
	   try {
	   /*
	    * 利用字符流处理
       Writer out = new OutputStreamWriter(
          socket.getOutputStream(), "ISO-8859-1");
       out.write(raws);
       out.flush();
       BufferedReader in = new BufferedReader(
          new InputStreamReader(socket.getInputStream(), "ISO-8859-1"));
       String line = null;
       while ((line = in.readLine()) != null) {
          System.out.println(line);
       }
       
	   Writer out = new OutputStreamWriter(
		          socket.getOutputStream(), "ISO-8859-1");
	   out.write(raws);
	   out.flush(); 
	   */
		
		   //利用二进制流处理
	   OutputStream test = socket.getOutputStream(); //发送数据包，以二进制格式
	   test.write(raws.getBytes());
	   test.flush();
	   
       BufferedReader in = new BufferedReader(
    	          new InputStreamReader(socket.getInputStream(), "ISO-8859-1"));
       String line = null;
       while ((line = in.readLine()) != null) {
          System.out.println(line);
       }
     } finally {
       socket.close(); 
     }
   }

}