package deprecated;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import httpbase.MyX509TrustManager;

@Deprecated
public class HttpUtil {
	/**
	 * 发起http请求并获取结果
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据       格式（例子："name=name&age=age"）  // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
	 * @return json字符串（json格式不确定 可能是JSONObject，也可能是JSONArray，这里用字符串，在controller里再转化）
	 */
	public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
		String resultStr = "";
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			//HttpURLConnection是基于HTTP协议的，其底层通过socket通信实现。如果不设置超时（timeout），在网络异常的情况下，可能会导致程序僵死而不继续往下执行
			httpUrlConn.setConnectTimeout(30*1000);//30s超时
			httpUrlConn.setReadTimeout(10*1000);//10s超时

			/*
			//设置请求属性    
			httpUrlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");     
			httpUrlConn.setRequestProperty("Charset", "UTF-8");
		    */
	        
			//HttpURLConnection的connect()函数，实际上只是建立了一个与服务器的tcp连接，并没有实际发送http请求。
			//get方式需要显式连接
			if ("GET".equalsIgnoreCase(requestMethod)){
				httpUrlConn.connect();
			}
			
			//这种post方式，隐式自动连接
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			
			resultStr = buffer.toString();
		} catch (ConnectException ce) {
			System.out.println("server connection timed out.");
		} catch (Exception e) {
			System.out.println(requestUrl+" request error:\n"+e);
		}
		return resultStr;
	}
	
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据       格式（例子："name=name&age=age"）  // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
	 * @return json字符串（json格式不确定 可能是JSONObject，也可能是JSONArray，这里用字符串，在controller里再转化）
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		String resultStr = "";
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			//HttpURLConnection是基于HTTP协议的，其底层通过socket通信实现。如果不设置超时（timeout），在网络异常的情况下，可能会导致程序僵死而不继续往下执行
			httpUrlConn.setConnectTimeout(30*1000);//30s超时
			httpUrlConn.setReadTimeout(10*1000);//10s超时

			/*
			//设置请求属性    
			httpUrlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");     
			httpUrlConn.setRequestProperty("Charset", "UTF-8");
		    */
	        
			//HttpURLConnection的connect()函数，实际上只是建立了一个与服务器的tcp连接，并没有实际发送http请求。
			//get方式需要显式连接
			if ("GET".equalsIgnoreCase(requestMethod)){
				httpUrlConn.connect();
			}
			
			//这种post方式，隐式自动连接
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			
			resultStr = buffer.toString();
		} catch (ConnectException ce) {
			System.out.println("server connection timed out.");
		} catch (Exception e) {
			System.out.println(requestUrl+" request error:\n"+e);
		}
		return resultStr;
	}
	
	public static void main(String[] args) {
		System.out.println(httpRequest("https://www.zhihu.com/", "GET", null));
	}
	
}