package deprecated;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import httpbase.Request;
import httpbase.Response;

@Deprecated
public class DoRequestWithSocket {

	private Request request;
	private Response response;
	private Socket socket;

	public static void main(String[] args) {
	}

	private static byte[] readInputStream(InputStream inStream) throws Exception{  
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
		byte[] buffer = new byte[1024];  
		int len = 0;
		while( (len=inStream.read(buffer)) != -1 ){  
			outStream.write(buffer, 0, len);  
		}  
		inStream.close();  
		return outStream.toByteArray();  
	}

	/*
	 * 发送请求，返回body的byte[]
	 */
	public void makeHttpRequest(String httpService,byte[] request) throws Exception {
		Request req = new Request(httpService,request);
		try {  
			if(req.getProtocol().equalsIgnoreCase("https:")) {
				//前提条件是要正确配置证书？？？
				socket = SSLSocketFactory.getDefault().
						createSocket(req.getHost(), req.getPort());
			}else {
				URL httpUrl = new URL(req.getUrl());
			}

			try {
				Writer out = new OutputStreamWriter(
						socket.getOutputStream(), "ISO-8859-1");
				out.write("GET / HTTP/1.1\r\n");  
				out.write("Host: " + req.getHost() + ":" + 
						req.getPort() + "\r\n");  
				out.write("Agent: SSL-TEST\r\n");  
				out.write("\r\n");  
				out.flush();  
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream(), "ISO-8859-1"));
				String line = null;
				while ((line = in.readLine()) != null) {
					System.out.println(line);
				}
			} finally {
				socket.close(); 
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}  
	}
}