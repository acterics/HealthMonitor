package com.acterics.healthmonitor.stompclient;

import java.util.Map;


public class Connection {
	private String url;
	
	private Map<String,String> headers;
	
	private int connecttimeout = 30;
	
	public Connection(String url, Map<String,String> headers) {
		this.url = url;
		this.headers = headers;
	}
	
	public String getUrl() {
		return url;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public int getConnecttimeout() {
		return connecttimeout;
	}

	public void setConnecttimeout(int connecttimeout) {
		this.connecttimeout = connecttimeout;
	}
}
