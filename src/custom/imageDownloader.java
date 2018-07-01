package custom;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
//https://oms.meizu.com:8443/cas/captcha.htm
//http://www.faithfulfitnessforlife.com/fitness_blog/Captcha.aspx
public class imageDownloader {
	URL url = new URL("http://www.yahoo.com/image_to_read.jpg");
	InputStream in = new BufferedInputStream(url.openStream());
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	byte[] buf = new byte[1024];
	int n = 0;
	while (-1!=(n=in.read(buf)))
	{
	   out.write(buf, 0, n);
	}
	out.close();
	in.close();
	byte[] response = out.toByteArray();
	FileOutputStream fos = new FileOutputStream("C://borrowed_image.jpg");
	fos.write(response);
	fos.close();
}
