package custom;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;

import javax.imageio.ImageIO;

public class YunSu {
	
	/**
	 * 字符串MD5加密
	 * @param s 原始字符串
	 * @return  加密后字符串
	 */
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 通用URL请求方法
	 * @param url 		请求URL，不带参数 如：http://api.ysdm.net/register.xml
	 * @param param 	请求参数，如：username=test&password=1
	 * @return 			平台返回结果XML样式 
	 * @throws IOException
	 */
	public static String httpRequestData(String url, String param)
			throws IOException {
		URL u;
		HttpURLConnection con = null;
		OutputStreamWriter osw;
		StringBuffer buffer = new StringBuffer();

		u = new URL(url);
		con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
		osw.write(param);
		osw.flush();
		osw.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(con
				.getInputStream(), "UTF-8"));
		String temp;
		while ((temp = br.readLine()) != null) {
			buffer.append(temp);
			buffer.append("\n");
		}

		return buffer.toString();
	}

	/**
	 * 答题
	 * @param url 			请求URL，不带参数 如：http://api.ysdm.net/create.xml
	 * @param param			请求参数，如：username=test&password=1
	 * @param data			图片二进制流
	 * @return				平台返回结果XML样式 
	 * @throws IOException
	 */
	public static String httpPostImage(String url, String param,
			byte[] data) throws IOException {
		long time = (new Date()).getTime();
		URL u = null;
		HttpURLConnection con = null;
		String boundary = "----------" + MD5(String.valueOf(time));
		String boundarybytesString = "\r\n--" + boundary + "\r\n";
		OutputStream out = null;
		
		u = new URL(url);
		
		con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod("POST");
		//con.setReadTimeout(95000);   
		con.setConnectTimeout(95000); //此值与timeout参数相关，如果timeout参数是90秒，这里就是95000，建议多5秒
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setUseCaches(true);
		con.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		
		out = con.getOutputStream();
			
		for (String paramValue : param.split("[&]")) {
			out.write(boundarybytesString.getBytes("UTF-8"));
			String paramString = "Content-Disposition: form-data; name=\""
					+ paramValue.split("[=]")[0] + "\"\r\n\r\n" + paramValue.split("[=]")[1];
			out.write(paramString.getBytes("UTF-8"));
		}
		out.write(boundarybytesString.getBytes("UTF-8"));

		String paramString = "Content-Disposition: form-data; name=\"image\"; filename=\""
				+ "sample.gif" + "\"\r\nContent-Type: image/gif\r\n\r\n";
		out.write(paramString.getBytes("UTF-8"));
		
		out.write(data);
		
		String tailer = "\r\n--" + boundary + "--\r\n";
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

		return buffer.toString();
	}

	/**
	 * 获取用户信息
	 * @param username	用户名
	 * @param password	密码
	 * @return			平台返回结果XML样式 
	 * @throws IOException
	 */
	public static String getInfo(String username, String password) {
		String param = String.format("username=%s&password=%s", username, password);
		String result;
		try {
			result = YunSu.httpRequestData(
					"http://api.ysdm.net/info.xml", param);
		} catch (IOException e) {
			result = "未知问题";
		}
		return result;
	}
	
	/**
	 * 注册用户
	 * @param username	用户名
	 * @param password	密码
	 * @param email		邮箱
	 * @return			平台返回结果XML样式 
	 * @throws IOException
	 */
	public static String register(String username, String password, String email) {
		String param = String.format("username=%s&password=%s&email=%s", username, password, email);
		String result;
		try {
			result = YunSu.httpRequestData(
					"http://api.ysdm.net/register.xml", param);
		} catch (IOException e) {
			result = "未知问题";
		}
		return result;
	}

	/**
	 * 充值
	 * @param username	用户名
	 * @param id		卡号
	 * @param password	密码
	 * @return			平台返回结果XML样式 
	 * @throws IOException
	 */
	public static String recharge(String username, String id, String password) {

		String param = String.format("username=%s&password=%s&id=%s", username,
				password, id);
		String result;
		try {
			result = YunSu.httpRequestData(
					"http://api.ysdm.net/recharge.xml", param);
		} catch (IOException e) {
			result = "未知问题";
		}
		return result;
	}
	
	/**
	 * 答题(URL) 
	 * @param username	用户名
	 * @param password	用户密码。(支持32位MD5)
	 * @param typeid	题目类型
	 * @param timeout	任务超时时间，默认与最小值为60秒。
	 * @param softid	软件ID，开发者可自行申请。
	 * @param softkey	软件KEY，开发者可自行申请。
	 * @param imageurl	远程图片URL
	 * @return			平台返回结果XML样式 
	 * @throws IOException
	 */
	public static String createByUrl(String username, String password,
			String typeid, String timeout, String softid, String softkey,
			String imageurl) {

		String param = String
				.format(
						"username=%s&password=%s&typeid=%s&timeout=%s&softid=%s&softkey=%s",
						username, password, typeid, timeout, softid, softkey);
		ByteArrayOutputStream baos = null;
		String result;
		try {
			URL u = new URL(imageurl);
			BufferedImage image = ImageIO.read(u);
			   
			baos = new ByteArrayOutputStream();
			ImageIO.write( image, "jpg", baos);
			baos.flush();
			byte[] data = baos.toByteArray();
			baos.close();
			
			result = YunSu.httpPostImage(
					"http://api.ysdm.net/create.xml", param, data);
	
			
		} catch(Exception e) {
			result = "未知问题";
		}
		return result;
	}
	
	/**
	 * 上报错题
	 * @param username	用户名
	 * @param password	用户密码
	 * @param softId	软件ID
	 * @param softkey	软件KEY
	 * @param id		报错题目的ID
	 * @return
	 * @throws IOException
	 */
	public static String report(String username, String password, String softid, String softkey, String id) {
		
		String param = String
		.format(
				"username=%s&password=%s&softid=%s&softkey=%s&id=%s",
				username, password, softid, softkey, id);
		String result;
		try {
			result = YunSu.httpRequestData("http://api.ysdm.net/reporterror.xml",
					param);
		} catch (IOException e) {
			result = "未知问题";
		}
		
		return result;
	}
	
	/**
	 * 上传题目图片返回结果	
	 * @param username		用户名
	 * @param password		密码
	 * @param typeid		题目类型
	 * @param timeout		任务超时时间
	 * @param softid		软件ID
	 * @param softkey		软件KEY
	 * @param filePath		题目截图或原始图二进制数据路径
	 * @return
	 * @throws IOException
	 */
	public static String createByPost(String username, String password,
			String typeid, String timeout, String softid, String softkey,
			String filePath) {
		String result = "";
		String param = String
		.format(
				"username=%s&password=%s&typeid=%s&timeout=%s&softid=%s&softkey=%s",
				username, password, typeid, timeout, softid, softkey);
		try {
			File f = new File(filePath);
			if (null != f) {
				int size = (int) f.length();
				byte[] data = new byte[size];
				FileInputStream fis = new FileInputStream(f);
				fis.read(data, 0, size);
				if(null != fis) fis.close();
				
				if (data.length > 0)	result = YunSu.httpPostImage("http://api.ysdm.net/create.xml", param, data);
			}
		} catch(Exception e) {
			result = "未知问题";
		}
		
		
		return result;
	}
	
	public static String createByPost(String username, String password,
			String typeid, String timeout, String softid, String softkey,
			byte[] byteArr) {
		String result = "";
		String param = String
		.format(
				"username=%s&password=%s&typeid=%s&timeout=%s&softid=%s&softkey=%s",
				username, password, typeid, timeout, softid, softkey);
		try {
			result = YunSu.httpPostImage("http://api.ysdm.net/create.xml", param, byteArr);
		} catch(Exception e) {
			result = "未知问题";
		}
		
		
		return result;
	}

}
