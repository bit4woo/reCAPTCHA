package httpbase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Response {

	//解析后的内部变量
	private byte[] head;
	private byte[] body;
	
	//解析后的外部常用变量
	private int statusCode;
	private String mimeType;
	private List<String> headerList;
	private HashMap<String,String> headerMap = new HashMap<String,String>();

	public static void main(String args[]) {		

		String test = "POST /areashopAdmin/admin/privilege.php?act=logout HTTP/1.1\r\n" + 
				"Host: 123.58.36.99\r\n" + 
				"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0\r\n" + 
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" + 
				"Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\r\n" + 
				"Accept-Encoding: gzip, deflate\r\n" + 
				"Content-Type: application/x-www-form-urlencoded\r\n" + 
				"Content-Length: 57\r\n" + 
				"Origin: http://123.58.36.99\r\n" + 
				"Connection: close\r\n" + 
				"Referer: http://123.58.36.99/areashopAdmin/admin/privilege.php?act=logout\r\n" + 
				"Cookie: ECS_LastCheckOrder=Fri%2C%2012%20Jun%202020%2006%3A21%3A39%20GMT; ECS[visit_times]=1; ECS_ID=b3df2347fdb99858615d19434ae7116ce8e86e23; ECSCP_ID=5eea85d0f71afc685fdd251dbe3f3c8393860aec\r\n" + 
				"Upgrade-Insecure-Requests: 1\r\n" + 
				"\r\n" + 
				"username=areashop&password=123456&captcha=z69k&act=signin";
		new Response(test.getBytes());
	}


	public Response(byte[] rawResponse) {
		parse(rawResponse);
	}

	private void parse(byte[] rawResponse) {
		int bodyOffset = findBodyOffset(rawResponse);
		System.out.println(bodyOffset);
		head = Arrays.copyOfRange(rawResponse, 0, bodyOffset-4);
		body = Arrays.copyOfRange(rawResponse, bodyOffset, rawResponse.length);//not length-1
		
		//System.arraycopy(rawResponse, 0, head, 0, bodyOffset-4);//0~bodyOffset-4-1
		//System.arraycopy(rawResponse, bodyOffset, body, 0, rawResponse.length-bodyOffset);

		//		System.out.println(new String(head));
		//		System.out.println();
		//		System.out.println();
		//		System.out.println(new String(body));

		String headString = new String(head);
		List<String> headerList = Arrays.asList(headString.split("\r\n"));

		String firstLine = headerList.get(0);
		statusCode = Integer.parseInt(firstLine.split(" ")[1]);

		headerList.remove(0);//移除第一行

		for (String line:headerList) {
			String[] keyAndValue = line.split(":",2);
			String key = keyAndValue[0];
			String value = keyAndValue[1].trim();
			headerMap.put(key, value);
		}


		mimeType = headerMap.get("Content-Type");
		if (mimeType != null) {
			mimeType = mimeType.split(";")[0];
		}
	}

	private static int findBodyOffset(byte[] requestOrResponse) {
		for(int i =0;i<requestOrResponse.length-4;i++) {
			byte[] item = {requestOrResponse[i],requestOrResponse[i+1],requestOrResponse[i+2],requestOrResponse[i+3]};
			//System.out.println("---"+new String(item)+"---");
			if (Arrays.equals(item,"\r\n\r\n".getBytes())) {
				return i+4;//指向body中的第一个字符
			}
		}
		return -1;
	}

	List<String> getHeaders(){
		return headerList;
	}

	public HashMap<String, String> getHeaderMap() {
		return headerMap;
	}

	int getStatusCode() {
		return statusCode;
	}

	String getMimeType() {
		return mimeType;
	}

}
