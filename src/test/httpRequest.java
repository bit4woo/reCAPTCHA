package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;  
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.URL;  
/**
 * @说明 从网络获取图片到本地
 * @version 1.0
 * @since
 */  
public class httpRequest {  
    /**
     * 测试
     * @param args
     */  
    public static void main(String[] args) {
    	writeImageToDisk(getImageFromNetByUrl("https://ecss.pingan.com/createImageCode?t=1509343555818"),"test.jpg");
    }

    /**
     * 将图片写入到磁盘
     * @param img 图片数据流
     * @param img_save_path 文件保存时的名称
     */  
    public static void writeImageToDisk(byte[] img, String img_save_path){
        try {  
            File file = new File(img_save_path);
            FileOutputStream fops = new FileOutputStream(file);  
            fops.write(img);  
            fops.flush();  
            fops.close();
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    /**
     * 根据地址获得数据的字节流
     * @param strUrl 网络连接地址
     * @return
     */  
    public static byte[] getImageFromNetByUrl(String strUrl){  
        try {  
            URL url = new URL(strUrl);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.addRequestProperty("Referer",strUrl);
            conn.addRequestProperty("User-Agent","Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:54.0) Gecko/20100101 Firefox/54.0");
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(8*1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
            return btImg;  
        } catch (Exception e) {
            System.out.println("请求图片异常："+e.getLocalizedMessage());
        }  
        return null;  
    }  
    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */  
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
}