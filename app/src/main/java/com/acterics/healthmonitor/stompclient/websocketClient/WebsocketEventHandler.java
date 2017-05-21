package com.acterics.healthmonitor.stompclient.websocketClient;


public interface WebsocketEventHandler {
	
	void onOpen();

	void onMessage(String message);

	void onClose(int code, String reason, boolean remote);

	void onError(Exception ex);
}
