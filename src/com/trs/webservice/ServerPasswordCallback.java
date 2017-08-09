package com.trs.webservice;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;
public class ServerPasswordCallback implements CallbackHandler {
	private static final String USERNAME="ws-client"; //服务调用用户
	private static final String PSSWORD="11111111";//服务调用密码
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		System.out.println(callbacks.length);
		if(callbacks.length>0){
			for(int i=0;i<callbacks.length;i++){
				WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
				if (pc.getIdentifier().equals(USERNAME)){
					pc.setPassword(PSSWORD);
					//return;
				}
			}
			
		}
	}
}
