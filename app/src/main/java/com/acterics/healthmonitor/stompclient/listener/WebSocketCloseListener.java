package com.acterics.healthmonitor.stompclient.listener;


public interface WebSocketCloseListener {
	
	/**
	 * https://developer.mozilla.org/en-US/docs/Web/API/CloseEvent
	 * 
	 * @param code
	 * @param reason
	 * @param remote
	 * @throws Exception
	 */
	void onClose(int code, String reason, boolean remote) throws Exception;
}
