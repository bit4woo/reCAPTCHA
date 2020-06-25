package burp;

/** 
* @author bit4woo
* @github https://github.com/bit4woo 
* @version CreateTime：Jun 25, 2020 3:21:11 PM 
*/

public class Info {
	public static String ExtensionName = "reCAPTCHA";
	public static String Version = bsh.This.class.getPackage().getImplementationVersion();
	public static String Author = "by bit4woo";	
	public static String github = "https://github.com/bit4woo/reCAPTCHA";

	//name+version+author
	public static String getFullExtensionName(){
		return ExtensionName+" "+Version+" "+Author;
	}
}
