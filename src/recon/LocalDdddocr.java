package recon;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

public class LocalDdddocr implements IHandler{

	@Deprecated
	public static String execute11(String imagePath) throws Exception {
		Runtime rt = Runtime.getRuntime();String[] commands = {"python","D:\\github\\reCAPTCHA\\script\\ddddocrHandler.py", imagePath};
		Process proc = rt.exec(commands);

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));// read the output from the command

		String s = null;

		while ((s = stdInput.readLine()) != null) {//注意！！只能读取第一行
			System.out.println(s);
			return s;
		}
		return null;
	}
	
	public static String execute(String imagePath) 
	  throws ExecuteException, IOException {
	    String line = "python " + "D:\\github\\reCAPTCHA\\script\\ddddocrHandler.py "+imagePath;
	    CommandLine cmdLine = CommandLine.parse(line);
	        
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
	        
	    DefaultExecutor executor = new DefaultExecutor();
	    executor.setStreamHandler(streamHandler);

	    int exitCode = executor.execute(cmdLine);
	    return outputStream.toString();
	}


	public static void test1() {

	}

	public static void main(String[] args) {

		try {
			System.out.println(execute("D:\\github\\reCAPTCHA\\script\\1111.jpg"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public String getHelpLink() {
		return "";
	}

	@Override
	public String getExampleUserInput() {
		return "python D:\\github\\reCAPTCHA\\script\\ddddocrHandler.py %s";
	}

	@Override
	public String getImageText(String imagePath, String userInputFromGUI) {
		try {
			return execute(imagePath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
