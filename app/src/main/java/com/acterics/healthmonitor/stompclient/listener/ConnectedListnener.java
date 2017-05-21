package com.acterics.healthmonitor.stompclient.listener;

import com.acterics.healthmonitor.stompclient.StompHeaders;


public interface ConnectedListnener {
	
	void onConnected(StompHeaders stompHeaders);
}
