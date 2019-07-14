package burp;

import java.util.concurrent.Callable;

import javax.swing.JTextField;

public class GetImageThread extends Thread {
	private IHttpRequestResponse MessageInfo;

	public GetImageThread(IHttpRequestResponse MessageInfo) {
		this.MessageInfo = MessageInfo;
	}
	public void run() {
		BurpExtender.setCurrentImgName(BurpExtender.getImage(MessageInfo));
	}
}


@Deprecated
class MyThread implements Runnable{
	//this can let me call getImage(make http request in GUI),but can't return value
	private IHttpRequestResponse MessageInfo =null;
	MyThread(IHttpRequestResponse MessageInfo,JTextField imgPath){
		this.MessageInfo = MessageInfo;
	}
	@Override
	public synchronized  void  run() {
		BurpExtender.getImage(MessageInfo);
	}
}

@Deprecated
class MyThread1 implements Callable<String> {
	//can return vaule!!
	private IHttpRequestResponse MessageInfo =null;
	MyThread1(IHttpRequestResponse MessageInfo){
		this.MessageInfo = MessageInfo;
	}
	@Override
	public String call() throws Exception {
		// Here your implementation
		return BurpExtender.getImage(MessageInfo);
	}
}