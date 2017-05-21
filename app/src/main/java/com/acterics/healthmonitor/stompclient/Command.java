package com.acterics.healthmonitor.stompclient;


public enum Command {
	CONNECT,
	CONNECTED,
	MESSAGE,
	RECEIPT,
	ERROR,
	DISCONNECT,
	SEND,
	SUBSCRIBE,
	UNSUBSCRIBE,
	ACK,
	NACK,
	COMMIT,
	ABORT,
	BEGIN
}
