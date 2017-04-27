package com.dodsoneng.updatefactory;

public class UpdateVersionFactory {

	public UpdateVersion DisplayMessage(String msgType) {
		
		if (msgType.equals("alert")){
			
			return new UpdateVersionPopup();
			
		} else if (msgType.equals("notification")) {
			
			return null;//new UpdateVersionNotify();
			
		}
		
		return null;
		
	}
}
