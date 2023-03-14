package burp;

import com.alibaba.fastjson2.JSON;


public class Config {
	
	public static final String ExtensionConfigName = "reCAPTCHA";

	byte[] requestBytes = {};
	String protocol ="";
	String host = "";
	int port = -1;
	
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setRequestBytes(byte[] requestBytes) {
		this.requestBytes = requestBytes;
	}

	public IHttpService getHttpService(){
		IHttpService service = BurpExtender.getCallbacks().getHelpers().buildHttpService(host, port, protocol);
		return service;
	}
	
	public byte[] getRequestBytes(){
		return requestBytes;
	}
	
	public String ToJson() {
		return JSON.toJSONString(this);
	}

	public static Config FromJson(String instanceString) {
		return JSON.parseObject(instanceString,Config.class);
	}
	
	public void saveConfigToBurp() {
		BurpExtender.getCallbacks().saveExtensionSetting(ExtensionConfigName,this.ToJson());
	}
	
	public static Config LoadConfigFromBurp() {
		String Json = BurpExtender.getCallbacks().loadExtensionSetting(ExtensionConfigName);
		Config config = FromJson(Json);
		if (config == null) {
			config = new Config();
		}
		return config;
	}
}
