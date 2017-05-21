package com.acterics.healthmonitor.stompclient.listener;

import com.acterics.healthmonitor.stompclient.frame.Frame;


public interface ErrorListener {
	
	void onError(final Frame frame);
}
