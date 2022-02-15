package com.increff.pos.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; 

public class InfoData {

	private String message;
	
	public InfoData() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		message = "Activity Time: " + dtf.format(now);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
