package test;

import burp.BurpExtender;
import burp.IBurpExtenderCallbacks;
import burp.IIntruderPayloadGenerator;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import custom.YunSu;

public class IntruderPayloadGenerator implements IIntruderPayloadGenerator {

    IBurpExtenderCallbacks callbacks = BurpExtender.getBurpCallbacks();
    
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
				String imgpath = getImage(imgMessageInfo);
				String code = yunsu.getCode(imgpath);
				stdout.println(imgpath+" "+code);
				return code.getBytes();
			}
			else {
				stdout.println("Failed try!!!");
				times +=1;
				continue;
			}
		}
		return null;
    }

    @Override
    public void reset() {
       
    }
}
