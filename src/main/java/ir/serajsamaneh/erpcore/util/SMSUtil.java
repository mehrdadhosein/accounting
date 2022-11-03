package ir.serajsamaneh.erpcore.util;

import org.springframework.stereotype.Component;

import ir.serajsamaneh.util.ISMSUtil;

@Component("smsUtil")
public class SMSUtil implements ISMSUtil{

	@Override
	public void sendSMS(String telNo, String messageText) {
		
	}
}
