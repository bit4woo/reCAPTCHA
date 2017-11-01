package test;

import java.net.Socket;

public class DoRequest {
	public 
    public static String sendSocketGet(String urlParam, Map<String, Object> params, String charset) {  
        String result = "";  
        // 构建请求参数  
        StringBuffer sbParams = new StringBuffer();  
        if (params != null && params.size() > 0) {  
            for (Entry<String, Object> entry : params.entrySet()) {  
                sbParams.append(entry.getKey());  
                sbParams.append("=");  
                sbParams.append(entry.getValue());  
                sbParams.append("&");  
            }  
        }  
        Socket socket = null;  
        OutputStreamWriter osw = null;  
        InputStream is = null;  
        try {  
            URL url = new URL(urlParam);  
            String host = url.getHost();  
            int port = url.getPort();  
            if (-1 == port) {  
                port = 80;  
            }  
            String path = url.getPath();  
            socket = new Socket(host, port);  
            StringBuffer sb = new StringBuffer();  
            sb.append("GET " + path + " HTTP/1.1\r\n");  
            sb.append("Host: " + host + "\r\n");  
            sb.append("Connection: Keep-Alive\r\n");  
            sb.append("Content-Type: application/x-www-form-urlencoded; charset=utf-8 \r\n");  
            sb.append("Content-Length: ").append(sb.toString().getBytes().length).append("\r\n");  
            // 这里一个回车换行，表示消息头写完，不然服务器会继续等待  
            sb.append("\r\n");  
            if (sbParams != null && sbParams.length() > 0) {  
                sb.append(sbParams.substring(0, sbParams.length() - 1));  
            }  
            osw = new OutputStreamWriter(socket.getOutputStream());  
            osw.write(sb.toString());  
            osw.flush();  
            is = socket.getInputStream();  
            String line = null;  
            // 服务器响应体数据长度  
            int contentLength = 0;  
            // 读取http响应头部信息  
            do {  
                line = readLine(is, 0, charset);  
                if (line.startsWith("Content-Length")) {  
                    // 拿到响应体内容长度  
                    contentLength = Integer.parseInt(line.split(":")[1].trim());  
                }  
                // 如果遇到了一个单独的回车换行，则表示请求头结束  
            } while (!line.equals("\r\n"));  
            // 读取出响应体数据（就是你要的数据）  
            result = readLine(is, contentLength, charset);  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } finally {  
            if (osw != null) {  
                try {  
                    osw.close();  
                } catch (IOException e) {  
                    osw = null;  
                    throw new RuntimeException(e);  
                } finally {  
                    if (socket != null) {  
                        try {  
                            socket.close();  
                        } catch (IOException e) {  
                            socket = null;  
                            throw new RuntimeException(e);  
                        }  
                    }  
                }  
            }  
            if (is != null) {  
                try {  
                    is.close();  
                } catch (IOException e) {  
                    is = null;  
                    throw new RuntimeException(e);  
                } finally {  
                    if (socket != null) {  
                        try {  
                            socket.close();  
                        } catch (IOException e) {  
                            socket = null;  
                            throw new RuntimeException(e);  
                        }  
                    }  
                }  
            }  
        }  
        return result;  
    }  
  
    /** 
     * @Description:读取一行数据，contentLe内容长度为0时，读取响应头信息，不为0时读正文 
     * @time:2016年5月17日 下午6:11:07 
     */  
    private static String readLine(InputStream is, int contentLength, String charset) throws IOException {  
        List<Byte> lineByte = new ArrayList<Byte>();  
        byte tempByte;  
        int cumsum = 0;  
        if (contentLength != 0) {  
            do {  
                tempByte = (byte) is.read();  
                lineByte.add(Byte.valueOf(tempByte));  
                cumsum++;  
            } while (cumsum < contentLength);// cumsum等于contentLength表示已读完  
        } else {  
            do {  
                tempByte = (byte) is.read();  
                lineByte.add(Byte.valueOf(tempByte));  
            } while (tempByte != 10);// 换行符的ascii码值为10  
        }  
  
        byte[] resutlBytes = new byte[lineByte.size()];  
        for (int i = 0; i < lineByte.size(); i++) {  
            resutlBytes[i] = (lineByte.get(i)).byteValue();  
        }  
        return new String(resutlBytes, charset);  
    }  
      
}  

}
