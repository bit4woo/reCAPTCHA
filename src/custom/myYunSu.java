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
		String parastring = "username=komi_long&password=komi2016&typeid=2040&timeout=90&softid=1&softkey=b40ffbee5c1cf4e38028c197eb2fc751";
		String imgPath = "E:\\wolaidai\\==wininit==\\work\\ecss.pingan.com1509447585218.jpg";
		System.out.print(getCode(imgPath,parastring));
	}
	
	public static HashMap<String,String> getConfig(String paraString) {
		HashMap<String,String> paraMap = new HashMap<String,String>();
		String[] tmp = paraString.split("&");
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
    	String softid = paraMap.get("softid");
        String softkey = paraMap.get("softkey");
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
				try {
					return new String(result.getBytes(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
