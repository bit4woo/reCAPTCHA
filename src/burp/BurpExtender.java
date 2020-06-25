package burp;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import custom.GUI;
import custom.ImageHandler;

public class BurpExtender extends GUI implements IBurpExtender, ITab, IContextMenuFactory, IIntruderPayloadGeneratorFactory,IIntruderPayloadGenerator
{	
	private static IBurpExtenderCallbacks callbacks;
	private static IExtensionHelpers helpers;

	public static PrintWriter stdout;
	public static PrintWriter stderr;

	private static IHttpRequestResponse imgMessageInfo;
	IMessageEditor imageMessageEditor;

	@Override
	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
	{
		BurpExtender.callbacks = callbacks;
		helpers = callbacks.getHelpers();
		flushStd();
		stdout.println(Info.getFullExtensionName());
		stdout.println(Info.github);
		callbacks.setExtensionName(Info.getFullExtensionName()); //插件名称
		//callbacks.registerHttpListener(this); //如果没有注册，下面的processHttpMessage方法是不会生效的。处理请求和响应包的插件，这个应该是必要的
		callbacks.registerContextMenuFactory(this);
		callbacks.registerIntruderPayloadGeneratorFactory(this);
		callbacks.addSuiteTab(BurpExtender.this);
	}

	private static void flushStd(){
		try{
			stdout = new PrintWriter(callbacks.getStdout(), true);
			stderr = new PrintWriter(callbacks.getStderr(), true);
		}catch (Exception e){
			stdout = new PrintWriter(System.out, true);
			stderr = new PrintWriter(System.out, true);
		}
	}

	/////////////////////////////////////////自定义函数/////////////////////////////////////////////////////////////
	public static IBurpExtenderCallbacks getBurpCallbacks() {
		return callbacks;
	}

	public static IHttpRequestResponse getImgMessageInfo() {
		return imgMessageInfo;
	}

	public static void setImgMessageInfo(IHttpRequestResponse imgMessageInfo) {
		BurpExtender.imgMessageInfo = imgMessageInfo;
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

	public static String getImage(IHttpRequestResponse messageInfo) {
		if (messageInfo == null) {
			return null;
		}
		try {
			IHttpService service = messageInfo.getHttpService();
			byte[] request =  messageInfo.getRequest();
			if (GUI.rdbtnUseSelfApi.isSelected()) {
				String proxy = GUI.proxyUrl.getText().trim();
				return ImageHandler.download(service, request, proxy);
			}else {
				return ImageHandler.downloadWithBurpMethod(service,request);
			}
		} catch (Exception e) {
			e.printStackTrace(stderr);
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