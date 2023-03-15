package recon;

public interface IHandler {
	
	/*
	 * 获取实现类相关帮助链接
	 */
	public String getHelpLink();
	
	/*
	 * 实现类需要用户输入的内容的模板，显示到GUI上，用户自行编辑修改后，用于getImageText函数
	 */
	public String getExampleUserInput();
	
	/*
	 * 获取图片上面的文本内容，即图形验证码的内容。
	 */
	public String getImageText(String imagePath,String userInputFromGUI,String proxyUrl);
	
}