package custom;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class myGSA {
	public static void main(String[] args) {
		String httpService = "127.0.0.1";
		String imgPath = "C:\\Users\\PC\\Desktop\\Captcha.jpg";
		System.out.print(getCode(imgPath,httpService));
	}
	
    public static String getCode(String imagePath,String httpService) {
    	String code ="";
		try {
			File f = new File(imagePath);
			int size = (int) f.length();
			byte[] data = new byte[size];
			FileInputStream fis;
			fis = new FileInputStream(f);
			fis.read(data, 0, size);
			if(null != fis) fis.close();
			code = httpPostImage(httpService+"/gsa_test.gsa",data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
    }
    /**
	 * 答题
	 * @param url 			请求URL，不带参数 如：http://127.0.0.1/gsa_test.gsa
	 * @param data			图片二进制流

	 * @throws IOException
	 */
	public static String httpPostImage(String url,byte[] data) throws IOException {
		long time = (new Date()).getTime();
		URL u = null;
		HttpURLConnection con = null;
		String boundary = "----------" + (String.valueOf(time));
		String boundarybytesString = "\r\n--" + boundary + "\r\n";
		OutputStream out = null;
		
		u = new URL(url);
		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
		//con = (HttpURLConnection) u.openConnection(proxy);
		con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod("POST");
		con.setReadTimeout(10000);   
		con.setConnectTimeout(10000); //10min
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setUseCaches(true);
		con.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		
		out = con.getOutputStream();

		out.write(boundarybytesString.getBytes("UTF-8"));

		String paramString = "Content-Disposition: form-data; name=\"file\"; filename=\""
				+ "sample.gif" + "\"\r\nContent-Type: image/gif\r\n\r\n";
		out.write(paramString.getBytes("UTF-8"));
		
		out.write(data);
		
		String tailer = "\r\n--" + boundary + "--\r\n";
		
		out.write(tailer.getBytes("UTF-8"));
		out.write("Content-Disposition: form-data; name=\"source_url\"\r\n".getBytes());
		
		out.write(tailer.getBytes("UTF-8"));
		out.write("Content-Disposition: form-data; name=\"captcha_platform\"\r\n".getBytes());
		
		out.write(tailer.getBytes("UTF-8"));
		out.write(("Content-Disposition: form-data; name=\"action\"\r\n" +"\r\n" + "Submit").getBytes());
		
		out.write(tailer.getBytes("UTF-8"));
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
		String html = buffer.toString();
		return grepResult(html);
	}
	
	public static String grepResult(String Html)
	{	
	      String pattern = "<span id=\"captcha_result\">(.*?)</span>";
	      Pattern r = Pattern.compile(pattern);
	      Matcher m = r.matcher(Html);
	      if (m.find( )) {
	         System.out.println("Found value: " + m.group(1) );
	         return m.group(1);
	      } else {
	         System.out.println("NO MATCH");
	         return Html;
	      }
	 }
}
