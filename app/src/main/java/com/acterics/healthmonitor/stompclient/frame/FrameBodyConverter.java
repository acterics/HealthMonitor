package com.acterics.healthmonitor.stompclient.frame;

public interface FrameBodyConverter {
	
	Object fromFrame(String body, Class<?> targetClass);
	
	String toString(Object payload);
}
