package deprecated;
/** 
* @author bit4woo
* @github https://github.com/bit4woo 
* @version CreateTime：Jun 13, 2020 4:10:13 PM 
*/

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class jsdatiTest {

	    public static void main(String[] args) {

	    	String BOUNDARY = "---------------------------68163001211748"; //boundary就是request头和上传文件内容的分隔符
	        String str="http://v1-http-api.jsdama.com/api.php?mod=php&act=upload";
	        String filePath="D:/github/reCAPTCHA/src/deprecated/testImage.jpeg";//本地图片路径
	        Map<String, String> paramMap = getParamMap();
	        try {
	            URL url=new URL(str);
	            
    			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
	            HttpURLConnection connection=(HttpURLConnection)url.openConnection(proxy);
	            
	            connection.setDoInput(true);
	            connection.setDoOutput(true);
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("content-type", "multipart/form-data; boundary="+BOUNDARY);
	            connection.setConnectTimeout(30000);
	            connection.setReadTimeout(30000);
	            
	            OutputStream out = new DataOutputStream(connection.getOutputStream());
				// 普通参数
				if (paramMap != null) {
					StringBuffer strBuf = new StringBuffer();
					Iterator<Entry<String, String>> iter = paramMap.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry<String,String> entry = iter.next();
						String inputName = entry.getKey();
						String inputValue = entry.getValue();
						strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
						strBuf.append("Content-Disposition: form-data; name=\""
								+ inputName + "\"\r\n\r\n");
						strBuf.append(inputValue);
					}
					out.write(strBuf.toString().getBytes());
				}
				
				// 图片文件
				if (filePath != null) {
					File file = new File(filePath);
					String filename = file.getName();
					String contentType = "image/jpeg";//这里看情况设置
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ "upload" + "\"; filename=\"" + filename+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());
					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
				byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
				out.write(endData);
				out.flush();
				out.close();
	            
	            //读取URLConnection的响应
	            InputStream in = connection.getInputStream();
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				while (true) {
					int rc = in.read(buf);
					if (rc <= 0) {
						break;
					} else {
						bout.write(buf, 0, rc);
					}
				}
				in.close();
				//结果输出
				System.out.println(new String(bout.toByteArray()));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	/**
	 * 参数信息
	 * @return
	 */
	private static Map<String, String> getParamMap() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("user_name", "qq315737546");
		paramMap.put("user_pw", "qq315737546");
		paramMap.put("yzm_minlen", "4");
		paramMap.put("yzm_maxlen", "4");
		paramMap.put("yzmtype_mark", "0");
		paramMap.put("zztool_token", "");
		
		return paramMap;
	}

}
