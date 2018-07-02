package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class grep {
	public static String grepResult(String Html)
	{	
	      String pattern = "<span id=\"captcha_result\">(.*?)</span>";
	 
	      // 创建 Pattern 对象
	      Pattern r = Pattern.compile(pattern);
	      
	      // 现在创建 matcher 对象
	      Matcher m = r.matcher(Html);
	      if (m.find( )) {
	         System.out.println("Found value: " + m.group(1) );
	         return m.group(1);
	      } else {
	         System.out.println("NO MATCH");
	         return m.group(0);
	      }
	 }
	
	public static void main(String[] args) {
		String xxxx = "<span id=\"captcha_result\">1bcd3</span>";
		System.out.print(grepResult(xxxx));
	}
}
