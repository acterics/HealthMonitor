package com.acterics.healthmonitor.stompclient;

import com.acterics.healthmonitor.stompclient.listener.ReceiptListener;


public class Receipt {
	private String receiptId;
	
	private ReceiptListener receiptListener;

	public String getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public ReceiptListener getReceiptListener() {
		return receiptListener;
	}

	public void setReceiptListener(ReceiptListener receiptListener) {
		this.receiptListener = receiptListener;
	}
	
}
