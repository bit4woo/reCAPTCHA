package test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import burp.BurpExtender;


public class YunSutest1 {

	public static void main(String [] args) {
		String a = burp.BurpExtender.getCode("E:\\wolaidai\\==wininit==\\work\\ecss.pingan.com1509004978504.jpg");
		System.out.println(a);
		System.out.println(displayXmlResult(a));
		
		
	}
	public static String displayXmlResult(String xml) {
    	try {
        	Document dm;
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		dm = db.parse(new ByteArrayInputStream(xml.getBytes("utf-8")));
			NodeList resultNl = dm.getElementsByTagName("Result");
			return resultNl.item(0).getFirstChild().getNodeValue();
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
        return null;
    }
}
