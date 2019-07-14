package burp;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import custom.GUI;

public class BurpExtender extends GUI implements IBurpExtender, ITab, IContextMenuFactory, IIntruderPayloadGeneratorFactory,IIntruderPayloadGenerator
{	
	private static IBurpExtenderCallbacks callbacks;
	private static IExtensionHelpers helpers;

	private String ExtenderName = "reCAPTCHA v0.8 by bit4";
	private String github = "https://github.com/bit4woo/reCAPTCHA";
	public static PrintWriter stdout;
	public static PrintWriter stderr;

	private static String currentImgName = null;
	private static IHttpRequestResponse imgMessageInfo;
	IMessageEditor imageMessageEditor;

	@Override
	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
	{
		stdout = new PrintWriter(callbacks.getStdout(), true);
		stderr = new PrintWriter(callbacks.getStderr(), true);
		stdout.println(ExtenderName);
		stdout.println(github);
		this.callbacks = callbacks;
		helpers = callbacks.getHelpers();
		callbacks.setExtensionName(ExtenderName); //插件名称
		//callbacks.registerHttpListener(this); //如果没有注册，下面的processHttpMessage方法是不会生效的。处理请求和响应包的插件，这个应该是必要的
		callbacks.registerContextMenuFactory(this);
		callbacks.registerIntruderPayloadGeneratorFactory(this);
		callbacks.addSuiteTab(BurpExtender.this);
	}

	/////////////////////////////////////////自定义函数/////////////////////////////////////////////////////////////
	public static IBurpExtenderCallbacks getBurpCallbacks() {
		return callbacks;
	}

	public static String getCurrentImgName() {
		return currentImgName;
	}

	public static void setCurrentImgName(String currentImgName) {
		BurpExtender.currentImgName = currentImgName;
	}

	public static IHttpRequestResponse getImgMessageInfo() {
		return imgMessageInfo;
	}

	public static void setImgMessageInfo(IHttpRequestResponse imgMessageInfo) {
		BurpExtender.imgMessageInfo = imgMessageInfo;
	}

	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
		return bs;
	}

	public String getHost(IRequestInfo analyzeRequest){
		List<String> headers = analyzeRequest.getHeaders();
		String domain = "";
		for(String item:headers){
			if (item.toLowerCase().contains("host")){
				domain = new String(item.substring(6));
			}
		}
		return domain ;
	}
	public static String getFileType(IResponseInfo analyzeResponse) {
		String fileType = null;    
		List<String> headers = analyzeResponse.getHeaders();

		for(String header:headers) {
			if(header.toLowerCase().startsWith("content-type")) {
				try {
					fileType= header.substring(header.indexOf("/")+1, header.indexOf(";"));
				}catch(Exception e) {
					fileType= header.substring(header.indexOf("/")+1, header.length());
				}
			}
		}
		return fileType;
	}

	public static String getImage(IHttpRequestResponse messageInfo) {
		if (messageInfo != null) {
			IHttpService service = messageInfo.getHttpService();
			byte[] request =  messageInfo.getRequest();
			IHttpRequestResponse messageInfo_issued = callbacks.makeHttpRequest(service,request);

			byte[] response = messageInfo_issued.getResponse();
			if (response ==null) return null;
			int BodyOffset = helpers.analyzeResponse(response).getBodyOffset();
			int body_length = response.length -BodyOffset;
			byte[] body = subBytes(response,BodyOffset,body_length);
			//这里之前遇到一个坑：现将byte[]转换为string，取substring后转换回来，这样是有问题的。

			String fileType =getFileType(helpers.analyzeResponse(messageInfo.getResponse())); 
			if(fileType==null) {
				fileType ="jpg";
			}

			currentImgName = System.currentTimeMillis()+service.getHost()+"."+fileType;
			//stdout.println(imgName);
			try {
				File imageFile = new File(currentImgName);
				//创建输出流  
				FileOutputStream outStream = new FileOutputStream(imageFile);  
				//写入数据  
				outStream.write(body);
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return currentImgName;
		}else {
			return null;
		}
	}

	///////////////////////////////////自定义函数////////////////////////////////////////////////////////////


	///////////////////////////////////以下是各种burp必须的方法 --start//////////////////////////////////////////


	//ITab必须实现的两个方法
	@Override
	public String getTabCaption() {
		return ("reCAPTCHA");
	}
	@Override
	public Component getUiComponent() {
		return this.getContentPane();
	}
	public BurpExtender getThis() {
		return this;
	}

	@Override
	public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation)
	{ //需要在签名注册！！callbacks.registerContextMenuFactory(this);
		IHttpRequestResponse[] messages = invocation.getSelectedMessages();
		List<JMenuItem> list = new ArrayList<JMenuItem>();
		if((messages != null) && (messages.length ==1))
		{	
			imgMessageInfo = messages[0];

			final byte[] sentRequestBytes = messages[0].getRequest();
			IRequestInfo analyzeRequest = helpers.analyzeRequest(sentRequestBytes);

			JMenuItem menuItem = new JMenuItem("Send to reCAPTCHA");
			menuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						imgRequestRaws.setText(new String(imgMessageInfo.getRequest())); //在GUI中显示这个请求信息。
						IHttpService httpservice =imgMessageInfo.getHttpService();
						imgHttpService.setText(httpservice.toString());
					}
					catch (Exception e1)
					{
						e1.printStackTrace(stderr);
					}
				}
			});
			list.add(menuItem);
		}
		return list;
	}


	//IIntruderPayloadGeneratorFactory 所需实现的2个函数
	@Override
	public String getGeneratorName() {
		return "reCAPTCHA";
	}

	@Override
	public IIntruderPayloadGenerator createNewInstance(IIntruderAttack attack) {

		return this;
	}



	//IIntruderPayloadGenerator 所需实现的三个函数
	@Override
	public boolean hasMorePayloads() {
		return true;
	}

	@Override
	public byte[] getNextPayload(byte[] baseValue) {
		// 获取图片验证码的值
		int times = 0;
		while(times <=5) {
			if (imgMessageInfo!=null) {
				try {					
					//String imgpath = imageDownloader.download(callbacks, helpers, imgMessageInfo.getHttpService(), imgMessageInfo.getRequest());
					String imgpath = this.getImage(imgMessageInfo);
					String code = getAnswer(imgpath);
					stdout.println(imgpath+" ---- "+code);
					return code.getBytes();
				} catch (Exception e) {
					e.printStackTrace(stderr);
					return e.getMessage().getBytes();
				}
			}else {
				stdout.println("Failed try!!! please send image request to reCAPTCHA first!");
				times +=1;
				continue;
			}
		}
		return null;
	}

	@Override
	public void reset() {

	}

	//////////////////////////////////////////////各种burp必须的方法 --end//////////////////////////////////////////////////////////////
}