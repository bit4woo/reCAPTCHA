package test;

import burp.IIntruderAttack;
import burp.IIntruderPayloadGenerator;
import burp.IIntruderPayloadGeneratorFactory;

public class PayloadFactory implements IIntruderPayloadGeneratorFactory{
  
    @Override
    public String getGeneratorName() {
    	return "CAPTCHA";
    }

    @Override
    public IIntruderPayloadGenerator createNewInstance(IIntruderAttack attack) {
        return new IntruderPayloadGenerator();
    }
    
}
