package com.acterics.healthmonitor.stompclient.listener;

import com.acterics.healthmonitor.stompclient.frame.Frame;


public interface ReceiptListener {
	
	void onReceived(Frame frame);
}
