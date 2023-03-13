package recon;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class LocalDdddocr implements IHandler{

	public static String execute(String imagePath) throws Exception {
		Runtime rt = Runtime.getRuntime();String[] commands = {"python","D:\\github\\PackageHanlder\\js\\AES-encry.js", imagePath};
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


	public static void test1() {

	}

	public static void main(String[] args) {

		String body = "{\"loginName\":\"admin\",\"kaptcha\":\"d4mb\",\"password\":\"admin\",\"timeStamp\":1666428310974}";
		JSONObject Json= new JSONObject(body);
		Json.put("password", "xxxx");
		System.out.println(Json.toString());
	}


	@Override
	public String getHelpLink() {
		return "";
	}

	@Override
	public String getExampleUserInput() {
		return "{\"python\",\"D:\\\\github\\\\PackageHanlder\\\\js\\\\AES-encry.js\", imagePath}";
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
