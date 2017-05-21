package com.acterics.healthmonitor.stompclient;


public class Transaction {
	
	String id;
	
	StompClient stompClient;
	
	Transaction(StompClient StompClient) {
		this.stompClient = StompClient;
	}
	
	public void commit() {
		stompClient.commit(id);
	}
	
	public void abort() {
		stompClient.abort(id);
	}
}
