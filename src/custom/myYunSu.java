package custom;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class myYunSu {
	public static void main(String[] args) {
		String parastring = "username=xxxx&password=xxxx&typeid=3040&timeout=60&softid=66239&softkey=a44fbc0b1900420681e436fc424cbd86";
		String imgPath = "E:\\wolaidai\\==wininit==\\work\\www.cnhww.com1509603432430.bmp";
		System.out.print(getCode(imgPath,parastring));
	}
	
	public static HashMap<String,String> getConfig(String paraString) {
		HashMap<String,String> paraMap = new HashMap<String,String>();
		String[] tmp = paraString.trim().split("&");
		for(int i=0;i < tmp.length;i++) {
			String key = tmp[i].split("=")[0];
			String value = tmp[i].split("=")[1];
			paraMap.put(key, value);
		}
		return paraMap;
	}
	
    public static String getCode(String imagePath,String paraString) {//GEN-FIRST:event_jButton1ActionPerformed
        //        
    	HashMap<String,String> paraMap = getConfig(paraString);
    	String softid = "66182";
        String softkey = "d1003debf03b488fae7064b48f5b6ef8";
        String typeid =  paraMap.get("typeid");
        
        String username = paraMap.get("username");
        String password = paraMap.get("password");
        File img = new File(imagePath);
        if(img.exists())
        {
			String result = YunSu.createByPost(username, password, typeid, "90", softid, softkey, imagePath);
			String code = XmlResultParser(result);
			if (code != null) {
				return code;
			}else {
				return result;
				//return new String (result.getBytes("utf-8"),"utf-8");
			}
        }
        return null;
    }
    
	public static String XmlResultParser(String xml) {
    	try {
        	Document dm;
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		dm = db.parse(new ByteArrayInputStream(xml.getBytes("utf-8")));
			NodeList resultNl = dm.getElementsByTagName("Result");
			//System.out.println(resultNl);
			//System.out.println(resultNl.item(0));
			return resultNl.item(0).getFirstChild().getNodeValue();
			
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
    }
    
}
