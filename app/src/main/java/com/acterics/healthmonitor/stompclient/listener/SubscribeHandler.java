package com.acterics.healthmonitor.stompclient.listener;

import com.acterics.healthmonitor.stompclient.StompHeaders;


public interface SubscribeHandler {
	
	void onReceived(final Object payload, StompHeaders stompHeaders);
}
